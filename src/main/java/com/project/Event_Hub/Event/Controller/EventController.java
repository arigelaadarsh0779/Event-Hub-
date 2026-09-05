package com.project.Event_Hub.Event.Controller;

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

        return eventService.viewAllEvents()
                .stream()
                .map(event -> new ResponseEventDto(
                        event.getTitle(),
                        event.getDescription(),
                        event.getDate(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getVenue(),
                        event.getThemeOfTheProject(),
                        event.getOrganizer(),
                        event.getCapacity()
                ))
                .toList();
    }


}
