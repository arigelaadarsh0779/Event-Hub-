package com.project.Event_Hub.Event.Controller;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api")
@RestController
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @GetMapping("/api/getallaevents")
    public ResponseEntity<List<ResponseEventDto>> getAllEvents() {

        return
                ResponseEntity
                        .status(200)
                        .body(eventService.viewAllEvents());
    }


    @PostMapping("/admin/addevent")
    public ResponseEntity<String> createEvent( @Valid @RequestBody RequestEventDto ev){
        eventService.createEvent(ev);
        return  ResponseEntity.status(200).body("Event Added Successfully");
    }
    @PostMapping("/api/editevent/{eventid}")
    public ResponseEntity<ResponseEventDto> editEvent(@PathVariable long  id , @RequestBody RequestEventDto ev){
        return ResponseEntity
                .status(200)
                .body(eventService.manageEventById(id,ev));
    }
    @GetMapping("/api/{title}")
    public ResponseEntity<ResponseEventDto> findByTitle(@PathVariable String title){
        return
                ResponseEntity
                        .status(200)
                        .body(eventService.findByTitle(title));
    }
    @GetMapping("/api/{theme}" )
    public ResponseEntity<List<ResponseEventDto>> findBytheme(@PathVariable String theme){
        return
                ResponseEntity
                        .status(200)
                        .body(eventService.findEventByTheme(theme));
    }
    @GetMapping("/api/{venue}")
    public ResponseEntity<List<ResponseEventDto>> findByeventVenue(@PathVariable String venue){
        return ResponseEntity
                .status(200)
                .body(eventService.findByEventVenue(venue));
    }
    @DeleteMapping("/api/admin/delete/{eventid}")
    public ResponseEntity<String> deleteByid(@PathVariable long id){
        eventService.deleteEventById(id);
        return ResponseEntity
                .status(200)
                .body("Event Deleted Sucessfull");
    }

}
