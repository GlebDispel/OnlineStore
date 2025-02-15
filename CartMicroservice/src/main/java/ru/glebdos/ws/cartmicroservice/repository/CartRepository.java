package ru.glebdos.ws.cartmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glebdos.ws.cartmicroservice.model.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);
    Optional<Cart> deleteCartById(Long id);
}