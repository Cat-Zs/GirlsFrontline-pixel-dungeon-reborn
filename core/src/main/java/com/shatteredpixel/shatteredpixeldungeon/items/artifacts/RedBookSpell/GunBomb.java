package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;

public class GunBomb extends BookSpell{

    public static final GunBomb INSTANCE = new GunBomb();

    public int icon() {
        return 6;
    }

    {
        chargeUse=1;
        timeUse=0;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        Buff.affect(Dungeon.hero,BombDamage.class, 2f);
    }

    public static class BombDamage extends FlavourBuff {
        public BombDamage() {
            this.type = buffType.POSITIVE;
        }
    }
}
