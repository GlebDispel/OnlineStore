package ru.glebdos.ws.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.ws.core.UpdateOrderStatusRequest;
import ru.glebdos.ws.orderservice.model.Order;
import ru.glebdos.ws.orderservice.model.OrderStatus;
import ru.glebdos.ws.orderservice.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/get")
    public List<Long> getOrder() {
        return orderService.getOrders();
    }

    @PostMapping("/update_status")
    private ResponseEntity<String> updateOrderStatus(@RequestBody UpdateOrderStatusRequest updateOrders){

        orderService.updateStatus(updateOrders);

        return ResponseEntity.ok("Статусы заказов обновлены");
    }
}
