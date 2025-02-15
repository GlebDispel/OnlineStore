package ru.glebdos.ws.core;


public class CartUpdateMessage {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String action;

    public CartUpdateMessage() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "CartUpdateMessage{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", action='" + action + '\'' +
                '}';
    }
}
