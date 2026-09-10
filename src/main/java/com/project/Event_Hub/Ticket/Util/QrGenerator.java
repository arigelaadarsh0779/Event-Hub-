package com.project.Event_Hub.Ticket.Util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
@Component
public class QrGenerator {

    public byte[] generateQrCode(String data) throws Exception {

        BitMatrix matrix = new MultiFormatWriter().encode(
                data,
                BarcodeFormat.QR_CODE,
                250,
                250
        );

        BufferedImage image = new BufferedImage(
                250,
                250,
                BufferedImage.TYPE_INT_RGB
        );

        for (int x = 0; x < 250; x++) {
            for (int y = 0; y < 250; y++) {
                image.setRGB(
                        x,
                        y,
                        matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF
                );
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ImageIO.write(image, "PNG", outputStream);

        return outputStream.toByteArray();
    }
}