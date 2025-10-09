package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holidays;
//暂时标注内容，后面还会改

public class Choco extends Food {
    {
        image = ItemSpriteSheet.CHOCO;
        bones = false;

    }
    
    @Override
	public int value() {
        if(Holidays.holiday == Holidays.Holiday.midAutumnFestival){
         return 100*quantity;
        } else {
        return 50*quantity;
        }
	}
    
}