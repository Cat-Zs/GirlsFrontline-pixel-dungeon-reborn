package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class TalentGrass extends CounterBuff{

    {
        type = buffType.POSITIVE;
    }
    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    public String desc() {
        return Messages.get(this, "desc");
    }
    public int icon() {
        return BuffIndicator.INVISIBLE;
    }

    public void tintIcon(Image icon) {
        icon.hardlight(2F, 1F, 1F);
    }
    public static class GrassCD extends FlavourBuff{
        {
            type = buffType.POSITIVE;
        }
        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        public void tintIcon(Image icon) {
            icon.hardlight(1F, 2F, 1F);
        }
    }

    public static class GrassWait extends Buff {

        {
            type = buffType.POSITIVE;
        }

        public int pos = -1;
        private int wait = 2;
        @Override
        public String toString() {
            return Messages.get(this, "name");
        }
        public String desc() {
            return Messages.get(this, "desc", wait+1);
        }

        @Override
        public boolean act() {
            if (pos == -1) pos = target.pos;
            if(wait>0) {
                if (pos != target.pos) {
                    detach();
                    wait = 3;
                } else {
                    spend(TICK);
                    wait--;
                }
            }else {
                if (Dungeon.hero.buff(TalentGrass.class)==null) {
                    Buff.count(Dungeon.hero, TalentGrass.class, 1);
                    Buff.affect(Dungeon.hero, Invisibility.class, 3);
                    detach();
                }
            }
            return true;
        }

        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        public void tintIcon(Image icon) {
            icon.hardlight(1F, 1F, 2F);
        }
    }
}
