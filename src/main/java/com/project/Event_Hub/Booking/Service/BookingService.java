package com.project.Event_Hub.Booking.Service;

import com.project.Event_Hub.Auth.Entity.User;
import com.project.Event_Hub.Auth.Repository.UserRepository;
import com.project.Event_Hub.Booking.Dto.BookingRequestDto;
import com.project.Event_Hub.Booking.Dto.BookingResponseDto;
import com.project.Event_Hub.Booking.Entity.BookingStatus;
import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Mapper.BookingMapper;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Event.Entity.Event;
import com.project.Event_Hub.Event.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingsRepository bookingsRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;


    // CREATE BOOKING
    public BookingResponseDto createBooking(BookingRequestDto dto) {

        // Find User
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Find Event
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));


        // Check number of seats
        if (dto.getNumberOfSeats() <= 0) {
            throw new RuntimeException(
                    "Number of seats must be greater than 0");
        }


        // Check availability
        if (event.getAvailableSeats() < dto.getNumberOfSeats()) {
            throw new RuntimeException(
                    "Not enough seats available");
        }


        // Create booking
        Bookings booking = new Bookings();

        booking.setUser(user);
        booking.setEvent(event);
        booking.setNumberOfSeats(dto.getNumberOfSeats());
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);


        // Reduce available seats
        event.setAvailableSeats(
                event.getAvailableSeats()
                        - dto.getNumberOfSeats()
        );

        eventRepository.save(event);


        // Save booking
        Bookings booked = bookingsRepository.save(booking);


        // Generate booking number
        booked.setBookingNumber(
                "TS21G" + booked.getId()
        );

        booked = bookingsRepository.save(booked);


        // Convert Entity -> DTO
        return bookingMapper.convertObjtoResponse(booked);
    }


    // GET ALL BOOKINGS
    public List<BookingResponseDto> getAllbookings() {

        return bookingsRepository.findAll()
                .stream()
                .map(bookingMapper::convertObjtoResponse)
                .toList();
    }


    // GET ALL BOOKINGS BY USER ID
    public List<BookingResponseDto> getBookingsByUserId(long id) {

        // Find user
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Find all bookings of that user
        return bookingsRepository.findByUser(user)
                .stream()
                .map(bookingMapper::convertObjtoResponse)
                .toList();
    }


    // CANCEL BOOKING
    public BookingResponseDto cancelBookingById(long id) {

        // Find booking
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));


        // Check already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException(
                    "Booking already cancelled");
        }


        // Get event
        Event event = booking.getEvent();


        // Return seats to event
        event.setAvailableSeats(
                event.getAvailableSeats()
                        + booking.getNumberOfSeats()
        );

        eventRepository.save(event);


        // Change status
        booking.setStatus(BookingStatus.CANCELLED);

        Bookings cancelledBooking =
                bookingsRepository.save(booking);


        // Convert Entity -> DTO
        return bookingMapper.convertObjtoResponse(
                cancelledBooking
        );
    }


    // CHECK EVENT AVAILABILITY
    public int checkAvailabilityByEvent(long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Event not found"));

        return event.getAvailableSeats();
    }
}