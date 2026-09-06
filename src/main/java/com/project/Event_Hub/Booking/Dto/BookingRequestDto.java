package com.project.Event_Hub.Booking.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {


    private Long eventId;

    private long id;

    private int numberOfSeats;

}
