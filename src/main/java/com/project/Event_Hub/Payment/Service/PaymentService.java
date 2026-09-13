package com.project.Event_Hub.Payment.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Entity.BookingStatus;
import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Exception.BookingsNotFoundException;
import com.project.Event_Hub.Notification.EmailSender;
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
                .orElseThrow(() ->
                        new BookingsNotFoundException("Booking not found"));

        // Prevent payment for an already confirmed booking
        if (bookings.getStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException(
                    "Booking is already confirmed and payment is completed"
            );
        }

        int paise = bookings.getTotalAmount()
                .multiply(BigDecimal.valueOf(100))
                .intValueExact();

        RazorpayClient razorpayClient =
                new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", paise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", bookings.getBookingNumber());

        Order order =
                razorpayClient.orders.create(orderRequest);

        // Save payment
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

        // Verify Razorpay payment
        boolean verified =
                Utils.verifyPaymentSignature(options, keySecret);

        System.out.println("Verified: " + verified);

        // Payment verification failed
        if (!verified) {
            return false;
        }

        // ==========================================
        // PAYMENT SUCCESS
        // ==========================================

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

        System.out.println("Payment saved as SUCCESS");
        System.out.println("Booking saved as CONFIRMED");

        // ==========================================
        // GENERATE TICKET
        // ==========================================

        TicketRequestDto ticketRequestDto =
                new TicketRequestDto();

        ticketRequestDto.setBookingId(
                booking.getBookingId()
        );

        try {

            ticketService.genereateTicket(ticketRequestDto);

            System.out.println("Ticket generated successfully");

        } catch (Exception e) {

            System.out.println(
                    "Ticket generation failed, but payment was successful: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // SEND EMAIL
        // ==========================================

        try {

            emailSender.sendEmail(
                    user.getEmail(),
                    "Booking Confirmed",
                    "Hello " + user.getName() + ",\n\n" +
                            "🎉 Your payment was successful and your booking is confirmed!\n\n" +

                            "Booking Details\n" +
                            "Booking ID: " + booking.getBookingId() + "\n" +
                            "Booking Number: " + booking.getBookingNumber() + "\n" +
                            "Event: " + event.getTitle() + "\n" +
                            "Number of Seats: " + booking.getNumberOfSeats() + "\n" +
                            "Booking Status: CONFIRMED\n\n" +

                            "Payment Details\n" +
                            "Payment ID: " + payment.getRazorpayPaymetId() + "\n" +
                            "Amount Paid: ₹" + payment.getAmount() + "\n" +
                            "Payment Status: SUCCESS\n\n" +

                            "Your ticket has been generated.\n\n" +

                            "Thank you for booking with Event Hub!\n\n" +
                            "Event Hub Team"
            );

            System.out.println("Confirmation email sent successfully");

        } catch (Exception e) {

            System.out.println(
                    "Email sending failed, but payment was successful: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // FINAL PAYMENT RESULT
        // ==========================================

        return true;
    }
}