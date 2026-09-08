package com.project.Event_Hub.Payment.Controller;

import com.project.Event_Hub.Payment.Dto.PaymentRequestDto;
import com.project.Event_Hub.Payment.Dto.PaymentResponseDto;
import com.project.Event_Hub.Payment.Dto.PaymentVerifyRequestDto;
import com.project.Event_Hub.Payment.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/createorder")
    public PaymentResponseDto createorder(@RequestBody PaymentRequestDto  dto) throws Exception {
        return paymentService.createOrder(dto);
    }

    @PostMapping("/verify")
    public String verifyPayment(
            @RequestBody PaymentVerifyRequestDto dto) throws Exception {

        boolean verified = paymentService.verifyPayment(dto);

        if (verified) {
            return "Payment verified successfully";
        }

        return "Payment verification failed";
    }
}
