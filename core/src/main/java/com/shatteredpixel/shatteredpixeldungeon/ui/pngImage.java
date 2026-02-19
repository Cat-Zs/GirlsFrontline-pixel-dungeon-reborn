package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.noosa.Image;

public class pngImage extends Image {
    private static final String pngImage = "pngimage/";
    public pngImage(String assets){
        super(pngImage+assets);
    }
    public pngImage(String assets, float size, boolean strict){
        super(pngImage+assets);
        if (strict)
            scale.set(size/width, size/height);
        else
            scale.set(size);
    }
    public pngImage(String assets, float width, float height, boolean strict){
        super(pngImage+assets);
        if (strict)
            scale.set(width/this.width, height/this.height);
        else
            scale.set(width, height);
    }
}
