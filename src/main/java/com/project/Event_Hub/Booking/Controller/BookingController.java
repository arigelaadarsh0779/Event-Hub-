package com.project.Event_Hub.Booking.Controller;

import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;


    // GET ALL BOOKINGS
    @GetMapping("/api/admin/allbookings")
    public ResponseEntity<List<BookingResponseDto>>getAll()
    {
        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.getAllbookings());
    }


    // CREATE BOOKING
    @PostMapping("/api/user/createbooking")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto dto) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.createBooking(dto));
    }


    // GET ALL BOOKINGS BY USER ID
    @GetMapping("/api/user/{bookingId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByUserId(
            @PathVariable long bookingId) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.getBookingsByUserId(bookingId));
    }


    // CANCEL BOOKING BY BOOKING ID
    @PutMapping("/api/user/cancel/{bookingId}")
    public ResponseEntity<BookingResponseDto> cancelBookingById(
            @PathVariable long bookingId) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.cancelBookingById(bookingId));
    }


    // CHECK AVAILABLE SEATS BY EVENT ID
    @GetMapping("/api/user/availability/{bookingId}")
    public ResponseEntity<Integer> checkAvailabilityByEvent(
            @PathVariable long bookingId) {

        return
                ResponseEntity
                        .status(200)
                        .body(bookingService.checkAvailabilityByEvent(bookingId));
    }
}