package com.project.Event_Hub.Ticket.Controller;

import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Ticket.DTO.TicketResponseDto;
import com.project.Event_Hub.Ticket.Entity.TicketEntity;
import com.project.Event_Hub.Ticket.Repository.TicketRepository;
import com.project.Event_Hub.Ticket.TicketMapper.TicketMapper;
import com.project.Event_Hub.Ticket.Util.PdfGenerator;
import com.project.Event_Hub.Ticket.Util.QrGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final BookingsRepository bookingsRepository;
    private final TicketRepository ticketRepository;
    private final PdfGenerator pdfGenerator;
    private final QrGenerator qrGenerator;
    private final TicketMapper ticketMapper;

    // GET /api/user/ticket/{bookingId} - Get ticket by booking ID (used by MyTickets page)
    @GetMapping("/api/user/ticket/{bookingId}")
    public ResponseEntity<TicketResponseDto> getTicketByBookingId(@PathVariable Long bookingId) {
        TicketEntity ticket = ticketRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Ticket not found for booking: " + bookingId));
        return ResponseEntity.ok(ticketMapper.toResponseDto(ticket));
    }

    // GET /api/user/mytickets/{userId} - Get all tickets for a user
    @GetMapping("/api/user/mytickets/{userId}")
    public ResponseEntity<List<TicketResponseDto>> getTicketsByUserId(@PathVariable Long userId) {
        List<TicketEntity> tickets = ticketRepository.findByBooking_User_Userid(userId);
        List<TicketResponseDto> dtos = tickets.stream()
                .map(ticketMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/tickets/pdf/{bookingId} - Download ticket PDF
    @GetMapping("/api/tickets/pdf/{bookingId}")
    public ResponseEntity<byte[]> getTicketPdf(@PathVariable Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        TicketEntity ticket = ticketRepository.findByBooking_BookingId(bookingId).orElse(null);

        String ticketNumber = ticket != null ? ticket.getTicketNumber() : "TKT-" + bookingId;

        try {
            byte[] qrCode = qrGenerator.generateQrCode(ticketNumber);
            byte[] pdfBytes = pdfGenerator.generateTicketPdf(
                    ticketNumber,
                    String.valueOf(bookingId),
                    booking.getUser() != null ? booking.getUser().getUsername() : "Guest",
                    booking.getUser() != null ? booking.getUser().getEmail() : "N/A",
                    booking.getEvent() != null ? booking.getEvent().getTitle() : "Event",
                    booking.getEvent() != null ? booking.getEvent().getThemeOfTheProject() : "General",
                    booking.getEvent() != null && booking.getEvent().getDate() != null ? booking.getEvent().getDate().toString() : "TBA",
                    booking.getEvent() != null ? (booking.getEvent().getStartTime() + " - " + booking.getEvent().getEndTime()) : "TBA",
                    booking.getEvent() != null ? booking.getEvent().getVenue() : "TBA",
                    booking.getNumberOfSeats(),
                    qrCode
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Ticket_" + ticketNumber + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ticket PDF: " + e.getMessage(), e);
        }
    }
}
