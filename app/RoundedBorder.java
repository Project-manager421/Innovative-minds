package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.Border;

public class RoundedBorder implements Border {

    private final int radius;
    private final Color borderColor;

    public RoundedBorder(int radius) {
        this(radius, Color.BLACK);
    }

    public RoundedBorder(
            int radius,
            Color borderColor) {

        this.radius = radius;
        this.borderColor = borderColor;
    }

    @Override
    public Insets getBorderInsets(Component component) {
        return new Insets(
                radius + 2,
                radius + 2,
                radius + 2,
                radius + 2
        );
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(
            Component component,
            Graphics graphics,
            int x,
            int y,
            int width,
            int height) {

        Graphics2D graphics2D =
                (Graphics2D) graphics.create();

        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics2D.setColor(borderColor);

        graphics2D.drawRoundRect(
                x,
                y,
                width - 1,
                height - 1,
                radius,
                radius
        );

        graphics2D.dispose();
    }
}