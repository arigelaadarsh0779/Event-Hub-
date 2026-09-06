package com.project.Event_Hub.Event.Mapper;

import com.project.Event_Hub.Event.Dto.RequestEventDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

        // EVENT --> ENTITY

    public Event dtoToObj(RequestEventDto dto){
        Event event = new Event();

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setVenue(dto.getVenue());
        event.setThemeOfTheProject(dto.getThemeOfTheProject());
        event.setOrganizer(dto.getOrganizer());
        event.setTotalSeats(dto.getTotalSeats());
        event.setAvailableSeats(dto.getRemainingSeats());
        return event;

    }
    //ENTITY --> RESPONSE DTO

    public ResponseEventDto objToRespose(Event event){
        ResponseEventDto dto = new ResponseEventDto();
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setDate(event.getDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setVenue(event.getVenue());
        dto.setThemeOfTheProject(event.getThemeOfTheProject());
        dto.setOrganizer(event.getOrganizer());
        dto.setTotalSeats(event.getTotalSeats());
        dto.setRemainingSeats(event.getAvailableSeats());

        return dto;
    }



}
