// 
// File:     ASCII Art Class
//
// Course:   DAT4001 
// Date:     Autumn 2023
// Group:    
//           Ross Grant
//           Sam Loftus
//           Tom Rowan
//
// Author:   EugeneP, via https://github.com/eugenp 
//

package carparkmanager;

//
// We do not claim credit for the work in this file.
// This is code used by us to draw the simulated ANPR images.
// This class is used *almost* verbatim from the URL below with a small change to make the Settings subclass available.
// https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-console/src/main/java/com/baeldung/asciiart/AsciiArt.java
//

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class AsciiArt {

    public AsciiArt() {
    }

    public void drawString(String text, String artChar, Settings settings) {

        BufferedImage image = getImageIntegerMode(settings.width, settings.height);

        Graphics2D graphics2D = getGraphics2D(image.getGraphics(), settings);
        graphics2D.drawString(text, 6, ((int) (settings.height * 0.67)));

        for (int y = 0; y < settings.height; y++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int x = 0; x < settings.width; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? " " : artChar);
            }

            if (stringBuilder.toString()
                .trim()
                .isEmpty()) {
                continue;
            }

            System.out.println(stringBuilder);
        }

    }

    private BufferedImage getImageIntegerMode(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private Graphics2D getGraphics2D(Graphics graphics, Settings settings) {
        graphics.setFont(settings.font);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        return graphics2D;
    }

    public static class Settings {
        public Font font;
        public int width;
        public int height;

        public Settings(Font font, int width, int height) {
            this.font = font;
            this.width = width;
            this.height = height;
        }
    }
}

//
// End of File: ASCII Art Class
//