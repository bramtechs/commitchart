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

    public static Palette createDefault() {
        Palette pal = new Palette();
        pal.add(Color.YELLOW);
        pal.add(Color.ORANGE);
        pal.add(Color.GREEN);
        pal.add(Color.cyan);
        pal.add(Color.white);
        pal.add(Color.pink);
        return pal;
    }
}
