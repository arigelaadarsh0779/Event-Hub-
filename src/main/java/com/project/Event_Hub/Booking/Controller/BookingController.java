package com.project.Event_Hub.Booking.Controller;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Service.BookingService;
import com.project.Event_Hub.Event.Entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(
            @RequestBody BookingRequestDto dto) {

        return bookingService.createBooking(dto);
    }

    @GetMapping("/{id}")
    public BookingResponseDto getBookingById(
            @PathVariable long id) {

        return bookingService.getBookingById(id);
    }

    @GetMapping("/user")
    public List<BookingResponseDto> getBookingsByUser(
            @RequestBody User user) {

        return bookingService.getBookingsByUser(user);
    }

    @GetMapping("/event")
    public List<BookingResponseDto> getBookingsByEvent(
            @RequestBody Event event) {

        return bookingService.getBookingsByEvent(event);
    }

    @PutMapping("/{id}/cancel")
    public BookingResponseDto cancelBookingById(
            @PathVariable long id) {

        return bookingService.cancelBookingById(id);
    }

    @GetMapping("/availability/{id}")
    public int checkAvailabilityByEvent(
            @PathVariable long id) {

        return bookingService.checkAvailabilityByEvent(id);
    }
}