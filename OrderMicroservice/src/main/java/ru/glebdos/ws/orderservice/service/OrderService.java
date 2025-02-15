package ru.glebdos.ws.orderservice.service;

import ru.glebdos.ws.core.OrderMessage;
import ru.glebdos.ws.core.UpdateOrderStatusRequest;
import ru.glebdos.ws.orderservice.model.Order;

import java.util.List;

public interface OrderService {

    void createOrder(OrderMessage orderMessage);
    List<Long> getOrders();
    public void updateStatus(UpdateOrderStatusRequest updateOrderStatus);
}
