package ru.glebdos.ws.productmicroservice.dto;

import lombok.Data;

@Data
public class CartUpdateMessage {
    private Long userId;
    private Long productId;
    private int quantity;
    private String action;
}
