package com.project.Event_Hub.Event.Service;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Mapper.EventMapper;
import com.project.Event_Hub.Event.Repository.EventRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService implements EventsInterface{
    private final EventRepository eventRepository;
    private final EventMapper eventmapper;

    public EventService(EventRepository eventRepository, EventMapper eventmapper){
        this.eventRepository=eventRepository;
        this.eventmapper=eventmapper;
    }


    public String createEvent(RequestEventDto dto){
        Event event  = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVenue(dto.getVenue());
        event.setThemeOfTheProject(dto.getThemeOfTheProject());
        event.setOrganizer(dto.getOrganizer());
        event.setAvailableSeats(dto.getRemainingSeats());
        event.setTotalSeats(dto.getTotalSeats());

        eventRepository.save(event);
       return "Event Created Sucessfully";
    }

    public List<ResponseEventDto> viewAllEvents(){
    return eventRepository.findAll().stream()
            .map(eventmapper::objToRespose).toList();
    }

    public ResponseEventDto manageEventById( long id , RequestEventDto dto){
       Event event = eventRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found "));
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVenue(dto.getVenue());
        event.setThemeOfTheProject(dto.getThemeOfTheProject());
        event.setOrganizer(dto.getOrganizer());
        event.setAvailableSeats(dto.getRemainingSeats());
        event.setTotalSeats(dto.getTotalSeats());

        Event ee =eventRepository.save(event);
        return eventmapper.objToRespose(ee);


    }

    public List<ResponseEventDto> findByTitle(String Title){
        return eventRepository.findByTitle(Title)
                .stream()
                .map(eventmapper::objToRespose)
                .toList();
    }


    public List<ResponseEventDto> findEventByTheme(String ThemeOfTheProject){
        return eventRepository.findByThemeOfTheProject(ThemeOfTheProject)
                .stream()
                .map(eventmapper::objToRespose)
                .toList();
    }


    public List<ResponseEventDto> findByEventVenue(String Venue){
        return eventRepository.findByVenue(Venue).stream()
                .map(eventmapper::objToRespose).toList();
    }

    public String deleteEventById(Long id){
       eventRepository.deleteById(id);
       return "Deleted Sucessfully";
    }


}
