package com.doomhowl.commitchart.server;

import com.doomhowl.commitchart.domain.RepositoryGroup;
import com.doomhowl.commitchart.gfx.ChartImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ChartImageFactory {
    private final Map<ChartImageSettings, File> mCache;

    public ChartImageFactory() {
        mCache = new HashMap<>();
    }

    public File createOrRender(ChartImageSettings settings) {
        return mCache.computeIfAbsent(settings, chartImageSettings -> {
            try {
                ChartImage img = new ChartImage(settings.width(), settings.height(), settings.caption()).bgColor(settings.bgColor()).darkMode(settings.darkMode());
                RepositoryGroup group = new RepositoryGroup(settings.repoDir());
                group.open();

                System.out.println("Done reading the git logs.");

                File outFile;
                outFile = File.createTempFile("commitchart", ".png");
                ImageIO.write(img.draw(group, settings.year()), "png", outFile);
                System.out.println("Wrote commit chart image to " + outFile.getAbsolutePath());
                return outFile;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("An error occurred while generating the image: " + e);
                throw new RuntimeException(e);
            }
        });
    }
}
