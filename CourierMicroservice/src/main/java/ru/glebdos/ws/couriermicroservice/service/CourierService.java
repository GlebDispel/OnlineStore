package ru.glebdos.ws.couriermicroservice.service;

import ru.glebdos.ws.couriermicroservice.model.Courier;
import ru.glebdos.ws.couriermicroservice.model.CourierStatus;

import java.util.List;

public interface CourierService {


    List<Courier> getAllCouriers();
     void chooseCourierForDelivery();
}
