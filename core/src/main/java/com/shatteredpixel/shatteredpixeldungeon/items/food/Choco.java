package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Choco extends Food {

    {
        stackable = true;
        image = ItemSpriteSheet.RATION; // 使用与MRE相同的贴图
        defaultAction = AC_EAT;
        bones = true;
    }

    @Override
    public int value() {
        return 20 * quantity;
    }
}