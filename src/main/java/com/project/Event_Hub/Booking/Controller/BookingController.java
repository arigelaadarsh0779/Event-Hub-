package com.project.Event_Hub.Booking.Controller;

import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    // GET ALL BOOKINGS
    @GetMapping
    public List<BookingResponseDto> getAll() {
        return bookingService.getAllbookings();
    }


    // CREATE BOOKING
    @PostMapping("/createBooking")
    public BookingResponseDto createBooking(
            @RequestBody BookingRequestDto dto) {

        return bookingService.createBooking(dto);
    }


    // GET ALL BOOKINGS BY USER ID
    @GetMapping("/user/{bookingid}")
    public List<BookingResponseDto> getBookingsByUserId(
            @PathVariable long id) {

        return bookingService.getBookingsByUserId(id);
    }


    // CANCEL BOOKING BY BOOKING ID
    @PutMapping("/{bookingid}/cancel")
    public BookingResponseDto cancelBookingById(
            @PathVariable long id) {

        return bookingService.cancelBookingById(id);
    }


    // CHECK AVAILABLE SEATS BY EVENT ID
    @GetMapping("/availability/{bookingid}")
    public int checkAvailabilityByEvent(
            @PathVariable long id) {

        return bookingService.checkAvailabilityByEvent(id);
    }
}