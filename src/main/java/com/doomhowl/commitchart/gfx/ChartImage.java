package com.doomhowl.commitchart.gfx;

import com.doomhowl.commitchart.domain.GitStats;
import com.doomhowl.commitchart.domain.GitStatsGroup;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChartImage extends GfxUtils {
    private static final boolean DRAW_DEBUG = false;

    private final BufferedImage mImg;
    private final int mWidth;
    private final int mHeight;

    private final String mCaption;
    private GitStats mStats;
    private int mYear;
    private long mTopDailyCommits;
    private PointBrush mBrush;
    private Color mBgColor;
    private Color mFgColor;
    private Palette mPalette;
    private Map<GitStats, Color> mColors;

    public ChartImage(int width, int height, String caption) {
        mWidth = width;
        mHeight = height;
        mImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        mBrush = new PointBrush(64);
        mCaption = caption;
        mPalette = Palette.createDefault();
        mColors = new HashMap<>();

        int e = 18;
        mBgColor = new Color(e, e, e);
        mFgColor = Color.white;
    }

    public ChartImage(int width, int height) {
        this(width, height, "");
    }

    public ChartImage bgColor(Color color) {
        mBgColor = color;
        return this;
    }

    public ChartImage darkMode(boolean on) {
        if (on) {
            mFgColor = Color.white;
        } else {
            mFgColor = Color.black;
        }
        return this;
    }

    public BufferedImage draw(GitStats stats, int year) {
        mStats = stats;
        mYear = year;
        mTopDailyCommits = stats.countTopDailyCommitsOfYear(year);

        createGraphics(this.mImg);
        {
            g.setColor(mBgColor);
            g.fillRect(0, 0, mWidth, mHeight);

            Rectangle header = new Rectangle(0, 0, mWidth, percMin(0.35f));
            drawHeader(header);

            Rectangle content = new Rectangle(0, header.y + header.height, mWidth, mHeight - header.height);
            content = columnRect(content, Math.min(mWidth, mHeight));
            drawContent(content);
        }
        disposeGraphics();
        return mImg;
    }

    private void drawHeader(Rectangle region) {

        if (DRAW_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(region.x, region.y, region.width, region.height);
            g.drawLine(region.x, region.y, region.x + region.width, region.y + region.height);
            g.drawLine(region.x, region.y + region.height, region.x + region.width, region.y);
        }

        g.setColor(mFgColor);
        int textSize = percMin(0.1f);
        Point textPos = rectCenter(region);
        textPos.translate(0, -textSize / 4);
        drawCenteredText(Integer.toString(mYear), textPos, textSize);

        textPos = rectCenter(region);
        textPos.translate(0, textSize / 2);
        drawCenteredText("Commits this year : " + mStats.getCommitsOfYear(mYear).count(), textPos, textSize / 2);

        textPos = rectCenter(region);
        textPos.translate(0, textSize);
        drawCenteredText(mCaption, textPos, textSize / 3);
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
        g.setColor(mFgColor);
        String monthName = LocalDate.of(mYear, month, 1).getMonth().name();
        monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();
        drawCenteredText(monthName, rectCenter(caption), caption.height / 2);

        region.translate(0, caption.height);
        region.height -= caption.height;

        LocalDate firstDateOfMonth = LocalDate.of(mYear, month, 1);
        int daysInMonth = firstDateOfMonth.lengthOfMonth();
        int startOffset = firstDateOfMonth.getDayOfWeek().getValue() - 1; // start offset

        int rows = daysInMonth / DAYS_PER_WEEK;
        float cellSize = Math.min(region.width / DAYS_PER_WEEK, region.height / rows);

        for (int day = 1; day <= daysInMonth; day++) {

            int x = (startOffset + day) % DAYS_PER_WEEK;
            int y = (startOffset + day) / DAYS_PER_WEEK;

            Rectangle cell = new Rectangle((int) (region.x + x * cellSize), (int) (region.y + y * cellSize), (int) cellSize, (int) cellSize);
            drawContentCell(cell, LocalDate.of(mYear, month, day));
        }
    }

    // https://easings.net/#easeOutCubic
    private static double easeOutCubic(double x) {
        return 1 - Math.pow(1 - x, 3);
    }

    private void drawContentCell(Rectangle region, LocalDate date) {
        region = padRect(region, -0.1f);
        long count = mStats.getCommitsOfDate(date).count();
        double perc = 0;
        if (mTopDailyCommits > 0) {
            perc = easeOutCubic(count / (double) mTopDailyCommits);
        }

        Color color = Color.YELLOW;
        if (mStats instanceof GitStatsGroup group) {
            Optional<GitStats> stats = group.getTopGitStatsOfDay(date);
            if (stats.isPresent()) {
                mColors.putIfAbsent(stats.get(), mPalette.nextColor());
                color = mColors.get(stats.get());
            }
        }

        BufferedImage brushImage = mBrush.getImage(color);
        float alpha = (float) Math.min(1.f, Math.max(0.f, perc));
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(comp);

        g.drawImage(mBrush.getImage(color),
                region.x, region.y, region.x + region.width, region.y + region.height,
                0, 0, brushImage.getWidth(), brushImage.getHeight(), null);
        g.setComposite(AlphaComposite.Src);
    }

    private int percMin(float p) {
        return (int) (Math.min(mWidth, mHeight) * p);
    }
}
