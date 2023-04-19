package com.atn.digital.user.domain.ports.out.persistence;

public interface UpdateOrderStatusPort {
    void updateStatus(String userId, String orderId, String status, String reason);
}
