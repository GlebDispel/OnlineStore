package ru.glebdos.ws.core;

import java.util.List;


public class UpdateOrderStatusRequest {
    private List<Long> orderIds;
    private String status;

    public UpdateOrderStatusRequest() {
    }

    public UpdateOrderStatusRequest(List<Long> orderIds, String status) {
        this.orderIds = orderIds;
        this.status = status;
    }

    public List<Long> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<Long> orderIds) {
        this.orderIds = orderIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateOrderStatusRequest{" +
                "orderIds=" + orderIds +
                ", status='" + status + '\'' +
                '}';
    }
}
