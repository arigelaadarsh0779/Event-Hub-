package com.project.Event_Hub.Event.Service;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Mapper.EventMapper;
import com.project.Event_Hub.Event.Repository.EventRepository;
import com.project.Event_Hub.Exception.DateExpiredExeception;
import com.project.Event_Hub.Exception.EventNotFoundException;
import com.project.Event_Hub.Exception.NoEventFoundException;
import com.project.Event_Hub.Exception.TitleNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        LocalDate today = LocalDate.now();
        if(dto.getDate().isAfter(today))
        event.setDate(dto.getDate());
        else throw new DateExpiredExeception("The Date Must be in Future..!!");


        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVenue(dto.getVenue());
        event.setThemeOfTheProject(dto.getThemeOfTheProject());
        event.setOrganizer(dto.getOrganizer());
        event.setAvailableSeats(dto.getRemainingSeats());
        event.setTotalSeats(dto.getTotalSeats());
        event.setTicketPrice(dto.getTicketPrice());

        eventRepository.save(event);
       return "Event Created Sucessfully";
    }

    public List<ResponseEventDto> viewAllEvents(){
        List<Event> event =eventRepository.findAll();
        if(event!=null) {
            return eventRepository.findAll().stream()
                    .map(eventmapper::objToRespose).toList();
        }
        else throw new NoEventFoundException("No Events Are Available Right Now ");
    }

    public ResponseEventDto manageEventById( long id , RequestEventDto dto){
       Event event = eventRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found "));
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setTicketPrice(dto.getTicketPrice());
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

    public ResponseEventDto findByTitle(String Title){
//
         Event ev =eventRepository.findByTitle(Title);
         if (ev!=null)
         return eventmapper.objToRespose(ev);
         else throw  new TitleNotFoundException("No Event Found With That Title");

    }


    public List<ResponseEventDto> findEventByTheme(String ThemeOfTheProject){
        List<Event> ev = eventRepository.findByThemeOfTheProject(ThemeOfTheProject);
        if (ev!= null) {

            return ev
                    .stream()
                    .map(eventmapper::objToRespose)
                    .toList();
        }
        else throw new EventNotFoundException("No Events are found");
    }


    public List<ResponseEventDto> findByEventVenue(String Venue){
        List<Event> ev = eventRepository.findByVenue(Venue);
        if(ev!=null){
        return ev.stream()
                .map(eventmapper::objToRespose).toList();
         }
        else throw new NoEventFoundException("No Events are found");
    }

    public String deleteEventById(Long id){
        Event ev = eventRepository.findById(id).orElseThrow(()->new NoEventFoundException("No Events are found "));
       eventRepository.delete(ev);
       return "Deleted Sucessfully";
    }


}
