package ru.glebdos.ws.couriermicroservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.glebdos.ws.core.UpdateOrderStatusRequest;
import ru.glebdos.ws.couriermicroservice.model.Courier;
import ru.glebdos.ws.couriermicroservice.model.CourierStatus;
import ru.glebdos.ws.couriermicroservice.repository.CourierRepository;

import java.util.List;

@Service
public class DeliveryService {

    private final CourierRepository courierRepository;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);
    @Autowired
    public DeliveryService(CourierRepository courierRepository, RestTemplate restTemplate) {
        this.courierRepository = courierRepository;
        this.restTemplate = restTemplate;
    }


    @Async
    @Transactional
    public void deliverOrder(Courier courier) {
        try {
            Thread.sleep(60*1000);
            Long courierId = courier.getId();
            Long orderId = courier.getOrderId();
            courier.setStatus(CourierStatus.AVAILABLE);
            courier.setOrderId(null);
            courierRepository.save(courier);

            UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(List.of(orderId), "DELIVERED");
            restTemplate.postForEntity("http://localhost:8003/order/update_status", request, String.class);

            log.info("Курьер {} завершил доставку заказа {}", courierId, orderId);
        } catch (InterruptedException e) {
            log.error("Ошибка при доставке заказа {}", courier.getOrderId(), e);
            Thread.currentThread().interrupt();
        }catch (Exception e) {
            log.error("Ошибка при доставке заказа {}", courier.getOrderId(), e);
        }
    }
}
