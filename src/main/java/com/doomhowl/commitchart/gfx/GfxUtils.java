package com.doomhowl.commitchart.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class GfxUtils {
    protected Graphics2D g;

    public static Rectangle padRect(Rectangle r, float p) {
        float pad = Math.min(r.width, r.height) * p;
        return new Rectangle(
                (int) (r.x - pad * 0.5f),
                (int) (r.y - pad * 0.5f),
                (int) (r.width - pad), (int) (r.height - pad)
        );
    }

    public static Point rectCenter(Rectangle r) {
        return new Point((int) r.getCenterX(), (int) r.getCenterY());
    }

    public void createGraphics(BufferedImage img) {
        g = img.createGraphics();
    }

    public void disposeGraphics() {
        Objects.requireNonNull(g);
        g.dispose();
    }

    public void drawCenteredText(String text, Point center, int size) {
        drawCenteredText(text, center, size, "ComicMono.ttf");
    }

    public void drawCenteredText(String text, Point center, int size, String fontAssetName) {
        Font font = FontLoader.loadFontFromJar(fontAssetName, size);
        FontLoader.configureGraphics(g);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);
        int x = center.x - metrics.stringWidth(text) / 2;
        int y = center.y - metrics.getHeight() / 2 + metrics.getAscent();
        g.drawString(text, x, y);
    }
}
