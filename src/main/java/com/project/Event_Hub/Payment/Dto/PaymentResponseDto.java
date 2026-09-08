package com.project.Event_Hub.Payment.Dto;

import com.project.Event_Hub.Payment.Entity.PaymentsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String keyId;
    private PaymentsEnum Status;
}
