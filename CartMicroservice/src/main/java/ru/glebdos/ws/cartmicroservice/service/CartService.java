package ru.glebdos.ws.cartmicroservice.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.glebdos.ws.cartmicroservice.dto.UserMessage;
import ru.glebdos.ws.cartmicroservice.model.Cart;
import ru.glebdos.ws.cartmicroservice.repository.CartRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class CartService {


    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private final CartRepository cartRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart addProductToCart(Long userId, Long productId, int quantity) throws ExecutionException, InterruptedException {
        log.info("add product to cart here");
        Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
        if (cart.getProducts() == null) {
            cart.setProducts(new HashMap<>());
        }
        Map<Long, Integer> products = cart.getProducts();
        products.put(productId, products.getOrDefault(productId, 0) + quantity);
        cart.setProducts(products);


        String message = String.format("{\"userId\": %d, \"productId\": %d, \"quantity\": %d, \"action\": \"ADD\"}", userId, productId, quantity);
        log.info("============================================================================");
        log.info(message);
        kafkaTemplate.send("cart-updates", message).get();

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


        String message = String.format("{\"userId\": %d, \"productId\": %d, \"quantity\": %d, \"action\": \"REMOVE\"}", userId, productId, quantity);
        kafkaTemplate.send("cart-updates", message);

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
}