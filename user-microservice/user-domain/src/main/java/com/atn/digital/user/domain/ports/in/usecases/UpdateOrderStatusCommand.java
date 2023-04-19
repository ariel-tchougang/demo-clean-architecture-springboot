package com.atn.digital.user.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateOrderStatusCommand extends SelfValidating<UpdateOrderStatusCommand> {

    @NotNull
    @NotBlank
    String orderId;

    @NotNull
    @NotBlank
    String userId;

    @NotNull
    @NotBlank
    String newStatus;

    String reason;

    public UpdateOrderStatusCommand(String orderId, String userId, String newStatus, String reason) {
        this.orderId = orderId;
        this.userId = userId;
        this.newStatus = newStatus;
        this.reason = reason;
        this.validateSelf();
    }
}
