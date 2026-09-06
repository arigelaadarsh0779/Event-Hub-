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
public class BookingService implements BookingInterface {
    private final BookingsRepository bookingsRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final BookingMapper bookingMapper;

    public BookingResponseDto createBooking (BookingRequestDto dto){
        User user = userRepository.findById(dto.getId()).orElseThrow(()->new RuntimeException("usernotfound"));
        Event event = eventRepository.findById(dto.getEventId()).orElseThrow(()-> new RuntimeException("Event not found "));
        if (event.getAvailableSeats()<0) {
            throw new RuntimeException("no seats available ");
        }
                Bookings booking = new Bookings();
                booking.setUser(user);
                booking.setEvent(event);
                booking.setNumberOfSeats(dto.getNumberOfSeats());
                booking.setBookingdate(LocalDateTime.now());
                booking.setBookingNumber("TS 21 G"+booking.getId());
                booking.setStatus(BookingStatus.CONFRIMED);
                event.setAvailableSeats(event.getAvailableSeats() - dto.getNumberOfSeats());

                eventRepository.save(event);
                userRepository.save(user);
                Bookings booked = bookingsRepository.save(booking);

                return bookingMapper.convertObjtoResponse(booked);

        }

    @Override
    public BookingResponseDto getBookingById(long id) {
       Bookings book= bookingsRepository.findById(id).orElseThrow(()->new RuntimeException("not found bro try again later"));
       return bookingMapper.convertObjtoResponse(book);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(User user) {

        List<Bookings> bookings = bookingsRepository.findByUser(user);
     return bookings.stream()
             .map(bookingMapper::convertObjtoResponse)
             .toList();
    }

    @Override
    public List<BookingResponseDto> getBookingsByEvent(Event event) {
        List<Bookings> bookings = bookingsRepository.findByEvent(event);
        return bookings.stream().map(bookingMapper::convertObjtoResponse).toList();

    }




    @Override
    public BookingResponseDto cancelBookingById(long id) {
        Bookings bookings = bookingsRepository.findById(id).orElseThrow(()->new RuntimeException(" booking not found"));
        Event event = bookings.getEvent();
        event.setAvailableSeats(event.getAvailableSeats()+bookings.getNumberOfSeats());
        bookings.setStatus(BookingStatus.CANCELLED);
        eventRepository.save(event);
        Bookings book = bookingsRepository.save(bookings);

        return bookingMapper.convertObjtoResponse(book);
    }

    @Override
    public int checkAvailabilityByEvent(long id) {
        Event event = eventRepository.findById(id).orElseThrow(()->new RuntimeException("event not found"));

        return event.getAvailableSeats();
    }

}
