package com.doomhowl.commitchart.gfx;

import com.doomhowl.commitchart.domain.GitStats;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ChartImage extends GfxUtils {
    private final BufferedImage img;
    private final int width;
    private final int height;

    private GitStats stats;
    private int year;

    public ChartImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage draw(GitStats stats, int year) {
        this.stats = stats;
        this.year = year;
        createGraphics(this.img);
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, width, height);

            Rectangle header = new Rectangle(0, 0, width, percMin(0.2f));
            drawHeader(header);
        }
        disposeGraphics();
        return img;
    }

    private void drawHeader(Rectangle region) {
        g.setColor(Color.white);

        int textSize = percMin(0.1f);
        Point textPos = rectCenter(padRect(region, 0.1f));
        drawCenteredText(Integer.toString(year), textPos, textSize);

        textPos.translate(0, textSize);
        drawCenteredText("Commits this year: " + stats.getCommitsOfYear(year).count(), textPos, textSize / 2);
    }

    private int percMin(float p) {
        return (int) (Math.min(width, height) * p);
    }
}
