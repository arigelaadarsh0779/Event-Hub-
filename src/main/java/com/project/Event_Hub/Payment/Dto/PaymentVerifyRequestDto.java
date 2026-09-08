package com.project.Event_Hub.Payment.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
@Data
@Component
@AllArgsConstructor@NoArgsConstructor
public class PaymentVerifyRequestDto {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;


}
