package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell.BookSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRedBook;
import com.watabou.utils.Bundle;

import java.util.ArrayList;


public class RedBook extends Artifact{
    public static final String AC_CAST="CAST";

    {
        image = ItemSpriteSheet.REDBOOK;

        levelCap = 10;
        exp = 0;
        charge = Math.min(level()+3, 10);
        partialCharge = 0;
        chargeCap = Math.min(level()+3, 10);

        defaultAction = AC_CAST;
        unique = true;
        bones = false;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        if ((isEquipped( hero ) || hero.hasTalent(Talent.Type56Three_Book))
                && !cursed && (charge > 0 || activeBuff != null)){
            actions.add(AC_CAST);
        }
        return actions;
    }
    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (!collect || !hero.hasTalent(Talent.Type56Three_Book)){
                if (activeBuff != null){
                    activeBuff.detach();
                    activeBuff = null;
                }
            } else {
                activate(hero);
            }

            return true;
        } else
            return false;
    }
    @Override
    public boolean collect( Bag container ) {
        if (super.collect(container)){
            if (container.owner instanceof Hero
                    && passiveBuff == null
                    && ((Hero) container.owner).hasTalent(Talent.Type56Three_Book)){
                activate((Hero) container.owner);
            }
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CAST)) {
            if (!isEquipped(hero) && !hero.hasTalent(Talent.Type56Three_Book)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
            else if (cursed)       GLog.i( Messages.get(this, "cursed") );
//            else if (charge <= 0)  GLog.i( Messages.get(this, "no_charge") );
            else {
                GirlsFrontlinePixelDungeon.scene().addToFront(new WndRedBook(this, false));
            }
        }
        lockchB();
    }
    public void onUse(int chargeUse){
        charge-=chargeUse;
        if (Dungeon.hero.buff(Talent.Type56BookTracker.class) == null) {
            Buff.count(Dungeon.hero, Talent.Type56BookTracker.class, 1);
        }
        updateQuickslot();
        Talent.onArtifactUsed(Dungeon.hero);
    }

    @Override
    public void charge(Hero target, float amount) {
        if (charge < chargeCap){
            if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.Type56Three_Book)/3f;
            partialCharge += 0.15f*amount;
            while (partialCharge >= 1){
                partialCharge--;
                charge++;
                updateQuickslot();
            }
        }
    }

    @Override
    public Item upgrade() {
        chargeCap++;
        chargeCap=Math.min(chargeCap, 10);
        if      (level() <= 3) image = ItemSpriteSheet.REDBOOK;
        else if (level() <= 6) image = ItemSpriteSheet.REDBOOK2;
        else if (level() >= 7) image = ItemSpriteSheet.REDBOOK3;
        return super.upgrade();
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if      (level() <= 3) image = ItemSpriteSheet.REDBOOK;
        else if (level() <= 6) image = ItemSpriteSheet.REDBOOK2;
        else if (level() >= 7) image = ItemSpriteSheet.REDBOOK3;
    }

    public String desc() {
        return Messages.get(this, "desc");
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new BookRecharge();
    }

    public boolean canCast(BookSpell spell) {
        return charge>=spell.chargeUse;
    }

    public class BookRecharge extends ArtifactBuff{
        @Override
        public boolean act() {
            lockcha();
            LockedFloor lock = target.buff(LockedFloor.class);
            if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
                float missing = (float)(chargeCap - charge);
                if (level() > 7) {
                    missing += (float)(5 * (level() - 7)) / 3.0F;
                }
                float turnsToCharge = 60.0F - missing*2;
                turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(this.target);
                float chargeGain = 1.0F / turnsToCharge;

                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                if (!isEquipped(Dungeon.hero)){
                    chargeGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.Type56Three_Book)/3f;
                }
                partialCharge += chargeGain;

                while (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                    }
                }
            }

            updateQuickslot();
            spend( TICK );
            return true;
        }
        public void gainExp( float levelPortion ) {
            if (cursed || levelPortion == 0) return;

            exp += Math.round(levelPortion*100);

            int base_exp = 100;
            int per_exp = 16;
            while (exp > base_exp+(level()+1)*per_exp && level() < levelCap){
                exp -= base_exp+(level()+1)*per_exp;
                GLog.p( Messages.get(this, "levelup") );
                upgrade();
                chargeCap = Math.min(level()+3, 10);
            }
        }
    }
}
