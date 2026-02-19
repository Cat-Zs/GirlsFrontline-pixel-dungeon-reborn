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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SMG;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Ump45 extends SubMachineGun {

    {
        BASE_COOLDOWN_TURNS = 300;
    }
    {
        image = ItemSpriteSheet.UMP45;

        tier = 1;
        RCH = 1;
        ACC = 0.7f;
        DEF = 1;
        DEFUPGRADE = 1;

        defaultAction = AC_SKILL;
        usesTargeting = true;
    }
/**/
    @Override
    public int max(int lvl) {
        return  5*(tier+1) +    //8 base, down from 10
                lvl*(tier+1);   //scaling unchanged
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SKILL);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);

        if (action.equals(AC_SKILL)) {
            //检查是否装备，复制的TimekeepersHourglass
            if (!isEquipped( hero )) {
                GLog.w(Messages.get(this, "must_hold"));
            }
            //检查是否有毒
            else if (cursed){
                GLog.i( Messages.get(this, "curse") );
            }
            //检查是否超力
            else if (hero.STR() < STRReq()) {
                GLog.w(Messages.get(Weapon.class, "too_heav"));
            }
            //检查是否cd
            else if (coolDownLeft > 0) {
                GLog.w(Messages.get(this, "cooldown", coolDownLeft));
            }
            //没有进入上述if，即满足全部要求之后，进入此处ThrowSmoke动作
            else {
                GameScene.selectCell(SmokeSelector);
            }
        }
    }
    private final CellSelector.Listener SmokeSelector = new CellSelector.Listener() {
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                //使用SMOKEB中的cast操作，Potion的cast不会保留掉落物
                Smoke smoke = new Smoke();
                smoke.cast(curUser, target);
                coolDownLeft = BASE_COOLDOWN_TURNS;
                // 更新快捷栏显示
                updateQuickslot();
            }
        }
        //这里要加文案
        @Override
        public String prompt() {
            return Messages.get(Ump45.class, "select_target");
        }
    };
    private static class Smoke extends Item {

        {
            image = ItemSpriteSheet.SMOKEUmp45;
        }

        public void explore( int cell ) {
            //气体音效
            if (Dungeon.level.heroFOV[cell]) {
                Sample.INSTANCE.play( Assets.Sounds.GAS );
            }

            int centerVolume = 50;
            //中心格非固体
            if (!Dungeon.level.solid[cell]){
                GameScene.add( Blob.seed( cell, centerVolume, SmokeScreen.class ) );
            }
            //中心格为固体
            else {
                int j =0;
                for (int i : PathFinder.NEIGHBOURS8){
                    if (!Dungeon.level.solid[cell+i]){
                        //累计邻格非固体格子数量
                        j++;
                    }
                }
                for (int i : PathFinder.NEIGHBOURS8){
                    if (!Dungeon.level.solid[cell+i]){
                        //给予邻格非固体格子均分的气体量
                        GameScene.add( Blob.seed( cell+i, centerVolume/j, SmokeScreen.class ) );
                    }
                }
            }
        }

        @Override
        public void onThrow( int cell ){
            explore(cell);
        }

    }

}

