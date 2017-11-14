package flappy;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

// Utility class for writing bordered text
public class TextWriter {

    public static void writeText(Graphics2D g, int x, int y, Font font, Color inner, Color outer, String message) {
        g.setFont(font);
        g.setColor(inner);
        g.drawString(message, x, y);
        g.setColor(outer);

        g.setStroke(new BasicStroke(2f));
        AffineTransform transform = g.getTransform();
        g.translate(x, y);

        GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(),
                message);
        g.draw(gv.getOutline());

        g.setTransform(transform);
    }
}
