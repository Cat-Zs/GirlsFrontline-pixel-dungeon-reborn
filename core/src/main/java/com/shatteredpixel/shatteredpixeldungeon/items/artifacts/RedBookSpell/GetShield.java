package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class GetShield extends BookSpell{

    public static final GetShield INSTANCE = new GetShield();

    public int icon() {
        return 44;
    }

    {
        chargeUse=2;
        timeUse=1;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        Buff.affect(hero, Barrier.class).setShield(7);
        super.onCast(book, hero);
    }

    public String desc() {
        String desc = Messages.get(this, "desc");
        desc += "\n\n" + Messages.get(this, "charge_cost", this.chargeUse);
        return desc;
    }
}
