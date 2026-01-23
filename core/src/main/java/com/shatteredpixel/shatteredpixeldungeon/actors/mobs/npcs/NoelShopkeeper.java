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
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.minigames.PlayGame;
import com.shatteredpixel.shatteredpixeldungeon.minigames.WndPlayGame;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NoelSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class NoelShopkeeper extends ImpShopkeeper {

	{
        spriteClass = NoelSprite.class;
        turnsSinceHarmed = -1;
        properties.add(Property.IMMOVABLE);
	}
	
	private boolean seenBefore       = false;
	public static final int RollNeed = 1200;
    public static int RollTimes      = 0;
    public static final int PlayNeed = 600;

	@Override
	protected boolean act() {
		if (!seenBefore && Dungeon.level.heroFOV[pos]) {
			yellgood( Messages.get(this, "greetings", Dungeon.hero.name() ) );
			seenBefore = true;
		}
		
		return super.act();
	}

    public String WndInfo(){
        return Messages.get(this, "wndinfo");
    }
    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                NoelShopkeeper noelShopkeeper = new NoelShopkeeper();
                String[] options = new String[3];
                int maxLen = PixelScene.landscape() ? 30 : 25;
                int i = 0;
                options[i++] = Messages.get(noelShopkeeper, "sell");
                options[i++] = Messages.get(noelShopkeeper, "roll", NoelShopkeeper.RollNeed *(NoelShopkeeper.RollTimes +1));
                options[i++] = Messages.get(noelShopkeeper, "play");

                GameScene.show(new WndOptions(noelShopkeeper.sprite(), Messages.titleCase(noelShopkeeper.name()), noelShopkeeper.WndInfo(), options) {
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0) {
                            Shopkeeper.sell();
                        } else if (index == 1) {
                            Dungeon.gold-=NoelShopkeeper.RollNeed *(NoelShopkeeper.RollTimes +1);
                            NoelShopkeeper.RollTimes++;
                            Item item = Generator.randomUsingDefaults();
                            int place;
                            Char target;
                            do{
                                place = Dungeon.hero.pos + PathFinder.NEIGHBOURS9[Random.Int(9)];
                                target = Actor.findChar(place);
                            }while(
                                    target!=null&&target.properties().contains(Property.IMMOVABLE)
                            );
                            Dungeon.level.drop(item, place).sprite.drop(Dungeon.hero.pos);
                        } else if (index == 2) {
                            Dungeon.gold-=NoelShopkeeper.PlayNeed;
                            PlayGame.gameStart();
                            GameScene.show(new WndPlayGame(false));
                        }
                    }

                    protected boolean enabled(int index) {
                        if (index == 0) {
                            return true;
                        } else if (index == 1){
                            return Dungeon.gold>=NoelShopkeeper.RollNeed *(NoelShopkeeper.RollTimes +1);
                        } else if (index == 2){
                            return Dungeon.gold>=NoelShopkeeper.PlayNeed;
                        }else
                            return super.enabled(index);
                    }

                });
            }
        });
        return true;
    }
    private final String ROLLTIME = "ROLLTIME";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(ROLLTIME, RollTimes);
    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        RollTimes = bundle.getInt(ROLLTIME);
    }
}
