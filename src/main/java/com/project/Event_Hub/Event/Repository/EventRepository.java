package com.project.Event_Hub.Event.Repository;


import com.project.Event_Hub.Event.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {

    List<Event> findByEventTitle(String title);
    List<Event> findByEventTheme(String theme);
    List<Event> findByEventVenue(String venue);
}
