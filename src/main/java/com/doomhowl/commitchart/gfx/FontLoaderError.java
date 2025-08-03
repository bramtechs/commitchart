package com.doomhowl.commitchart.gfx;

public class FontLoaderError extends RuntimeException {
    public FontLoaderError(String message, Exception inner) {
        super(message, inner);
    }

    public FontLoaderError(String message) {
        super(message);
    }
}
