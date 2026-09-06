package com.project.Event_Hub.Booking.Dto;

import com.project.Event_Hub.Booking.Entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class BookingResponseDto {

    private Long id;

    private String name;
    private Long eventId;

    private int numberOfSeats;

    private String BookingNumber;

    private LocalDateTime bookingDate;

    private BookingStatus status;
}
