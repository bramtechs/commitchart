package com.doomhowl.commitchart;

import com.doomhowl.commitchart.domain.Repository;
import com.doomhowl.commitchart.gfx.ChartImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CommitChartServer {
    public static void main(String[] args) throws IOException {
        Repository repo = new Repository("C:\\dev\\spacetyper");
        repo.open();

        ChartImage chartImage = new ChartImage(600,300);
        BufferedImage img = chartImage.draw(repo, 2025);
        ImageIO.write(img, "png", new File("out.png"));
    }
}
