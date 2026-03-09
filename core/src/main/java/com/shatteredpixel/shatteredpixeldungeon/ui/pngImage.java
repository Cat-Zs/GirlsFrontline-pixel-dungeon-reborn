package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.watabou.noosa.Image;

public class pngImage extends Image {
    //此类用于直接将完整的png图片转换成贴图，strict的意思是，是否严格使用这个尺寸
    //严格使用的情况下，所得到的贴图的比例就是输入的尺寸
    //不严格使用的情况下，会将当前贴图的长宽均视为1然后扩大或缩小到输入的尺寸
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
