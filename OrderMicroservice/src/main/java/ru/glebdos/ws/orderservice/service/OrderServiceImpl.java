package ru.glebdos.ws.orderservice.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glebdos.ws.core.OrderMessage;
import ru.glebdos.ws.core.UpdateOrderStatusRequest;
import ru.glebdos.ws.orderservice.model.Order;
import ru.glebdos.ws.orderservice.model.OrderStatus;
import ru.glebdos.ws.orderservice.repository.OrderRepository;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ObjectMapper objectMapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    @KafkaListener(topics = "create-order-topic", groupId = "cart-service")
    public void createOrder(OrderMessage orderMessage) {
        log.info("OrderMessage received: " + orderMessage);
        Order order = objectMapper.convertValue(orderMessage, Order.class);
        order.setId(null);
        order.setStatus(OrderStatus.CREATED);
        log.info("OrderMessage map to Order : " + order);

        orderRepository.save(order);


        log.info("Order created: " + order);

    }
// ПРОДОЛЖИ ТУТ
    @KafkaListener(topics = "create-order-topic-dlt", groupId = "cart-service")
    public void createOrderDLT(OrderMessage orderMessage) {
        log.info("OrderMessage from dlt: " + orderMessage);
    }

    public List<Long> getOrders() {
     return orderRepository.findAllOrderId();
    }

    @Transactional
    @Override
    public void updateStatus(UpdateOrderStatusRequest updateOrderStatus) {
        log.info("Статус и номера заказов здесь  : " + updateOrderStatus);
        OrderStatus status = objectMapper
                .convertValue(updateOrderStatus.getStatus(), OrderStatus.class);
        List<Order> orders = orderRepository.findAllByOrderIds(updateOrderStatus.getOrderIds());
        for (Order order : orders) {
            order.setStatus(status);
        }


    }


}
