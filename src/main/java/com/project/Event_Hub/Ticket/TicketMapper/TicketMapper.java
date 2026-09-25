package com.project.Event_Hub.Ticket.TicketMapper;



import com.project.Event_Hub.Ticket.DTO.TicketResponseDto;
import com.project.Event_Hub.Ticket.Entity.TicketEntity;

import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponseDto toResponseDto(TicketEntity ticket) {

        TicketResponseDto dto = new TicketResponseDto();
        dto.setTicketId(ticket.getTicketId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setGeneratedAt(ticket.getGeneratedAt());
        dto.setStatus(ticket.getStatus());

        if (ticket.getBooking() != null) {
            dto.setBookingId(ticket.getBooking().getBookingId());
            dto.setNumberOfSeats(ticket.getBooking().getNumberOfSeats());

            if (ticket.getBooking().getUser() != null) {
                dto.setUserName(ticket.getBooking().getUser().getUsername());
            }

            if (ticket.getBooking().getEvent() != null) {
                var event = ticket.getBooking().getEvent();
                dto.setEventTitle(event.getTitle());
                dto.setEventVenue(event.getVenue());
                dto.setEventDate(event.getDate() != null ? event.getDate().toString() : "TBA");
                dto.setEventTime(event.getStartTime() + " - " + event.getEndTime());
                dto.setEventTheme(event.getThemeOfTheProject());
            }
        }

        return dto;
    }
}