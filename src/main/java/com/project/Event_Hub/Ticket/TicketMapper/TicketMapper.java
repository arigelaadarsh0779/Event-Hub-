package com.project.Event_Hub.Ticket.TicketMapper;



import com.project.Event_Hub.Ticket.DTO.TicketResponseDto;
import com.project.Event_Hub.Ticket.Entity.TicketEntity;

import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDto toResponseDto(TicketEntity ticket) {

        return new TicketResponseDto(
                ticket.getTicketId(),
                ticket.getTicketNumber(),
                ticket.getBooking().getBookingId(),
                ticket.getGeneratedAt(),
                ticket.getStatus()
        );
    }
}