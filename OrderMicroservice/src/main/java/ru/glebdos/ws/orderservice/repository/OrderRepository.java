package ru.glebdos.ws.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.glebdos.ws.orderservice.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.id FROM orders o where o.status = 'CREATED'")
    List<Long> findAllOrderId();
    @Query("SELECT o FROM orders o WHERE o.id IN :ordersId")
    List<Order> findAllByOrderIds(@Param("ordersId") List<Long> ordersId);

}
