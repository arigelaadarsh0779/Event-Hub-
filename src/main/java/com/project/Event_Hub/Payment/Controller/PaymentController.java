package com.project.Event_Hub.Payment.Controller;

import com.project.Event_Hub.Payment.Dto.PaymentRequestDto;
import com.project.Event_Hub.Payment.Dto.PaymentResponseDto;
import com.project.Event_Hub.Payment.Dto.PaymentVerifyRequestDto;
import com.project.Event_Hub.Payment.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/api/user/payment/createorder")
    public ResponseEntity<PaymentResponseDto> createorder(@RequestBody PaymentRequestDto  dto) throws Exception {
        return
                ResponseEntity
                        .status(200)
                        .body(paymentService.createOrder(dto));
    }

    @PostMapping("/api/user/payment/verify")
    public ResponseEntity<String> verifyPayment(
            @RequestBody PaymentVerifyRequestDto dto) throws Exception {

        boolean verified = paymentService.verifyPayment(dto);

        if (verified) {
            return
                    ResponseEntity
                            .status(200)
                            .body("Payment verified successfully");
        }

        return ResponseEntity
                .status(400)
                .body("Payment verification failed");
    }
}
