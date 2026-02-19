package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActHPtoGetFood;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class GetLight extends BookSpell{

    public static final GetLight INSTANCE = new GetLight();

    public int icon() {
        return 4;
    }


    {
        chargeUse=1;
        timeUse=1;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        int light = 75;
        if (Dungeon.isChallenged(Challenges.DARKNESS))
            light/=5;
        Buff.prolong(hero, Light.class, light);
        int changeC = ActHPtoGetFood.changeC;
        if (changeC== 0) {
            Buff.prolong(hero, Haste.class, 3);
        }else if (changeC == 1){
            Buff.prolong(hero, Adrenaline.class, 3);
        }
        super.onCast(book, hero);
    }

    public String desc() {
        String desc = Messages.get(this, "desc");
        if (ActHPtoGetFood.changeC == 0){
            desc += Messages.get(this, "zero");
        }else {
            desc += Messages.get(this, "one");
        }
        desc += "\n\n" + Messages.get(BookSpell.class, "charge_cost", this.chargeUse);
        return desc;
    }
}
