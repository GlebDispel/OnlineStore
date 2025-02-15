package ru.glebdos.ws.couriermicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.glebdos.ws.core.UpdateOrderStatusRequest;
import ru.glebdos.ws.couriermicroservice.model.Courier;
import ru.glebdos.ws.couriermicroservice.model.CourierStatus;
import ru.glebdos.ws.couriermicroservice.repository.CourierRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

    private static final Logger log = LoggerFactory.getLogger(CourierServiceImpl.class);
    private final RestTemplate restTemplate;
    private final CourierRepository courierRepository;
    private final DeliveryService deliveryService;


    @Autowired
    public CourierServiceImpl(RestTemplate restTemplate, CourierRepository courierRepository, DeliveryService deliveryService) {
        this.restTemplate = restTemplate;
        this.courierRepository = courierRepository;
        this.deliveryService = deliveryService;
    }


    @Override
    public List<Courier> getAllCouriers() {
        return courierRepository.findByStatus(CourierStatus.AVAILABLE);
    }

    //не одной кафкой едины
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void chooseCourierForDelivery() {
        List<Long> ordersId = restTemplate.exchange("http://localhost:8003/order/get"
                , HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Long>>() {
                }).getBody();

        if (ordersId == null || ordersId.isEmpty()) {
            log.info("Список заказов пуст или равен null. Завершение метода.");
            return; // Завершить выполнение метода
        }

        List<Long> changeStatusMessage = new ArrayList<>();

        log.info(ordersId.toString() + " список заказов прибыл");

        List<Courier> couriers = courierRepository.findByStatus(CourierStatus.AVAILABLE);
        log.info("список курьеров: {}", couriers);

        for (int i = 0; i < Math.min(ordersId.size(), couriers.size()); i++) {
            Long orderId = ordersId.get(i);
            Courier courier = couriers.get(i);
            courier.setOrderId(orderId);
            courier.setOrderCount(courier.getOrderCount() + 1);
            courier.setStatus(CourierStatus.BUSY);
            courierRepository.save(courier);
            changeStatusMessage.add(orderId);
            deliveryService.deliverOrder(courier);

            log.info("Заказ " + orderId + " назначен курьеру " + courier.getId());
        }
        log.info("Выдача заказов закончена");


        if (!changeStatusMessage.isEmpty()) {

            UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(changeStatusMessage, "IN_TRANSIT");

            restTemplate.postForEntity("http://localhost:8003/order/update_status", request, String.class);
            log.info("Статус заказов обновлен на IN_TRANSIT: {}", changeStatusMessage);
        }

    }
}
