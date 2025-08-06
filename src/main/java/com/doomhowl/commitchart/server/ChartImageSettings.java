package com.doomhowl.commitchart.server;

import java.awt.*;

public record ChartImageSettings(
        int width,
        int height,
        String caption,
        String repoDir,
        Color bgColor,
        boolean darkMode,
        int year,
        int generatedOnMonth,
        int generatedOnDay
) {
}
