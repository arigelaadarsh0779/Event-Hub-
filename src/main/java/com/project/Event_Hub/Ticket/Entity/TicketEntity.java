package com.project.Event_Hub.Ticket.Entity;


import com.project.Event_Hub.Booking.Entity.Bookings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    private String ticketNumber;

    private String qrCode;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    private TicketEnum status;

    @OneToOne
    private Bookings booking;
}
