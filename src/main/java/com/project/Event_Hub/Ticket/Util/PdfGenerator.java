package com.project.Event_Hub.Ticket.Util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.Color;
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
        return generateTicketPdf(ticketNumber, bookingId, userName, email, eventName, "General", date, time, venue, seats, qrCode);
    }

    public byte[] generateTicketPdf(
            String ticketNumber,
            String bookingId,
            String userName,
            String email,
            String eventName,
            String theme,
            String date,
            String time,
            String venue,
            int seats,
            byte[] qrCode
    ) throws Exception {

        PDDocument document = new PDDocument();


        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        float width = page.getMediaBox().getWidth();
        float height = page.getMediaBox().getHeight();

        // Determine colors based on event theme
        Color primaryColor = getThemePrimaryColor(theme);
        Color accentBgColor = getThemeLightColor(theme);

        // 1. Draw Page Outer Border
        content.setStrokingColor(primaryColor);
        content.setLineWidth(2f);
        content.addRect(20, 20, width - 40, height - 40);
        content.stroke();

        // 2. Draw Top Colored Header Banner
        content.setNonStrokingColor(primaryColor);
        content.addRect(22, height - 90, width - 44, 68);
        content.fill();

        // Header Title: "EVENT HUB"
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 22);
        content.setNonStrokingColor(Color.WHITE);
        content.newLineAtOffset(40, height - 55);
        content.showText("EVENT HUB");
        content.endText();

        // Header Theme Badge: e.g. "MUSIC EVENT PASS" / "TECH CONFERENCE TICKET"
        String themeBadgeText = (theme != null ? theme.toUpperCase() : "GENERAL") + " OFFICIAL TICKET";
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        content.setNonStrokingColor(Color.WHITE);
        content.newLineAtOffset(width - 260, height - 55);
        content.showText(themeBadgeText);
        content.endText();

        // 3. Event Name (Large Bold Title)
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
        content.setNonStrokingColor(new Color(15, 23, 42)); // Dark Slate
        content.newLineAtOffset(40, height - 130);
        content.showText(sanitizeText(eventName));
        content.endText();

        // Divider Line below Event Name
        content.setStrokingColor(new Color(226, 232, 240));
        content.setLineWidth(1f);
        content.moveTo(40, height - 145);
        content.lineTo(width - 40, height - 145);
        content.stroke();

        // 4. Ticket Info Box (Left Side)
        addLabelValue(content, "TICKET NUMBER", ticketNumber, 40, height - 180);
        addLabelValue(content, "BOOKING ID", "#" + bookingId, 40, height - 220);
        addLabelValue(content, "ATTENDEE NAME", userName, 40, height - 260);
        addLabelValue(content, "REGISTERED EMAIL", email, 40, height - 300);

        // 5. Event Info Box (Middle Side)
        addLabelValue(content, "EVENT DATE", date, 290, height - 180);
        addLabelValue(content, "EVENT TIME", time, 290, height - 220);
        addLabelValue(content, "VENUE LOCATION", venue, 290, height - 260);
        addLabelValue(content, "RESERVED SEATS", seats + " Seat(s)", 290, height - 300);

        // 6. Payment & Ticket Status Badges
        content.setNonStrokingColor(accentBgColor);
        content.addRect(40, height - 365, 200, 35);
        content.fill();

        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.setNonStrokingColor(primaryColor);
        content.newLineAtOffset(52, height - 343);
        content.showText("STATUS: CONFIRMED & VALID");
        content.endText();

        // 7. QR Code Panel (Right Side)
        content.setNonStrokingColor(new Color(248, 250, 252));
        content.addRect(width - 230, height - 365, 190, 205);
        content.fill();

        content.setStrokingColor(new Color(203, 213, 225));
        content.setLineWidth(1f);
        content.addRect(width - 230, height - 365, 190, 205);
        content.stroke();

        // Draw QR Code Image
        PDImageXObject qrImage = PDImageXObject.createFromByteArray(document, qrCode, "ticket-qr");
        content.drawImage(qrImage, width - 205, height - 325, 140, 140);

        // QR instructions text below QR
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
        content.setNonStrokingColor(new Color(71, 85, 105));
        content.newLineAtOffset(width - 205, height - 345);
        content.showText("SCAN FOR ENTRY CHECK-IN");
        content.endText();

        // 8. Bottom Footer Bar
        content.setStrokingColor(new Color(226, 232, 240));
        content.setLineWidth(1f);
        content.moveTo(40, 45);
        content.lineTo(width - 40, 45);
        content.stroke();

        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.setNonStrokingColor(new Color(100, 116, 139));
        content.newLineAtOffset(40, 32);
        content.showText("This is an official digital ticket generated by Event Hub. Present this PDF or QR code at the event entrance.");
        content.endText();

        content.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    private void addLabelValue(PDPageContentStream content, String label, String value, float x, float y) throws Exception {
        // Label
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 8);
        content.setNonStrokingColor(new Color(100, 116, 139)); // Muted slate
        content.newLineAtOffset(x, y + 12);
        content.showText(label);
        content.endText();

        // Value
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        content.setNonStrokingColor(new Color(15, 23, 42)); // Dark Slate
        content.newLineAtOffset(x, y);
        content.showText(sanitizeText(value));
        content.endText();
    }

    private String sanitizeText(String text) {
        if (text == null) return "";
        return text.replaceAll("[^\\x00-\\x7F]", ""); // Filter non-ASCII characters for standard Helvetica
    }

    private Color getThemePrimaryColor(String theme) {
        if (theme == null) return new Color(37, 99, 235); // Royal Blue
        String t = theme.toLowerCase();
        if (t.contains("music") || t.contains("concert") || t.contains("dance")) {
            return new Color(67, 56, 202); // Deep Royal Indigo
        } else if (t.contains("tech") || t.contains("hack") || t.contains("code") || t.contains("software")) {
            return new Color(30, 58, 138); // Deep Navy Blue
        } else if (t.contains("sport") || t.contains("fit") || t.contains("game")) {
            return new Color(4, 120, 87); // Forest Emerald
        } else if (t.contains("business") || t.contains("corp") || t.contains("conf") || t.contains("finance")) {
            return new Color(30, 64, 175); // Sapphire Blue
        } else if (t.contains("art") || t.contains("design") || t.contains("fashion")) {
            return new Color(190, 24, 93); // Carmine Rose
        } else if (t.contains("food") || t.contains("cullinary") || t.contains("drink")) {
            return new Color(194, 65, 12); // Warm Amber
        }
        return new Color(37, 99, 235); // Classic Royal Blue default
    }

    private Color getThemeLightColor(String theme) {
        if (theme == null) return new Color(239, 246, 255); // Ice Blue
        String t = theme.toLowerCase();
        if (t.contains("music") || t.contains("concert") || t.contains("dance")) {
            return new Color(238, 242, 255); // Light Indigo
        } else if (t.contains("tech") || t.contains("hack") || t.contains("code")) {
            return new Color(240, 249, 255); // Light Cyan Blue
        } else if (t.contains("sport") || t.contains("fit")) {
            return new Color(236, 253, 245); // Light Mint
        } else if (t.contains("art") || t.contains("design")) {
            return new Color(253, 242, 248); // Light Rose
        } else if (t.contains("food")) {
            return new Color(255, 247, 237); // Light Amber
        }
        return new Color(239, 246, 255); // Ice Blue
    }
}