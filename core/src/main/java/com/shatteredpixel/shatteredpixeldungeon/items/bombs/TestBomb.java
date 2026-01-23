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

package com.shatteredpixel.shatteredpixeldungeon.items.bombs;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Ghoul;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Scarecrow;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Tengu;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Blacksmith;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeepCaveBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RabbitBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

public class TestBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.BOMB_HOLDER;
	}


    private boolean outMap(int cell){
        Point p = Dungeon.level.cellToPoint(cell);
        return p.x <= 0 || p.y <= 0 || p.x >= Dungeon.level.width() - 1 || p.y >= Dungeon.level.height() - 1;
    }
	@Override
	public void explode(int ignore) {
        int length = Dungeon.level.length();
        for (int i = 0; i < length; i++) {
            if (outMap(i))
                continue;
            if (Dungeon.level.map[i] == Terrain.ENTRANCE||Dungeon.level.map[i] == Terrain.EXIT||Dungeon.level.map[i] == Terrain.LOCKED_EXIT||Dungeon.level.map[i] == Terrain.UNLOCKED_EXIT) {

            }else {
                set(i, Terrain.GRASS);
            }
            Heap heap = Dungeon.level.heaps.get(i);
            if (heap != null) {
                heap.explodeLOCK();
            }
            GameScene.updateMap(i);
        }

        for (int j = 0; j < length; j++) {
            Char target = Actor.findChar(j);
            if (outMap(j))
                continue;
            if (target != null && target.alignment != Char.Alignment.ALLY) {
                if (target instanceof Tengu || target instanceof Elphelt) {
                    if (target.HP > target.HT / 2) {
                        target.HP = target.HT / 2 + 1;
                        target.damage(1, this);
                    } else {
                        target.HP = 1;
                        target.damage(1, this);
                    }
                } else if (target instanceof Ghoul) {
                    ((Ghoul) target).dieA(true, this);
                } else if (target instanceof Shopkeeper||target instanceof Ghost|| target instanceof Wandmaker||target instanceof Blacksmith||target instanceof Imp){

                } else {
                    target.die(this);
                }
            }
        }

        Level level = Dungeon.level;
        for (Blob blob : level.blobs.values()) {
            blob.fullyClear();
        }
        Dungeon.level.cleanWalls();
        Dungeon.observe();

        for (int k = 0; k < length; k++) {
            if (outMap(k))
                continue;
            Heap h = Dungeon.level.heaps.get(k);
            if (h != null) {
                Buff.append(Dungeon.hero, TalismanOfForesight.HeapAwareness.class, 100000).pos = h.pos;
            }
        }
        Scroll map = new ScrollOfMagicMapping();
        map.doRead();
    }
    @Override
    public String name(){
        return "清图炸弹";
    }
    @Override
    public String info(){
        return "用于清图。";
    }
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
