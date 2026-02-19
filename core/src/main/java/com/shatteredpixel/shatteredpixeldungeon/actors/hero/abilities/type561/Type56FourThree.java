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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FieldPot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Type56FourThree extends ArmorAbility {

	{
		baseChargeUse = 75f;
	}


	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

    @Override
    public float chargeUse(Hero hero) {
        if (FieldPot.getPot()==null) {
            return super.chargeUse(hero);
        } else {
            return 0;
        }
    }

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		if (target == null){
			return;
		}else if (!Dungeon.level.passable[target]){
            GLog.w(Messages.get(this, "cant_place"));
            return;
        }

		Char ch = Actor.findChar(target);

		if (ch != null || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(this, "cant_select"));
			return;
		}

        armor.charge -= chargeUse(hero);
        if (FieldPot.getPot()==null) {
            FieldPot pot = new FieldPot();
            pot.HT = pot.HP = 20+10*Dungeon.hero.pointsInTalent(Talent.Type56_431);
            pot.pos = target;
            GameScene.add(pot);
            if (Dungeon.hero.hasTalent(Talent.Type56_432)){
                pot.HP -= 5;
                Dungeon.energy+= 2+2*Dungeon.hero.pointsInTalent(Talent.Type56_432);
            }
        }else {
            FieldPot.getPot().targetPos = target;
            GameScene.updateMap(target);
            Dungeon.observe();
        }
        //消耗充能
		Item.updateQuickslot();
        //更新快捷栏
		Invisibility.dispel();
        //去除隐形
		hero.spendAndNext(1);
        //消耗回合
	}

	@Override
	public int icon() {
		return HeroIcon.RATMOGRIFY;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{ Talent.Type56_431, Talent.Type56_432, Talent.Type56_433, Talent.HEROIC_ENERGY};
	}

}
