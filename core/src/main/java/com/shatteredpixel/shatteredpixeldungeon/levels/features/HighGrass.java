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

package com.shatteredpixel.shatteredpixeldungeon.levels.features;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.SandalsOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class HighGrass {
	
	//prevents items dropped from grass, from trampling that same grass.
	//yes this is a bit ugly, oh well.
	private static boolean freezeTrample = false;

	public static void trample( Level level, int pos ) {
		
		if (freezeTrample) return;
		
		Char ch = Actor.findChar(pos);
		
		if (level.map[pos] == Terrain.FURROWED_GRASS){
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				//Do nothing
				freezeTrample = true;
			} else {
				Level.set(pos, Terrain.GRASS);
			}
			
		} else {
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				Level.set(pos, Terrain.FURROWED_GRASS);
				freezeTrample = true;
			} else {
				Level.set(pos, Terrain.GRASS);
			}
			
			int naturalismLevel = 0;
			
			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.itemLevel() + 1;
						naturalism.charge(1);
					} else {
						naturalismLevel = -1;
					}
				}

				//berries try to drop on floors 2/3/4/6/7/8, to a max of 4/6
				Talent.NatureBerriesAvailable berries = ch.buff(Talent.NatureBerriesAvailable.class);
				if (berries != null) {
					int targetFloor = 2 + 2*((Hero)ch).pointsInTalent(Talent.NATURES_BOUNTY);
					targetFloor -= berries.count();
					targetFloor += (targetFloor >= 5) ? 3 : 2;

					//If we're behind: 1/10, if we're on page: 1/30, if we're ahead: 1/90
					boolean droppingBerry = false;
					if (Dungeon.depth > targetFloor)        droppingBerry = Random.Int(10) == 0;
					else if (Dungeon.depth == targetFloor)  droppingBerry = Random.Int(30) == 0;
					else if (Dungeon.depth < targetFloor)   droppingBerry = Random.Int(90) == 0;

					if (droppingBerry){
						berries.countDown(1);
						level.drop(new Berry(), pos).sprite.drop();
						if (berries.count() <= 0){
							berries.detach();
						}
					}

				}
			}
			
			if (naturalismLevel >= 0) {
				// Seed, scales from 1/25 to 1/5
				if (Random.Int(25 - (naturalismLevel * 5)) == 0) {
					level.drop(Generator.random(Generator.Category.SEED), pos).sprite.drop();
				}
				
				// Dew, scales from 1/6 to 1/3
				if (Random.Int(24 - naturalismLevel*3) <= 3) {
					level.drop(new Dewdrop(), pos).sprite.drop();
				}
			}

			//Camouflage
			if (ch instanceof Hero) {
				Hero hero = (Hero) ch;
                float time = 0;
                boolean TalentAdd = false;
                int ExtraSTR = 0;
				if (hero.belongings.armor() != null ) {
                    if (hero.belongings.armor().hasGlyph(Camouflage.class, hero)) {
                        time += Camouflage.HeroActivate(hero.belongings.armor.buffedLvl());
                    }
                    if (hero.belongings.armor().STRReq()<=Dungeon.hero.STR()){
                        TalentAdd = true;
                        ExtraSTR = Dungeon.hero.STR()-hero.belongings.armor().STRReq();
                    }
				}else {
                    TalentAdd = true;
                }//装了护甲不超力或者不装护甲时允许天赋给予隐身

                Hunger hunger = Dungeon.hero.buff(Hunger.class);
                if (hunger != null && hunger.isStarving()){
                    TalentAdd = false;
                }//处于极度饥饿时截停天赋给予隐身

                if (TalentAdd && Dungeon.hero.hasTalent(Talent.Type56Two_Grass)) {
                    time += 1 + Dungeon.hero.pointsInTalent(Talent.Type56Two_Grass);
                }

                int timeV2 = 0;
                if (Dungeon.hero.hasTalent(Talent.Type56_23V2)){
                    timeV2 += ExtraSTR*Dungeon.hero.pointsInTalent(Talent.Type56_23V2);
                    timeV2 = Math.max(4+2*Dungeon.hero.pointsInTalent(Talent.Type56_23V2), timeV2);
                }
                time = Math.max(timeV2, time);
                if (time != 0) {
                    Sample.INSTANCE.play(Assets.Sounds.MELD);
                    Buff.prolong(hero, Invisibility.class, time);
                }
			} else if (ch instanceof DriedRose.GhostHero){
				DriedRose.GhostHero ghost = (DriedRose.GhostHero) ch;
				if (ghost.armor() != null && ghost.armor().hasGlyph(Camouflage.class, ghost)){
					Camouflage.activate(ghost, ghost.armor().buffedLvl());
				}
			} else if (ch instanceof ArmoredStatue){
				ArmoredStatue statue = (ArmoredStatue) ch;
				if (statue.armor() != null && statue.armor().hasGlyph(Camouflage.class, statue)){
					Camouflage.activate(statue, statue.armor().buffedLvl());
				}
			}
			
		}
		
		freezeTrample = false;
		
		if (GirlsFrontlinePixelDungeon.scene() instanceof GameScene) {
			GameScene.updateMap(pos);
			
			CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
		}
	}
}
