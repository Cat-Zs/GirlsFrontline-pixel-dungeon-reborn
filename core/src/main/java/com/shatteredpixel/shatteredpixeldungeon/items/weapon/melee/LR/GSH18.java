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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.LR;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class GSH18 extends LongRange {

	{
		image = ItemSpriteSheet.GSH18; 

		tier = 1; // 设置为1级武器
		RCH = 2; // 射程2格
	}

	@Override
	public int min(int lvl) {
		return 1 + // 基础最小值1
				lvl;   // 每级成长1
	}

	@Override
	public int max(int lvl) {
		return 8 +  // 基础最大值8
				lvl*2;   // 每级成长2
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		// 无视敌方防御进行攻击，但不影响格挡
		// 通过在proc中返回原始伤害，跳过防御计算
		return damage;
	}
}