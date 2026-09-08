package com.project.Event_Hub.Payment.Mapper;

import com.project.Event_Hub.Payment.Dto.PaymentResponseDto;
import com.project.Event_Hub.Payment.Entity.PaymetEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponseDto convertObjToResponse(PaymetEntity payment) {

        PaymentResponseDto response = new PaymentResponseDto();

        response.setOrderId(payment.getRazorpayOrderId());

        response.setAmount(payment.getAmount());
        response.setCurrency("INR");
        response.setStatus(payment.getStatus());

        return response;
    }
}