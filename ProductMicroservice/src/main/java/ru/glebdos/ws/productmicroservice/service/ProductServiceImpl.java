package ru.glebdos.ws.productmicroservice.service;




import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import ru.glebdos.ws.core.CartUpdateMessage;
import ru.glebdos.ws.productmicroservice.model.Product;
import ru.glebdos.ws.productmicroservice.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class ProductServiceImpl implements ProductService {


    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }


    @KafkaListener(topics = "product-update-topic", groupId = "product_group")
    public void handleCartUpdate(CartUpdateMessage cartUpdateMessage) {
        log.info("Received cart update: {}", cartUpdateMessage);

        Long productId = cartUpdateMessage.getProductId();
        int quantity = cartUpdateMessage.getQuantity();
        String action = cartUpdateMessage.getAction();
        log.info("Received update for product with id: {}", productId);
        log.info("Received update for product with quantity: {}", quantity);
        log.info("Received update for product with action: {}", action);
        if ("ADD".equals(action)) {
            decreaseProductQuantity(productId, quantity);
        } else if ("REMOVE".equals(action)) {
            increaseProductQuantity(productId, quantity);
        }


    }

    @KafkaListener(topics = "product-update-topic-dlt", groupId = "product_group")
    public void handleDLTMessage(String message) {
        log.error(" Message in DLT: {}", message);
    }

    public void decreaseProductQuantity(Long productId, int quantity) {
        log.info("Decrease product quantity: {}", quantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough products in stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    public void increaseProductQuantity(Long productId, int quantity) {
        log.info("Increase product quantity: {}", quantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }
}
