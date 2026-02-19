package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;

public class ExtraFood extends BookSpell{

    public static final ExtraFood INSTANCE = new ExtraFood();

    public int icon() {
        return 2;
    }


    {
        chargeUse=1;
        timeUse=0;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        Buff.affect(hero, FoodExtra.class, 2f);
        super.onCast(book, hero);
    }

    public static class FoodExtra extends FlavourBuff {
        public FoodExtra() {
            this.type = buffType.POSITIVE;
        }
    }
}
