package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class GetShield extends BookSpell{

    public static final GetShield INSTANCE = new GetShield();
    private static int shield = 5+(int)(Dungeon.depth*0.4F);

    public int icon() {
        return 5;
    }

    {
        chargeUse=2;
        timeUse=1;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        Buff.affect(hero, Barrier.class).setShield(shield);
        super.onCast(book, hero);
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc", shield);
        desc += "\n\n" + Messages.get(BookSpell.class, "charge_cost", chargeUse);
        return desc;
    }
}
