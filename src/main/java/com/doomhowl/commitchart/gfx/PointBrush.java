package com.doomhowl.commitchart.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PointBrush {
    private final int mResolution;
    private final Map<Color, BufferedImage> mColors;

    public PointBrush(int resolution) {
        mResolution = resolution;
        mColors = new HashMap<>();
    }

    public BufferedImage render(Color color) {
        BufferedImage image = new BufferedImage(mResolution, mResolution, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.clearRect(0, 0, mResolution, mResolution);
        g.setComposite(AlphaComposite.Src);

        int centerX = mResolution / 2;
        int centerY = mResolution / 2;
        double maxDistance = distance(0, 0, mResolution / 2, 0);

        for (int y = 0; y < mResolution; ++y) {
            for (int x = 0; x < mResolution; ++x) {
                double dis = distance(x, y, centerX, centerY);
                int alpha = (int) Math.max(Math.min(255.0 - (dis / maxDistance * 255.0), 255), 0.0);
                g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                g.fillRect(x, y, 1, 1);
            }
        }

        g.dispose();
        return image;
    }

    public BufferedImage getImage(Color color) {
        if (!mColors.containsKey(color)) {
            BufferedImage img = render(color);
            mColors.put(color, img);
            return img;
        } else {
            return mColors.get(color);
        }
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
