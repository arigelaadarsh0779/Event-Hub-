package com.project.Event_Hub.Ticket.Util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfGenerator {

    public byte[] generateTicketPdf(
            String ticketNumber,
            String bookingId,
            String userName,
            String email,
            String eventName,
            String date,
            String time,
            String venue,
            int seats,
            byte[] qrCode
    ) throws Exception {

        PDDocument document = new PDDocument();

        // Horizontal page
        PDPage page = new PDPage(
                new PDRectangle(
                        PDRectangle.A4.getHeight(),
                        PDRectangle.A4.getWidth()
                )
        );

        document.addPage(page);

        PDPageContentStream content =
                new PDPageContentStream(document, page);

        float width = page.getMediaBox().getWidth();
        float height = page.getMediaBox().getHeight();

        // Title
        content.beginText();
        content.setFont(
                new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
                24
        );
        content.newLineAtOffset(40, height - 50);
        content.showText("EVENT HUB");
        content.endText();

        // Event name
        content.beginText();
        content.setFont(
                new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
                24
        );
        content.newLineAtOffset(40, height - 90);
        content.showText(eventName);
        content.endText();

        // Ticket details
        addText(content, "Ticket No: " + ticketNumber, 40, height - 130);
        addText(content, "Booking ID: " + bookingId, 40, height - 155);
        addText(content, "Name: " + userName, 40, height - 190);
        addText(content, "Email: " + email, 40, height - 215);

        addText(content, "Date: " + date, 300, height - 130);
        addText(content, "Time: " + time, 300, height - 155);
        addText(content, "Venue: " + venue, 300, height - 190);
        addText(content, "Seats: " + seats, 300, height - 215);

        addText(content, "Payment: CONFIRMED", 40, height - 260);
        addText(content, "Ticket Status: VALID", 40, height - 285);

        // QR Code
        PDImageXObject qrImage =
                PDImageXObject.createFromByteArray(
                        document,
                        qrCode,
                        "ticket-qr"
                );

        content.drawImage(
                qrImage,
                width - 190,
                height - 250,
                150,
                150
        );

        addText(
                content,
                "Scan QR code at entrance",
                width - 190,
                height - 270
        );

        content.close();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    private void addText(
            PDPageContentStream content,
            String text,
            float x,
            float y
    ) throws Exception {

        content.beginText();

        content.setFont(
                new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                12
        );

        content.newLineAtOffset(x, y);
        content.showText(text);

        content.endText();
    }
}