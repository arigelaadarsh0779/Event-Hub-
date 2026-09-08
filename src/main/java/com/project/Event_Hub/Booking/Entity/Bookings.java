package com.project.Event_Hub.Booking.Entity;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Event.Entity.Event;
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
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    private int numberOfSeats;

    private LocalDateTime bookingDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String bookingNumber;

    private BigDecimal totalAmount;

    @ManyToOne
    private Event event;

    @ManyToOne
    private User user;
}