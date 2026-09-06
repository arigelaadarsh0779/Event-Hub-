package com.project.Event_Hub.Event.Repository;


import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Event.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findByTitle(String Title);
    List<Event> findByThemeOfTheProject(String ThemeOfTheProject);
    List<Event> findByVenue(String Venue);

}
