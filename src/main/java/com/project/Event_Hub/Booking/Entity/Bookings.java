package com.project.Event_Hub.Booking.Entity;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Event.Entity.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int NumberOfSeats;

    private LocalDateTime Bookingdate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private String BookingNumber;
    @ManyToOne
    private Event event;
    @ManyToOne
    private User user;
}
