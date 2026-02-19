/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.UG;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Speed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Cannon extends UniversaleGun {

    {
        image = ItemSpriteSheet.CANNON;

        tier = 1;
        RCH = 70;    //lots of extra reach
        ACC = 20f;
        DLY = 0.1f;
        DEF = 30;
        defaultAction = AC_CHANGE;
    }

    private static final String AC_CHANGE = "CHANGE";
    public boolean mustDie = false;

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_CHANGE);
        return actions;
    }
    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CHANGE)){
            mustDie = !mustDie;
            if(mustDie)
                GLog.p("切换至秒杀模式。");
            else
                GLog.n("切换至正常攻击模式。");
        }
    }

    @Override
    public int max(int lvl) {
        return  500*(tier+1) +    //8 base, down from 10
                lvl*(tier+60);   //scaling unchanged
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            Buff.affect(hero, Light.class, 10);
            //Dungeon.Flag = true;
            if (enemy instanceof Mob){
                if (mustDie){
                    return 0;
                }else if(((Mob) enemy).surprisedBy(hero)) {
                    //deals 75% toward max to max on surprise, instead of min to max.
                    int diff = max() - min();
                    int damage = augment.damageFactor(Random.NormalIntRange(
                            min() + Math.round(diff*3.75f),
                            max()));
                    int exStr = hero.STR() - STRReq();
                    if (exStr > 0) {
                        damage += Random.IntRange(0, exStr);
                    }
                    owner.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                    hero.HP += Math.min(damage, hero.HT-hero.HP);
                    return damage;
                }
            }
        }
        owner.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
        int damage = super.damageRoll(owner);
        owner.HP += Math.min(damage, owner.HT-owner.HP);
        return damage;
    }
    @Override
    public String info(){
        String info = super.info();
        if (mustDie)
            info+="\n\n已开启秒杀模式。";
        return info;
    }
}
