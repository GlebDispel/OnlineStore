package ru.glebdos.ws.productmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.glebdos.ws.productmicroservice.dto.ProductDTO;
import ru.glebdos.ws.productmicroservice.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Optional<Product> findByName(String name);
}