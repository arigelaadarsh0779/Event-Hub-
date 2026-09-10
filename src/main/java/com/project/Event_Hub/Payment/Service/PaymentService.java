package com.project.Event_Hub.Payment.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Entity.BookingStatus;
import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Notification.Service.EmailSender;
import com.project.Event_Hub.Payment.Dto.PaymentRequestDto;
import com.project.Event_Hub.Payment.Dto.PaymentResponseDto;
import com.project.Event_Hub.Payment.Dto.PaymentVerifyRequestDto;
import com.project.Event_Hub.Payment.Entity.PaymentsEnum;
import com.project.Event_Hub.Payment.Entity.PaymetEntity;
import com.project.Event_Hub.Payment.Mapper.PaymentMapper;
import com.project.Event_Hub.Payment.Repository.PaymentRepository;
import com.project.Event_Hub.Ticket.DTO.TicketRequestDto;
import com.project.Event_Hub.Ticket.Service.TicketService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingsRepository bookingsRepository;
    private final PaymentMapper paymentMapper;
    private final EmailSender emailSender;
    private final TicketService ticketService;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public PaymentResponseDto createOrder(PaymentRequestDto dto) throws Exception {

        Bookings bookings = bookingsRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        int paise = bookings.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValueExact();

        System.out.println("Razorpay Key ID = " + keyId);

        RazorpayClient razorpayClient =
                new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", paise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", bookings.getBookingNumber());

        Order order =
                razorpayClient.orders.create(orderRequest);

        // Save payment details
        PaymetEntity payment = new PaymetEntity();

        payment.setBooking(bookings);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(bookings.getTotalAmount());
        payment.setStatus(PaymentsEnum.CREATED);
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        PaymentResponseDto response =
                paymentMapper.convertObjToResponse(payment);

        response.setKeyId(keyId);

        return response;
    }

    public boolean verifyPayment(PaymentVerifyRequestDto dto) throws Exception {

        PaymetEntity payment = paymentRepository
                .findByRazorpayOrderId(dto.getRazorpayOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        System.out.println("Saved Order ID: "
                + payment.getRazorpayOrderId());

        System.out.println("Received Order ID: "
                + dto.getRazorpayOrderId());

        System.out.println("Payment ID: "
                + dto.getRazorpayPaymentId());

        System.out.println("Signature: "
                + dto.getRazorpaySignature());

        JSONObject options = new JSONObject();

        options.put(
                "razorpay_order_id",
                payment.getRazorpayOrderId()
        );

        options.put(
                "razorpay_payment_id",
                dto.getRazorpayPaymentId()
        );

        options.put(
                "razorpay_signature",
                dto.getRazorpaySignature()
        );

        boolean verified =
                Utils.verifyPaymentSignature(options, keySecret);

        System.out.println("Verified: " + verified);

        if (!verified) {
            return false;
        }

        // Payment successful
        payment.setRazorpayPaymetId(
                dto.getRazorpayPaymentId()
        );

        payment.setStatus(PaymentsEnum.SUCCESS);

        // Get booking
        Bookings booking = payment.getBooking();

        User user = booking.getUser();

        Event event = booking.getEvent();

        // Confirm booking
        booking.setStatus(BookingStatus.CONFIRMED);

        // Save payment and booking
        paymentRepository.save(payment);
        bookingsRepository.save(booking);

        // Send booking confirmation email
        emailSender.sendEmail(
                user.getEmail(),
                "Booking Confirmed",
                "Hello " + user.getName() + ",\n\n" +
                        "🎉 Your payment was successful and your booking is confirmed!\n\n" +

                        "Booking Details\n" +
                        "Booking Number: " + booking.getBookingNumber() + "\n" +
                        "Event: " + event.getTitle() + "\n" +
                        "Number of Seats: " + booking.getNumberOfSeats() + "\n" +
                        "Booking Status: CONFIRMED\n\n" +

                        "Payment Details\n" +
                        "Payment ID: " + payment.getRazorpayPaymetId() + "\n" +
                        "Amount Paid: ₹" + payment.getAmount() + "\n" +
                        "Payment Status: SUCCESS\n\n" +
                        "Your Ticket will be Sent Shortly to your mail \n"+

                        "Thank you for booking with Event Hub!\n\n" +
                        "Event Hub Team"
        );

        // Generate ticket after successful payment
        TicketRequestDto ticketRequestDto =
                new TicketRequestDto();

        ticketRequestDto.setBookingId(
                booking.getBookingId()
        );

        ticketService.genereateTicket(ticketRequestDto);

        return true;
    }
}