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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.RabbitBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.SewerBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

public class GolyatBomb extends Bomb {
	
	{
		image = ItemSpriteSheet.GOLYAT_BOMB;
	}
	
	@Override
	public void explode(int cell) {
		super.explode(cell);
        if(!(Dungeon.bossLevel())||(Dungeon.level instanceof CavesBossLevel)||(Dungeon.level instanceof SewerBossLevel)||(Dungeon.level instanceof RabbitBossLevel)){
            boolean terrainAffected = false;
            for (int i : PathFinder.NEIGHBOURS4){
                int c = cell + i;
                if (c >= 0 && c < Dungeon.level.length()){
                    if (Dungeon.level.breakable[c]){
                        set(c, Terrain.EMBERS);
                        GameScene.updateMap(c);
                        terrainAffected = true;
                    }
                    Heap heap = Dungeon.level.heaps.get(c);
                    if (heap != null)
                        heap.explodeLOCK();
                }
            }
            if(Dungeon.level.breakable[cell]){
                set(cell, Terrain.EMBERS);
                GameScene.updateMap(cell);
                terrainAffected = true;
            }
            if (terrainAffected) {
                Dungeon.observe();
            }
        }
        Heap heap = Dungeon.level.heaps.get(cell);
        if (heap != null)
            heap.explodeLOCK();
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 30);
	}
}
