package com.doomhowl.commitchart;

import com.doomhowl.commitchart.domain.RepositoryGroup;
import com.doomhowl.commitchart.gfx.ChartImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CommitChartCli {
    // TODO: not implemented yet
    public static void main(String[] args) throws IOException {
        RepositoryGroup group = new RepositoryGroup("C:\\dev\\_dump");
        group.open();

        ChartImage lightChartImage = new ChartImage(1280, 720, "git.doomhowl-interactive.com") //
                .bgColor(new Color(0, 0, 0, 0)) //
                .darkMode(false);
        ImageIO.write(lightChartImage.draw(group, 2025), "png", new File("light.png"));

        ChartImage darkChartImage = new ChartImage(800, 600, "git.doomhowl-interactive.com") //
                .bgColor(new Color(0, 0, 0, 0)) //
                .darkMode(true);
        ImageIO.write(darkChartImage.draw(group, 2025), "png", new File("dark.png"));
    }
}
