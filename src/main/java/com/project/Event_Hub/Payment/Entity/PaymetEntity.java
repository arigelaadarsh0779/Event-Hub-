package com.project.Event_Hub.Payment.Entity;

import com.project.Event_Hub.Booking.Entity.Bookings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Payment")
public class PaymetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    @OneToOne
    private Bookings booking;

    private String razorpayOrderId;
    private String razorpayPaymetId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentsEnum status;
    private LocalDateTime createdAt;



}
