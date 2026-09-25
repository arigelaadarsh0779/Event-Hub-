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
import com.project.Event_Hub.Exception.BookingsNotFoundException;
import com.project.Event_Hub.Exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.project.Event_Hub.Notification.EmailSender;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingsRepository bookingsRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final EmailSender emailSender;


    // CREATE BOOKING
    public BookingResponseDto createBooking(BookingRequestDto dto) {

        // Find User
        User user = userRepository.findById(dto.getUserId())
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


        // Calculate total amount
        BigDecimal totalAmount =
                event.getTicketPrice()
                        .multiply(BigDecimal.valueOf(dto.getNumberOfSeats()));

        booking.setTotalAmount(totalAmount);

        // For payment flow
        booking.setStatus(BookingStatus.PENDING);


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
                "TS21G"+ booked.getBookingId()
        );

        booked = bookingsRepository.save(booked);


        // Convert Entity -> DTO
        return bookingMapper.convertObjtoResponse(booked);
    }


    // GET ALL BOOKINGS
    public List<BookingResponseDto> getAllbookings() {
        List<Bookings> boo = bookingsRepository.findAll();
        if (boo != null) {
            return boo
                    .stream()
                    .map(bookingMapper::convertObjtoResponse)
                    .toList();
        }
        else throw new BookingsNotFoundException("No Bookings Found");
    }



    // GET ALL BOOKINGS BY USER ID
    public List<BookingResponseDto> getBookingsByUserId(long id) {

        // Find user
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find all bookings of that user
        List<Bookings> boo = bookingsRepository.findByUser(user);
        if (boo!=null) {
            return boo
                    .stream()
                    .map(bookingMapper::convertObjtoResponse)
                    .toList();
        }
        else throw new BookingsNotFoundException("No Bookings Found the Id");
    }


    // CANCEL BOOKING
    public BookingResponseDto cancelBookingById(long id) {

        // Find booking
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() ->
                        new BookingsNotFoundException("Booking not found"));


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
                        new EventNotFoundException("Event not found"));

        return event.getAvailableSeats();
    }

    // CONFIRM BOOKING & SEND BOOKING SUCCESS EMAIL
    public BookingResponseDto confirmBookingById(long id) {
        Bookings booking = bookingsRepository.findById(id)
                .orElseThrow(() -> new BookingsNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking is already confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Bookings confirmedBooking = bookingsRepository.save(booking);

        // Send Booking Success Email upon booking confirmation
        sendBookingSuccessEmail(confirmedBooking);

        return bookingMapper.convertObjtoResponse(confirmedBooking);
    }

    // SEND BOOKING SUCCESS EMAIL UPON BOOKING CONFIRMATION
    public void sendBookingSuccessEmail(Bookings booking) {
        if (booking == null || booking.getUser() == null || booking.getUser().getEmail() == null) {
            return;
        }

        try {
            String email = booking.getUser().getEmail();
            String username = booking.getUser().getUsername();
            String eventTitle = booking.getEvent() != null ? booking.getEvent().getTitle() : "Event";
            String venue = booking.getEvent() != null ? booking.getEvent().getVenue() : "TBA";
            String date = booking.getEvent() != null && booking.getEvent().getDate() != null ? booking.getEvent().getDate().toString() : "TBA";

            String subject = "🎉 Booking Success - " + eventTitle;
            String body = "Hello " + username + ",\n\n" +
                          "🎉 Your booking for " + eventTitle + " is CONFIRMED!\n\n" +
                          "Booking Details:\n" +
                          "• Booking ID: #" + booking.getBookingId() + "\n" +
                          "• Booking Ref: " + booking.getBookingNumber() + "\n" +
                          "• Event: " + eventTitle + "\n" +
                          "• Date: " + date + "\n" +
                          "• Venue: " + venue + "\n" +
                          "• Reserved Seats: " + booking.getNumberOfSeats() + "\n" +
                          "• Total Amount: ₹" + booking.getTotalAmount() + "\n" +
                          "• Status: CONFIRMED\n\n" +
                          "Your ticket with QR code is generated and attached to your account. You can download your PDF ticket anytime from your Event Hub account under 'My Bookings'.\n\n" +
                          "Thank you for booking with Event Hub!\n\n" +
                          "Best regards,\n" +
                          "Event Hub Team";

            emailSender.sendEmail(email, subject, body);
            System.out.println("Booking success email sent to " + email);
        } catch (Exception e) {
            System.out.println("Could not send booking success email: " + e.getMessage());
        }
    }
}