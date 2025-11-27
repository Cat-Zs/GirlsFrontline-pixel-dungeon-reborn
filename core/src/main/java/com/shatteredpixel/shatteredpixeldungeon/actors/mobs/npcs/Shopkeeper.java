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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Shopkeeper extends NPC {

    private int turnsSinceHarmed;

    {
		spriteClass = ShopkeeperSprite.class;
        turnsSinceHarmed = -1;
		properties.add(Property.IMMOVABLE);
	}


    protected boolean act() {
        if (this.turnsSinceHarmed >= 0) {
            ++this.turnsSinceHarmed;
        }

        this.sprite.turnTo(this.pos, Dungeon.hero.pos);
        this.spend(1.0F);
        return super.act();
    }

    public void damage(int dmg, Object src) {
        this.processHarm();
    }

    public void add(Buff buff) {
        if (buff.type == Buff.buffType.NEGATIVE) {
            this.processHarm();
        }else{
            super.add(buff);
        }
    }
    public void processHarm() {
        if (Dungeon.level.heroFOV[this.pos]) {
            if (this.turnsSinceHarmed == -1) {
                this.turnsSinceHarmed = 0;
                this.yell(Messages.get(this, "warn", new Object[0]));
                ArrayList<Blob> blobs = new ArrayList();

                for(Class c : (new BlobImmunity()).immunities()) {
                    Blob b = Dungeon.level.blobs.get(c);
                    if (b != null && b.volume > 0) {
                        blobs.add(b);
                    }
                }

                PathFinder.buildDistanceMap(this.pos, BArray.not(Dungeon.level.solid, (boolean[])null), 4);

                for(int i = 0; i < Dungeon.level.length(); ++i) {
                    if (PathFinder.distance[i] < Integer.MAX_VALUE) {
                        boolean affected = false;

                        for(Blob blob : blobs) {
                            if (blob.cur[i] > 0) {
                                blob.clear(i);
                                affected = true;
                            }
                        }

                        if (affected && Dungeon.level.heroFOV[i]) {
                            CellEmitter.get(i).burst(Speck.factory(101), 2);
                        }
                    }
                }
            } else if (this.turnsSinceHarmed >= 1) {
                this.flee();
            }

        }
    }


    public void flee() {
		destroy();

		Notes.remove(Notes.Landmark.SHOP);
		
		sprite.killAndErase();
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		for (Heap heap: Dungeon.level.heaps.valueList()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				if (heap.size() == 1) {
					heap.destroy();
				}else{
					heap.items.remove(heap.size()-1);
					heap.type = Heap.Type.HEAP;
				}
			}
		}
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//shopkeepers are greedy!
	public static int sellPrice(Item item){
		float rate=1f;

		if(Dungeon.hero.hasTalent(Talent.BARGAIN_SKILLS)
		&& item instanceof Food){
			rate-=0.2f*Dungeon.hero.pointsInTalent(Talent.BARGAIN_SKILLS);
		}

		return Math.max(item.value()*2,(int)(item.value()*rate*(Dungeon.curDepth()+4f)));
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector );
	}

	public static boolean canSell(Item item){
		if (item.value() <= 0)                                              return false;
		if (item.unique && !item.stackable)                                 return false;
		if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
		if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
		return true;
	}

	private static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {
		@Override
		public String textPrompt() {
			return Messages.get(Shopkeeper.class, "sell");
		}

		@Override
		public boolean itemSelectable(Item item) {
			return Shopkeeper.canSell(item);
		}

		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact(Char c) {
		if (c != Dungeon.hero) {
			return true;
		}
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				sell();
			}
		});
		return true;
	}
}
