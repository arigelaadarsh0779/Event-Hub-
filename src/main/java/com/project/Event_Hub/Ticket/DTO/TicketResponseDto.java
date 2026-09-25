package com.project.Event_Hub.Ticket.DTO;


import com.project.Event_Hub.Ticket.Entity.TicketEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDto {

    private long ticketId;
    private String ticketNumber;
    private long bookingId;
    private LocalDateTime generatedAt;
    @Enumerated(EnumType.STRING)
    private TicketEnum status;

    // Event details
    private String eventTitle;
    private String eventVenue;
    private String eventDate;
    private String eventTime;
    private String eventTheme;

    // Booking details
    private int numberOfSeats;
    private String userName;
}