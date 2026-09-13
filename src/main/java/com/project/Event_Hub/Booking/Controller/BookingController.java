package com.project.Event_Hub.Booking.Controller;

import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    // GET ALL BOOKINGS
    @GetMapping
    public ResponseEntity<List<BookingResponseDto>>getAll()
    {
        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.getAllbookings());
    }


    // CREATE BOOKING
    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto dto) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.createBooking(dto));
    }


    // GET ALL BOOKINGS BY USER ID
    @GetMapping("/user/{bookingId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUserId(
            @PathVariable long id) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.getBookingsByUserId(id));
    }


    // CANCEL BOOKING BY BOOKING ID
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<BookingResponseDto> cancelBookingById(
            @PathVariable long id) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.cancelBookingById(id));
    }


    // CHECK AVAILABLE SEATS BY EVENT ID
    @GetMapping("/availability/{bookingId}")
    public ResponseEntity<Integer> checkAvailabilityByEvent(
            @PathVariable long id) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.checkAvailabilityByEvent(id));
    }
}