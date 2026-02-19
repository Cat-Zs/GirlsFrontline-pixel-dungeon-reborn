package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActHPtoGetFood;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class BookSpell {
    public static ArrayList<BookSpell> getSpellList(RedBook book, int tier) {
        ArrayList<BookSpell> spells = new ArrayList<>();
        int lvl = book.level();
        if (tier == 1) {
            spells.add(GetLight.INSTANCE);
            if (lvl >= 1) {
                spells.add(GunCd.INSTANCE);
            }
            if (lvl >= 2) {
                spells.add(GetShield.INSTANCE);
                spells.add(ExtraFood.INSTANCE);
            }
        }
        else if (tier == 2) {
            if (lvl >= 3) {
                spells.add(GunBomb.INSTANCE);
            }
            if (lvl >= 4) {
                spells.add(DeadBomb.INSTANCE);
            }
            if (Dungeon.hero.hasTalent(Talent.Type56_23V4)) {
                spells.add(GetGrass.INSTANCE);
            }
            if (Dungeon.hero.buff(ActHPtoGetFood.class)!=null){
                spells.add(HPtoFood.INSTANCE);
            }
        }
        return spells;
    }

    public int icon() {
        return 0;
    }

    public String name() {
        return Messages.get(this, "name");
    }

    public String desc() {
        String desc = Messages.get(this, "desc");
        desc += "\n\n" + Messages.get(BookSpell.class, "charge_cost", this.chargeUse);
        return desc;
    }

    public int chargeUse = 1;
    public float timeUse = 1;

    public void onCast(RedBook book, Hero hero) {
        book.onUse(chargeUse);
        Dungeon.hero.spendAndNext(timeUse);
        Item.updateQuickslot();
        Sample.INSTANCE.play("sounds/read.mp3");
    }

    public String shortDesc() {
        String Desc = Messages.get(this, "short_desc");
        Desc += "\n" + Messages.get(BookSpell.class, "charge_cost", this.chargeUse);
        return Desc;
    }
}
