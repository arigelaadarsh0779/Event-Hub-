 package com.project.Event_Hub.Ticket.Repository;

import com.project.Event_Hub.Ticket.Entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Optional<TicketEntity> findByBooking_BookingId(Long bookingId);

    List<TicketEntity> findByBooking_User_Userid(Long userId);

}