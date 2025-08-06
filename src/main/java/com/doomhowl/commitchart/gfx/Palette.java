package com.doomhowl.commitchart.gfx;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;

public class Palette {

    private final Queue<Color> mColors;

    public Palette() {
        mColors = new LinkedList<>();
    }

    public void add(Color color) {
        mColors.add(color);
    }

    public Color nextColor() {
        Color color =  mColors.remove();
        mColors.add(color);
        return color;
    }

    public static Palette createDefault(boolean dark) {
        Palette pal = new Palette();
        if (dark) {
            // Bright, high-contrast on black background
            pal.add(new Color(255, 255, 0));     // Yellow
            pal.add(new Color(255, 165, 0));     // Orange
            pal.add(new Color(0, 255, 255));     // Cyan
            pal.add(new Color(255, 255, 255));   // White
            pal.add(new Color(255, 105, 180));   // Hot Pink
            pal.add(new Color(144, 238, 144));   // Light Green
        } else {
            // Dark, high-contrast on white background
            pal.add(new Color(139, 0, 0));       // Dark Red
            pal.add(new Color(0, 100, 0));       // Dark Green
            pal.add(new Color(0, 0, 139));       // Dark Blue
            pal.add(new Color(85, 26, 139));     // Dark Violet
            pal.add(new Color(47, 79, 79));      // Dark Slate Gray
            pal.add(new Color(112, 128, 144));   // Slate Gray
        }
        return pal;
    }

}
