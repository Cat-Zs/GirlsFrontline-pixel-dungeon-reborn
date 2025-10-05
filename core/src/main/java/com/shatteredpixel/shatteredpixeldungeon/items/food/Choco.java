package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Choco extends Food {

    {
        stackable = true;
        image = ItemSpriteSheet.CHOCO; // 从MRE贴图修改为choco贴图
        defaultAction = AC_EAT;
        bones = true;
    }

    @Override
    public int value() {
        return 20 * quantity;
    }
}