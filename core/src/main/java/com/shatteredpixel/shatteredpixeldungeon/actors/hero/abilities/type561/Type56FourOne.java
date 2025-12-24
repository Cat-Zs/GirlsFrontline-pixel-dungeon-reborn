/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.type561;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.hero;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Wound;
import com.shatteredpixel.shatteredpixeldungeon.items.KindOfWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Type56FourOne extends ArmorAbility {

	{
		baseChargeUse = 5f;
	}


	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		if (target == null){
			return;
		}


		Char ch = Actor.findChar(target);

		if (ch == null) {
			GLog.w(Messages.get(this, "no_target"));
			return;
		} else if (ch == hero){
            GLog.w(Messages.get(this, "self_target"));
            return;
		} else if (ch.alignment != Char.Alignment.ENEMY || !(ch instanceof Mob) ){
			GLog.w(Messages.get(this, "cant_attack"));
			return;
		}else if(Dungeon.level.distance(hero.pos, target)>1+ hero.pointsInTalent(Talent.Type56FourOneOne)){
            GLog.w(Messages.get(this, "cant_reach"));
            return;
        }/* else if (Char.hasProp(ch, Char.Property.MINIBOSS) || Char.hasProp(ch, Char.Property.BOSS)){
			GLog.w(Messages.get(this, "too_strong"));
			return;
		}*/ else {
            int dmg = damageRoll()*(1+hero.pointsInTalent(Talent.Type56FourOneTwo));
            int time = 4+ hero.pointsInTalent(Talent.Type56FourOneTwo);
            Wound.hit( ch );
            ch.damage(2*damageRoll(),this);
            Buff.affect( ch, Bleeding.class ).set( dmg>>1 );
            Buff.prolong( ch, Cripple.class, time );
		}
        if(hero.pointsInTalent(Talent.Type56FourOneThree)>0){
            if(Random.Int(5-hero.pointsInTalent(Talent.Type56FourOneThree))<1){
                ch.damage(10*hero.pointsInTalent(Talent.Type56FourOneThree),this);
                Buff.affect( ch, Terror.class, 10 ).object = hero.id();
            }
        }
		armor.charge -= chargeUse(hero);
        //消耗充能
		armor.updateQuickslot();
        //更新快捷栏
		Invisibility.dispel();
        //去除隐形
		hero.spendAndNext(Actor.TICK);
        //消耗回合
	}
    private int damageRoll() {
        KindOfWeapon wep = hero.belongings.weapon();
        int dmg;

        if (wep != null) {
            dmg = wep.damageRoll( hero);
        } else {
            dmg = RingOfForce.damageRoll(hero);
        }
        if (dmg < 0) dmg = 0;

        return dmg;
    }

	@Override
	public int icon() {
		return HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.Type56FourOneOne, Talent.Type56FourOneTwo, Talent.Type56FourOneThree, Talent.HEROIC_ENERGY};
	}

}
