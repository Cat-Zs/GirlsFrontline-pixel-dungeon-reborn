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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Choco;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.FncSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

// 在导入部分添加CounterBuff类的导入
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.CounterBuff;

public class RatKing extends NPC {

	{        
		spriteClass = FncSprite.class;
		
		state = SLEEPING;
	}
	
	// 添加持久化的choco获取计数器
	public static class ChocoTracker extends CounterBuff {
		{ revivePersists = true; }
	}
    
    // 添加choco限制提示计数器
    public static class ChocoLimitNotifier extends CounterBuff {
        { revivePersists = true; }
    }
	
	@Override
	public int defenseSkill( Char enemy ) {
		return INFINITE_EVASION;
	}
	
	@Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//***This functionality is for when rat king may be summoned by a distortion trap

	@Override
	protected void onAdd() {
		super.onAdd();
		if (Dungeon.depth != 5){
			yell(Messages.get(this, "confused"));
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.depth < 5){
			if (pos == Dungeon.level.exit){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.exit;
			}
		} else if (Dungeon.depth > 5){
			if (pos == Dungeon.level.entrance){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.entrance;
			}
		}
		return super.act();
	}

	//***

	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return super.interact(c);
		}

		KingsCrown crown = Dungeon.hero.belongings.getItem(KingsCrown.class);
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, "not_sleeping") );
			state = WANDERING;
		} else if (crown != null){
			if (Dungeon.hero.belongings.armor() == null){
				yell( Messages.get(RatKing.class, "crown_clothes") );
			} else {
				Badges.validateRatmogrify();
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndOptions(
								sprite(),
								Messages.titleCase(name()),
								Messages.get(RatKing.class, "crown_desc"),
								Messages.get(RatKing.class, "crown_yes"),
								Messages.get(RatKing.class, "crown_info"),
								Messages.get(RatKing.class, "crown_no")
						){
							@Override
							protected void onSelect(int index) {
								if (index == 0){
									crown.upgradeArmor(Dungeon.hero, Dungeon.hero.belongings.armor(), new Ratmogrify());
									((FncSprite)sprite).resetAnims();
									yell(Messages.get(RatKing.class, "crown_thankyou"));
								} else if (index == 1) {
									GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, new Ratmogrify()));
								} else {
									yell(Messages.get(RatKing.class, "crown_fine"));
								}
							}
						});
					}
				});
			}
		} else if (Dungeon.hero.armorAbility instanceof Ratmogrify) {
			yell( Messages.get(this, "crown_after") );
		} else {
			// 检查choco获取次数
			ChocoTracker tracker = Dungeon.hero.buff(ChocoTracker.class);
			float chocoCount = (tracker != null) ? tracker.count() : 0;
			
			if (chocoCount < 2) {
				// 玩家还可以获取choco
				Choco t1 = new Choco();
				t1.identify();
				if (t1.doPickUp(Dungeon.hero)) {
					// 显示获得信息
					Messages.get(Dungeon.hero, "you_now_have", t1.name());
				} else {
					// 掉落至pos+1位置
					Dungeon.level.drop(t1, Dungeon.hero.pos + 1).sprite.drop();
				}
				
				// 增加计数器
				Buff.count(Dungeon.hero, ChocoTracker.class, 1);
			} else {
				// 检查提示次数
				ChocoLimitNotifier notifier = Dungeon.hero.buff(ChocoLimitNotifier.class);
				float notifyCount = (notifier != null) ? notifier.count() : 0;
				
				if (notifyCount < 3) {
					// 根据提示次数显示不同的消息
					if (notifyCount == 0) {
						yell(Messages.get(this, "choco_limit"));
					} else if (notifyCount == 1) {
						yell(Messages.get(this, "choco_limit_2"));
					} else {
						yell(Messages.get(this, "choco_limit_3"));
					}
					// 增加提示计数器
					Buff.count(Dungeon.hero, ChocoLimitNotifier.class, 1);
				} else {
					// 已经提示了3次，根据玩家状态显示相应的原始对话
					if (Dungeon.hero.armorAbility instanceof Ratmogrify) {
						yell(Messages.get(this, "crown_after"));
					} else {
						yell(Messages.get(this, "what_is_it"));
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public String description() {
		return ((FncSprite)sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();
	}
}
