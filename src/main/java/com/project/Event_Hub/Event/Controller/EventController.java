package com.project.Event_Hub.Event.Controller;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/events")
@RestController
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @GetMapping
    public List<ResponseEventDto> getAllEvents() {
        return eventService.viewAllEvents();
    }


    @PostMapping("/add")
    public String createEvent(@RequestBody RequestEventDto ev){
        eventService.createEvent(ev);
        return "Event created Susccesfully";
    }
    @PostMapping("/edit/{id}")
    public ResponseEventDto editEvent(@PathVariable long  id , @RequestBody RequestEventDto ev){
        return eventService.manageEventById(id,ev);
    }
    @GetMapping("/{title}")
    public List<ResponseEventDto> findByTitle(@PathVariable String title){
        return eventService.findByTitle(title);
    }
    @GetMapping("/{theme}" )
    public List<ResponseEventDto> findBytheme(@PathVariable String theme){
        return eventService.findEventByTheme(theme);
    }
    @GetMapping("/{venue}")
    public List<ResponseEventDto> findByeventVenue(@PathVariable String venue){
        return eventService.findByEventVenue(venue);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteByid(@PathVariable long id){
        eventService.deleteEventById(id);
        return "Deleted Sucessfull";
    }

}
