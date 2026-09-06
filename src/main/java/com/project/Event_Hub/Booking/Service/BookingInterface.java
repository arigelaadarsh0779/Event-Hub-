package com.project.Event_Hub.Booking.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Event.Dto.ResponseEventDto;
import com.project.Event_Hub.Event.Entity.Event;

import java.util.List;

public interface BookingInterface {
   BookingResponseDto createBooking(BookingRequestDto dto);
   BookingResponseDto getBookingById(long id) ;
   List<BookingResponseDto>getBookingsByUser(User user);
    List<BookingResponseDto>getBookingsByEvent(Event event);
    BookingResponseDto cancelBookingById(long id);
    int checkAvailabilityByEvent(long id);
}
