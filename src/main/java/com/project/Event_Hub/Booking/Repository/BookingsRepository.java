package com.project.Event_Hub.Booking.Repository;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Event.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingsRepository extends JpaRepository<Bookings,Long>{
    List<Bookings> findByUser(User user);
    List<Bookings> findByEvent(Event event);
}
