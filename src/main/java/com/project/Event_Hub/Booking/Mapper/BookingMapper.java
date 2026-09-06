package com.project.Event_Hub.Booking.Mapper;

import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Entity.Bookings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

  public BookingResponseDto convertObjtoResponse(Bookings bookings){
      BookingResponseDto dto = new BookingResponseDto();
      dto.setName(bookings.getUser().getName());
      dto.setBookingDate(bookings.getBookingdate());
      dto.setStatus(bookings.getStatus());
      dto.setEventId(bookings.getEvent().getId());
      dto.setBookingNumber(bookings.getBookingNumber());
      dto.setNumberOfSeats(bookings.getNumberOfSeats());
     return dto;
  }


}
