package com.doomhowl.commitchart.gfx;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {
    private FontLoader() {
    }

    public static void configureGraphics(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static Font loadFontFromJar(String path, float size) {
        try (InputStream is = FontLoader.class.getResourceAsStream("/" + path)) {
            if (is == null) throw new FontLoaderError("Font resource not found: " + path);
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
        } catch (IOException e) {
            throw new FontLoaderError("Failed to read the font", e);
        } catch (FontFormatException e) {
            throw new FontLoaderError("Font file is in unexpected format", e);
        }
    }
}
