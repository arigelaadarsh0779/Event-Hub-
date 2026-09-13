package com.project.Event_Hub.Ticket.Service;

import com.project.Event_Hub.Booking.Entity.Bookings;
import com.project.Event_Hub.Booking.Repository.BookingsRepository;
import com.project.Event_Hub.Notification.EmailSender;
import com.project.Event_Hub.Ticket.DTO.TicketRequestDto;
import com.project.Event_Hub.Ticket.DTO.TicketResponseDto;
import com.project.Event_Hub.Ticket.Entity.TicketEntity;
import com.project.Event_Hub.Ticket.Entity.TicketEnum;
import com.project.Event_Hub.Ticket.Repository.TicketRepository;
import com.project.Event_Hub.Ticket.TicketMapper.TicketMapper;
import com.project.Event_Hub.Ticket.Util.PdfGenerator;
import com.project.Event_Hub.Ticket.Util.QrGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService implements TicketInterface {

    private final TicketRepository ticketRepository;
    private final BookingsRepository bookingsRepository;
    private final TicketMapper ticketMapper;
    private final QrGenerator qrGenerator;
    private final PdfGenerator pdfGenerator;
    private final EmailSender emailSender;

    @Override
    public TicketResponseDto genereateTicket(TicketRequestDto dto) {

        // 1. Find booking
        Bookings booking = bookingsRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 2. Generate ticket number
        String ticketNumber = "TKT-" + System.currentTimeMillis();

        // 3. Create ticket
        TicketEntity ticket = new TicketEntity();

        ticket.setTicketNumber(ticketNumber);
        ticket.setBooking(booking);
        ticket.setGeneratedAt(LocalDateTime.now());
        ticket.setStatus(TicketEnum.VALID);

        // 4. Generate QR code
        byte[] qrCode;

        try {

            String qrData = ticketNumber;

            qrCode = qrGenerator.generateQrCode(qrData);

            ticket.setQrCode(ticketNumber);

        } catch (Exception e) {
            throw new RuntimeException("QR code generation failed", e);
        }

        // 5. Get user details
        String userName = booking.getUser().getUsername();
        String email = booking.getUser().getEmail();

        // 6. Get event details
        String eventName = booking.getEvent().getTitle();
        String date = booking.getEvent().getDate().toString();

        String time = booking.getEvent().getStartTime()
                + " - "
                + booking.getEvent().getEndTime();

        String venue = booking.getEvent().getVenue();


        // 7. Get booking details
        int seats = booking.getNumberOfSeats();
        String bookingId = String.valueOf(booking.getBookingId());

        // 8. Generate PDF ticket
        byte[] pdf;

        try {

            pdf = pdfGenerator.generateTicketPdf(
                    ticketNumber,
                    bookingId,
                    userName,
                    email,
                    eventName,
                    date,
                    time,
                    venue,
                    seats,
                    qrCode
            );

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }

        // 9. Save ticket
        TicketEntity savedTicket = ticketRepository.save(ticket);

        // 10. Send ticket PDF to user's email
        try {

            emailSender.sendTicketEmail(
                    email,
                    "Your Event Hub Ticket",
                    "Your ticket has been generated successfully. Please find your ticket attached.",
                    pdf
            );

            System.out.println("Ticket email sent successfully");

        } catch (Exception e) {

            System.out.println(
                    "Ticket generated successfully, but email sending failed: "
                            + e.getMessage()
            );
        }

        // 11. Return response
        return ticketMapper.toResponseDto(savedTicket);
    }
}