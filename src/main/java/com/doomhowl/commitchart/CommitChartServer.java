package com.doomhowl.commitchart;

import com.doomhowl.commitchart.domain.Repository;
import com.doomhowl.commitchart.gfx.ChartImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CommitChartServer {
    public static void main(String[] args) throws IOException {
        Repository repo = new Repository("C:\\dev\\spacetyper");
        repo.open();

        ChartImage lightChartImage = new ChartImage(800, 600, "git.doomhowl-interactive.com") //
                .bgColor(new Color(0, 0, 0, 0)) //
                .darkMode(false);
        ImageIO.write(lightChartImage.draw(repo, 2025), "png", new File("light.png"));

        ChartImage darkChartImage = new ChartImage(800, 600, "git.doomhowl-interactive.com") //
                .bgColor(new Color(0, 0, 0, 0)) //
                .darkMode(true);
        ImageIO.write(darkChartImage.draw(repo, 2025), "png", new File("dark.png"));
    }
}
