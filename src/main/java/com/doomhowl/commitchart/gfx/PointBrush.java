package com.doomhowl.commitchart.gfx;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PointBrush {
    private BufferedImage mImage;

    public PointBrush(int resolution, Color color) {
        mImage = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = mImage.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.clearRect(0, 0, resolution, resolution);
        g.setComposite(AlphaComposite.Src);

        int centerX = resolution / 2;
        int centerY = resolution / 2;
        double maxDistance = distance(0, 0, resolution / 2, 0);

        for (int y = 0; y < resolution; ++y) {
            for (int x = 0; x < resolution; ++x) {
                double dis = distance(x, y, centerX, centerY);
                int alpha = (int) Math.max(Math.min(255.0 - (dis / maxDistance * 255.0), 255), 0.0);
                g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                g.fillRect(x, y, 1, 1);
            }
        }

        g.dispose();
    }

    public BufferedImage getImage() {
        return mImage;
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
