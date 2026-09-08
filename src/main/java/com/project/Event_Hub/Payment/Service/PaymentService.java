package com.project.Event_Hub.Payment.Service;

import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Payment.Dto.PaymentRequestDto;
import com.project.Event_Hub.Payment.Dto.PaymentResponseDto;
import com.project.Event_Hub.Payment.Entity.PaymentsEnum;
import com.project.Event_Hub.Payment.Entity.PaymetEntity;
import com.project.Event_Hub.Payment.Mapper.PaymentMapper;
import com.project.Event_Hub.Payment.Repository.PaymentRepository;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Value;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingsRepository bookingsRepository;
    private final PaymentMapper paymentMapper;


    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public PaymentResponseDto createOrder(PaymentRequestDto dto) throws Exception{
        Bookings bookings = bookingsRepository.findById(dto.getBookingId()).orElseThrow(()->new RuntimeException(" bookinf=gs not found"));

        int paise = bookings.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValueExact();


        RazorpayClient razorpayClient= new RazorpayClient(keyId,keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",paise);
        orderRequest.put("currency","INR");
        orderRequest.put("receipt",bookings.getBookingNumber());

        Order order =
                razorpayClient.orders.create(orderRequest);

        // Save payment details
       PaymetEntity payment= new PaymetEntity();

        payment.setBooking(bookings);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(bookings.getTotalAmount());
        payment.setStatus(PaymentsEnum.CREATED);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return paymentMapper.convertObjToResponse(payment);
    }



}
