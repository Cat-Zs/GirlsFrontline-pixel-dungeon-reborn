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

package com.shatteredpixel.shatteredpixeldungeon.items;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.levels.CavesBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Grass extends Item {
    //考虑设置草料的堆叠上限，到达上限将会分组，但这需要整理代码
    //考虑脱离隐身会增加暴露度，暴露度会增加草料消耗，并且暴露度到达一定程度将会无法以此进入隐身，暴露度随时间衰减
    //考虑增加割草难度，概率获得草料，失败则获得草渣，将堆叠草块的功能给到草渣，对枯草使用草渣概率变成草块，失败则变成草地
    //增加功能：考虑让草料可对固体使用，令其加入可燃烧II型词条，在燃烧摧毁处加入判断，可燃烧I型正常破坏（旧有地形），II型概率被破坏，失败则去除可燃烧II型词条
    //可能需要分离燃烧的摧毁函数与其余破坏的摧毁函数
	{
		image = ItemSpriteSheet.SEED_HOLDER;
		stackable = true;
        defaultAction =AC_CHOOSE;
	}
	private static final String ActA = "ACTA";
    private static final String ActB = "ACTB";
    private static final String ActC = "ACTC";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
        actions.add(ActA);
        if (enough(2)){
            actions.add(ActB);
        }
        if (enough(3456)){
            actions.add(ActC);
        }
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );
        switch (action) {
            case ActA:
                GameScene.selectCell(ActAA);
                break;
            case ActB:
                GameScene.selectCell(ActBA);
                break;
            case ActC:
                GameScene.selectCell(ActCA);
                break;
        }
	}
    private static void removeGrass(int quantity){
        Grass grass = Dungeon.hero.belongings.getItem(Grass.class);
        grass.detach(Dungeon.hero.belongings.backpack, quantity);
    }
    private static boolean enough(int quantity){
        Grass grass = Dungeon.hero.belongings.getItem(Grass.class);
        return grass.quantity >= quantity;
    }
    private static final String far = Messages.get(Grass.class, "far");
    private static final String not_enough = Messages.get(Grass.class,"not_enough");

    private static final String nothing = Messages.get(Grass.class, "nothing");
    private static final String enemy = Messages.get(Grass.class, "enemy");
    private static final String cant_select = Messages.get(Grass.class,"cant_select");
    private static final String cant_build = Messages.get(Grass.class,"cant_build");
    private static final String prompt = Messages.get(Grass.class, "prompt");

    public static CellSelector.Listener ActAA = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                boolean near = false;
                for (int i : PathFinder.NEIGHBOURS9){
                    int d = i+target;
                    if (d == Dungeon.hero.pos) {
                        near = true;
                        break;
                    }
                }
                if (!near) {
                    GLog.n(far);
                    return;
                }
                if (enough(1)){
                    Char ch = Actor.findChar(target);
                    if (ch != null) {
                        if (ch.alignment == Char.Alignment.ALLY) {
                            Buff.affect(ch, Invisibility.class, 2);
                            removeGrass(1);
                        } else {
                            GLog.n(enemy);
                        }
                    } else if (Dungeon.level.map[target] == Terrain.TRAP) {
                        set(target, Terrain.TRAP_GRASS);
                        Dungeon.level.setTrap(Dungeon.level.traps.get(target).halfHide(), target);
                        GameScene.updateMap(target);
                        removeGrass(1);
                    } else {
                        GLog.n(nothing);
                    }
                }
                else {
                    GLog.n(not_enough);
                }
            }
        }

        @Override
        public String prompt() {
            return prompt;
        }
    };
    public static CellSelector.Listener ActBA = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer target) {
            if (target != null) {
                boolean near = false;
                for (int i : PathFinder.NEIGHBOURS9){
                    int d = i+target;
                    if (d == Dungeon.hero.pos) {
                        near = true;
                        break;
                    }
                }
                if (!near) {
                    GLog.n(far);
                    return;
                }
                Char ch = Actor.findChar(target);
                if (ch!=null&&ch!=Dungeon.hero){
                    GLog.n(cant_select);
                    return;
                }
                if (enough(2)){
                    if (Dungeon.level.map[target] == Terrain.EMPTY || Dungeon.level.map[target] == Terrain.EMBERS
                            || Dungeon.level.map[target] == Terrain.PEDESTAL || Dungeon.level.map[target] == Terrain.EMPTY_SP
                            || Dungeon.level.map[target] == Terrain.EMPTY_DECO || Dungeon.level.map[target] == Terrain.WATER) {
                        set(target, Terrain.GRASS);
                        GameScene.updateMap(target);
                        removeGrass(2);
                        Dungeon.hero.spend(1);
                    } else if (Dungeon.level.map[target] == Terrain.INACTIVE_TRAP && !(Dungeon.level instanceof CavesBossLevel)) {
                        set(target, Terrain.GRASS);
                        GameScene.updateMap(target);
                        removeGrass(2);
                        Dungeon.hero.spend(1);
                    } else if (Dungeon.level.map[target] == Terrain.GRASS) {
                        set(target, Terrain.FURROWED_GRASS);
                        GameScene.updateMap(target);
                        removeGrass(2);
                        Dungeon.hero.spend(1);
                    } else {
                        GLog.n(cant_build);
                    }
                }
                else {
                    GLog.n(not_enough);
                }
            }
        }

        @Override
        public String prompt() {
            return prompt;
        }
    };
    public static CellSelector.Listener ActCA = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer target) {
            if (target != null) {

                boolean near = false;
                for (int i : PathFinder.NEIGHBOURS9){
                    int d = i+target;
                    if (d == Dungeon.hero.pos) {
                        near = true;
                        break;
                    }
                }
                if (!near) {
                    GLog.n(far);
                    return;
                }
                Char ch = Actor.findChar(target);
                if (ch!=null){
                    GLog.n(cant_select);
                    return;
                }
                if (enough(3456)){
                    if (Dungeon.level.passable[target]&&Dungeon.level.map[target] != Terrain.ENTRANCE
                            &&Dungeon.level.map[target] != Terrain.UNLOCKED_EXIT&&Dungeon.level.map[target] != Terrain.EXIT
                            //从passable中去除入口和出口
                            || Dungeon.level.map[target] == Terrain.TRAP || Dungeon.level.map[target] == Terrain.INACTIVE_TRAP
                            //9草料处理一个地表电线，倒也算正常消耗
                            || Dungeon.level.map[target] == Terrain.TRAP_GRASS) {
                        set(target, Terrain.BARRICADE);
                        GameScene.updateMap(target);
                        Dungeon.observe();
                        removeGrass(81);
                        Dungeon.hero.spend(2);
                    } else {
                        GLog.n(cant_build);
                    }
                }
                else {
                    GLog.n(not_enough);
                }
            }
        }

        @Override
        public String prompt() {
            return prompt;
        }
    };

    @Override
    public String info(){
        String info = super.info();
        info +="\n" + Messages.get(this,"ActAB");
        Grass grass = Dungeon.hero.belongings.getItem(Grass.class);
        if (grass.quantity>=2){
            info +="\n" + Messages.get(this,"ActBB");
        }
        if (grass.quantity>=3456){
            info +="\n" + Messages.get(this,"ActCB");
        }
        return info;
    }
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int value() {
		return 2 * quantity;
	}

}
