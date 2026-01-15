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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;

public class StarShield extends ShieldBuff {
	
	{
		type = buffType.POSITIVE;
	}

	private int turnsPassed = 0;

	@Override
	public void incShield(int addAmount) {
		//对非GSH18角色添加30点上限限制
		int curAmount = shielding();
		int newAmount = addAmount + curAmount;
		if(!(target instanceof Hero) || ((Hero)target).heroClass!=HeroClass.GSH18){
			if(newAmount > 30){
				newAmount = 30;
			}
		}

		int incAmount = newAmount - curAmount;
		if(incAmount <= 0){
			return;
		}
		
		int baseAmount = curAmount>30?curAmount:30;
		int overShielding = newAmount - baseAmount;
		if(overShielding > 0){
			Healing healing = target.buff(Healing.class);
			if(healing == null){
				Buff.affect(target, Healing.class).setHeal(overShielding, 0.0f, 1);
			}else{
				healing.increaseHeal(overShielding);
			}
		}

		super.incShield(incAmount);
	}

	@Override
	public boolean act() {
		int shieldingValue = shielding();
		int heroLevel = 0;
		if(target instanceof Hero){
			heroLevel = ((Hero)target).lvl;
		}
		
		// 根据护盾值范围设置不同的衰减频率
		turnsPassed++;
		if(shieldingValue <= 0) {
			detach();
		} else if (shieldingValue > 30*heroLevel) {
			decShield(2);
			turnsPassed=0;
		} else if (shieldingValue >= 30) {
			// 30-5000护盾每2回合减少1点
			if (turnsPassed >= 2) {
				decShield(1);
				turnsPassed = 0;
			}
		} else if (shieldingValue >= 10) {
			// 10-30区间每3回合减少1点
			if (turnsPassed >= 3) {
				decShield(1);
				turnsPassed = 0;
			}
		} else if (shieldingValue < 10) {
			// 10护盾以下时，每5回合衰减1点
			if (turnsPassed >= 5) {
				decShield(1);
				turnsPassed = 0;
			}
		}
		
		spend(TICK);
		return true;
	}
	
	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.SHIELDED);
		else target.sprite.remove(CharSprite.State.SHIELDED);
	}
	
	@Override
	public int icon() {
		return BuffIndicator.STAR_SHIELD;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(1f, 1f, 1f); //白色
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(shielding());
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", shielding());
	}

	private static final String TURNS_PASSED = "turns_passed";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TURNS_PASSED, turnsPassed);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		turnsPassed = bundle.getInt(TURNS_PASSED);
	}
}