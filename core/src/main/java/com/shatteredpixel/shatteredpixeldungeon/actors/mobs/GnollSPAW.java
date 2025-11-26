//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff.buffType;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSPAWSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import java.util.ArrayList;

public class GnollSPAW extends Gnoll {
    public GnollSPAW() {
        this.spriteClass = GnollSPAWSprite.class;
        this.PASSIVE = new Passive();
        this.WANDERING = new Wandering();
        this.state = this.PASSIVE;
        this.defenseSkill = 6;
        this.HP = this.HT = 24;
        this.lootChance = 0.0F;
    }

    public int damageRoll() {
        return Random.NormalIntRange(1, 10);
    }

    public int attackSkill(Char target) {
        return 15;
    }

    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 1);
    }

    protected boolean canAttack(Char enemy) {
        if (Dungeon.level.adjacent(this.pos, enemy.pos)) {
            return true;
        } else {
            if (Dungeon.level.distance(this.pos, enemy.pos) <= 2) {
                boolean[] passable = BArray.not(Dungeon.level.solid, (boolean[])null);

                for(Char ch : Actor.chars()) {
                    passable[ch.pos] = ch == this;
                }

                PathFinder.buildDistanceMap(enemy.pos, passable, 2);
                if (PathFinder.distance[this.pos] <= 2) {
                    return true;
                }
            }

            return super.canAttack(enemy);
        }
    }

    public void rollToDropLoot() {
        super.rollToDropLoot();
        if (Dungeon.hero.lvl <= this.maxLvl + 2) {
            ArrayList<Item> items = new ArrayList<>();
            items.add(Generator.randomUsingDefaults());
            items.add(Generator.randomUsingDefaults());
            if (Random.Int(2) == 0) {
                items.add(Generator.randomUsingDefaults());
            }

            for(Item item : items) {
                int ofs = PathFinder.NEIGHBOURS9[Random.Int(9)];
                if (!Dungeon.level.solid[this.pos + ofs] || Dungeon.level.passable[this.pos + ofs]) {
                    Dungeon.level.drop(item, this.pos + ofs).sprite.drop(this.pos);
                }
            }

        }
    }

    public void beckon(int cell) {
        if (this.state != this.PASSIVE) {
            super.beckon(cell);
        } else {
            this.target = cell;
        }

    }

    public String description() {
        String desc = super.description();
        if (this.state == this.PASSIVE) {
            desc = desc + "\n\n" + Messages.get(this, "desc_passive", new Object[0]);
        } else {
            desc = desc + "\n\n" + Messages.get(this, "desc_aggro", new Object[0]);
        }

        return desc;
    }

    private class Passive extends Mob.Wandering {
        private int seenNotifyCooldown;

        private Passive() {
            this.seenNotifyCooldown = 0;
        }

        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            for(Buff b : GnollSPAW.this.buffs()) {
                if (b.type == buffType.NEGATIVE) {
                    GnollSPAW.this.state = GnollSPAW.this.WANDERING;
                    return true;
                }
            }

            if (GnollSPAW.this.fieldOfView[Dungeon.hero.pos] && Dungeon.level.heroFOV[GnollSPAW.this.pos]) {
                if (this.seenNotifyCooldown <= 0) {
                    GLog.p(Messages.get(GnollSPAW.class, "seen_passive", new Object[0]), new Object[0]);
                }

                this.seenNotifyCooldown = 10;
            } else {
                --this.seenNotifyCooldown;
            }

            if (enemyInFOV && justAlerted) {
                if (Dungeon.level.heroFOV[GnollSPAW.this.pos]) {
                    GLog.w(Messages.get(GnollSPAW.class, "seen_aggro", new Object[0]), new Object[0]);
                }

                return this.noticeEnemy();
            } else {
                return this.continueWandering();
            }
        }
    }

    private class Wandering extends Mob.Wandering {

        protected boolean noticeEnemy() {
            GLog.w(Messages.get(GnollSPAW.class, "seen_aggro", new Object[0]), new Object[0]);
            return super.noticeEnemy();
        }
    }
}
