package ru.glebdos.ws.productmicroservice.service;

import ru.glebdos.ws.productmicroservice.dto.ProductDTO;
import ru.glebdos.ws.productmicroservice.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    void decreaseProductQuantity(Long productId, int quantity);
    void increaseProductQuantity(Long productId, int quantity);
}
