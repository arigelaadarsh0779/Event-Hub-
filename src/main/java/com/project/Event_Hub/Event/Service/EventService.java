package com.project.Event_Hub.Event.Service;

import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Repository.EventRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository){
        this.eventRepository=eventRepository;
    }


    public String createEvent(Event ev){
       eventRepository.save(ev);
       return "Event Created Sucessfully";
    }

    public List<Event> viewAllEvents(){
    return eventRepository.findAll();
    }
    public Event manageEventById(Event event){
        return eventRepository.save(event);
    }

    public List<Event> findEventByTitle(String Title){
        return eventRepository.findByEventTitle(Title);
    }
    public List<Event> findEventByTheme(String theme){
        return eventRepository.findByEventTheme(theme);
    }
    public List<Event> findEventByVenue(String venue){
        return eventRepository.findByEventVenue(venue);
    }
    public String deleteEventById(Long id){

       eventRepository.deleteById(id);
       return "Deleted Sucessfully";

    }


}
