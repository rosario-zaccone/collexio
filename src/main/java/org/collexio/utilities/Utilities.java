package org.collexio.utilities;

import org.collexio.domain.ItemPhoto;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Utilities {
    public static boolean validateId(String prefix, String id) {
        //validate id in form prefix-number
        if (!id.startsWith(prefix))
            return false;
        String numId = id.substring(prefix.length());
        char[] numbers = numId.toCharArray();
        if (numbers.length == 0 || numbers[0] == 0)
            return false;
        for (char c: numbers) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }


    public static void uploadPhoto(ItemPhoto photo) throws IOException { // da spostare in service in caso di refactor
        String fileName = photo.getPath().getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IOException("File extension not found");
        }
        String ext = fileName.substring(dotIndex + 1).toLowerCase();
        Path outputPath = Paths.get("images/thumbnails/" + photo.getId() + "." + ext);
        if (Files.exists(outputPath)) {
            return;
        }
        try (InputStream is = Files.newInputStream(photo.getPath())) {
            BufferedImage originalImage = ImageIO.read(is);
            BufferedImage resizedImage = new BufferedImage(100, 100, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 100, 100, null);
            g.dispose();
            ImageIO.write(resizedImage, ext, outputPath.toFile());
        }
    }
}
