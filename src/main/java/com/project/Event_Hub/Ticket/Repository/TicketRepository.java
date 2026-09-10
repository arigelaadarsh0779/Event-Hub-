 package com.project.Event_Hub.Ticket.Repository;

import com.project.Event_Hub.Ticket.Entity.TicketEntity;
import com.project.Event_Hub.Ticket.Entity.TicketEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
}