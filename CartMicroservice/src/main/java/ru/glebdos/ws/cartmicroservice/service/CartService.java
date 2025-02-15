package ru.glebdos.ws.cartmicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glebdos.ws.cartmicroservice.model.Cart;
import ru.glebdos.ws.cartmicroservice.repository.CartRepository;
import ru.glebdos.ws.core.CartUpdateMessage;
import ru.glebdos.ws.core.OrderMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
public class CartService {


    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public CartService(CartRepository cartRepository, KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.cartRepository = cartRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("add product to cart here");
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());

        Map<Long, Integer> products = cart.getProducts();
        products.put(productId, products.getOrDefault(productId, 0) + quantity);
        cart.setProducts(products);

        CartUpdateMessage message = new CartUpdateMessage();
        message.setQuantity(quantity);
        message.setProductId(productId);
        message.setAction("ADD");
        message.setUserId(userId);


        Message<CartUpdateMessage> toProductMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, "product-update-topic")
                .build();


        kafkaTemplate.send(toProductMessage).get();

        return cartRepository.save(cart);
    }

    public Cart removeProductFromCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Map<Long, Integer> products = cart.getProducts();
        if (products.containsKey(productId)) {
            int currentQuantity = products.get(productId);
            if (currentQuantity <= quantity) {
                products.remove(productId);
            } else {
                products.put(productId, currentQuantity - quantity);
            }
        }
        CartUpdateMessage message = new CartUpdateMessage();
        message.setQuantity(quantity);
        message.setProductId(productId);
        message.setAction("REMOVE");
        message.setUserId(userId);




        kafkaTemplate.send("product-update-topic", message);

        return cartRepository.save(cart);
    }

    @KafkaListener(topics = "user-created-topic", groupId = "cart-service")
    public void listen(Long userMessage) {

        log.info("{} received", userMessage);
        Cart cart = new Cart();
        cart.setUserId(userMessage);
        cart.setProducts(new HashMap<>());


        cartRepository.save(cart);
    }

    @Transactional
    public Cart createOrder(Long userId ) throws ExecutionException, InterruptedException {
        Cart cartToOrder = getCartByUserId(userId);

        OrderMessage message = objectMapper.convertValue(cartToOrder, OrderMessage.class);
        log.info("received order: {}", message.toString());

        kafkaTemplate.send("create-order-topic", message).get();
        log.info("Сообщение успешно отправлено ");
        cartToOrder.setProducts(new HashMap<Long,Integer>());
        var updatedCart = cartRepository.save(cartToOrder);
        log.info("Обновленная карзина : {}", updatedCart);

        return cartToOrder;

        //продолжишь тут
    }



}