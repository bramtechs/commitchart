package com.doomhowl.commitchart.gfx;

import com.doomhowl.commitchart.domain.GitStats;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

public class ChartImage extends GfxUtils {
    private static final boolean DRAW_DEBUG = false;

    private final BufferedImage img;
    private final int width;
    private final int height;

    private GitStats stats;
    private int year;
    private long topDailyCommits;

    public ChartImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage draw(GitStats stats, int year) {
        this.stats = stats;
        this.year = year;
        this.topDailyCommits = stats.countTopDailyCommitsOfYear(year);

        createGraphics(this.img);
        {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, width, height);

            Rectangle header = new Rectangle(0, 0, width, percMin(0.2f));
            drawHeader(header);

            Rectangle content = new Rectangle(0, header.y + header.height, width, height - header.height);
            content = columnRect(content, Math.min(width, height));
            drawContent(content);
        }
        disposeGraphics();
        return img;
    }

    private void drawHeader(Rectangle region) {

        if (DRAW_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(region.x, region.y, region.width, region.height);
            g.drawLine(region.x, region.y, region.x + region.width, region.y + region.height);
            g.drawLine(region.x, region.y + region.height, region.x + region.width, region.y);
        }

        g.setColor(Color.white);
        int textSize = percMin(0.1f);
        Point textPos = rectCenter(region);
        textPos.translate(0, -textSize / 4);
        drawCenteredText(Integer.toString(year), textPos, textSize);

        textPos = rectCenter(region);
        textPos.translate(0, textSize / 2);
        drawCenteredText("Commits this year: " + stats.getCommitsOfYear(year).count(), textPos, textSize / 2);
    }

    private void drawContent(Rectangle region) {

        if (DRAW_DEBUG) {
            g.setColor(Color.GREEN);
            g.drawRect(region.x, region.y, region.width, region.height);
            g.drawLine(region.x, region.y, region.x + region.width, region.y + region.height);
            g.drawLine(region.x, region.y + region.height, region.x + region.width, region.y);
        }

        int monthsPerCol = 5;
        int monthW = region.width / monthsPerCol;

        int rows = 12 / monthsPerCol;
        int monthH = Math.min(region.height / rows, monthW);

        for (int m = 0; m < 12; m++) {
            int x = m % monthsPerCol * monthW;
            int y = m / monthsPerCol * monthH;

            Rectangle col = new Rectangle(region.x + x, region.y + y, monthW, monthH);
            drawContentMonth(m + 1, col);
        }
    }

    private void drawContentMonth(int month, Rectangle region) {
        final int DAYS_PER_WEEK = 7;

        if (DRAW_DEBUG) {
            g.setColor(Color.magenta);
            g.drawRect(region.x, region.y, region.width, region.height);
        }

        region = padRect(region, 0.1f);

        if (DRAW_DEBUG) {
            g.setColor(Color.pink);
            g.drawRect(region.x, region.y, region.width, region.height);
        }

        // header caption
        Rectangle caption = new Rectangle(region.x, region.y, region.width, region.height / 3);
        g.setColor(Color.white);
        drawCenteredText(LocalDate.of(year, month, 1).getMonth().name(), rectCenter(caption), caption.height / 2);

        region.translate(0, caption.height);
        region.height -= caption.height;

        LocalDate firstDateOfMonth = LocalDate.of(year, month, 1);
        int daysInMonth = firstDateOfMonth.lengthOfMonth();
        int startOffset = firstDateOfMonth.getDayOfWeek().getValue() - 1; // start offset

        int rows = daysInMonth / DAYS_PER_WEEK;
        float cellSize = Math.min(region.width / DAYS_PER_WEEK, region.height / rows);

        for (int day = 1; day <= daysInMonth; day++) {

            int x = (startOffset + day) % DAYS_PER_WEEK;
            int y = (startOffset + day) / DAYS_PER_WEEK;

            Rectangle cell = new Rectangle((int) (region.x + x * cellSize), (int) (region.y + y * cellSize),
                    (int) cellSize, (int) cellSize);
            drawContentCell(cell, LocalDate.of(year, month, day));
        }
    }

    private void drawContentCell(Rectangle region, LocalDate date) {
        region = padRect(region, 0.1f);
        long count = stats.getCommitsOfDate(date).count();
        float perc = (float)count / (float) topDailyCommits;

        g.setColor(Color.WHITE);
        g.clearRect(region.x, region.y, region.width, region.height);

        g.setColor(Color.getHSBColor(48, perc * 100.f, 52));
        g.fillRect(region.x, region.y, region.width, region.height);
    }

    private int percMin(float p) {
        return (int) (Math.min(width, height) * p);
    }
}
