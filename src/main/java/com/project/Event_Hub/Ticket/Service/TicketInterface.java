package com.project.Event_Hub.Ticket.Service;

import com.project.Event_Hub.Ticket.DTO.TicketRequestDto;
import com.project.Event_Hub.Ticket.DTO.TicketResponseDto;

public interface TicketInterface {
   TicketResponseDto genereateTicket (TicketRequestDto dto);
}
