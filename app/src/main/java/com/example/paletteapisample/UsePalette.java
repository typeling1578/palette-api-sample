package com.example.paletteapisample;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;

import java.util.HashSet;
import java.util.List;

public class UsePalette {
    private final Palette palette;
    
    UsePalette(Bitmap bitmap) {
        palette = Palette.from(bitmap).generate();
    }
    
    public List<Palette.Swatch> getColors() {
        return palette.getSwatches();
    }
}
