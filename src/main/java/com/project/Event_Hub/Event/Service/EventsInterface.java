package com.project.Event_Hub.Event.Service;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EventsInterface {
    String createEvent(RequestEventDto dto);
    List<ResponseEventDto> viewAllEvents();
    ResponseEventDto manageEventById( long id , RequestEventDto dto);
    List<ResponseEventDto> findByTitle(String Title);
    List<ResponseEventDto> findEventByTheme(String ThemeOfTheProject);
    List<ResponseEventDto> findByEventVenue(String Venue);
    String deleteEventById(Long id);

}
