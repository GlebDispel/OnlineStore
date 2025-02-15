package ru.glebdos.ws.core;

import java.util.Map;


public class OrderMessage {


    private Long id;

    private Long userId;

    private Map<Long, Integer> products;

    public OrderMessage() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<Long, Integer> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "id=" + id +
                ", userId=" + userId +
                ", products=" + products +
                '}';
    }
}
