package ru.glebdos.ws.cartmicroservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import ru.glebdos.ws.cartmicroservice.model.Cart;
import ru.glebdos.ws.cartmicroservice.service.CartService;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {


    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;


    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Cart> addProductToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) throws ExecutionException, InterruptedException, JsonProcessingException {
        log.info("Adding product to Cart");
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
    }

    @PostMapping("/{userId}/remove")
    public ResponseEntity<Cart> removeProductFromCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId, quantity));
    }

    @PostMapping("/{userId}/create_order")
    public ResponseEntity<Cart> createOrder(@PathVariable Long userId) throws ExecutionException, InterruptedException {

        return ResponseEntity.ok(cartService.createOrder(userId));

    }



}