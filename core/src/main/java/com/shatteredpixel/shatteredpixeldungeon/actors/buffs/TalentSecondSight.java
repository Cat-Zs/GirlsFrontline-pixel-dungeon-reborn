/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2020 Evan Debenham
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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

//A buff whose only purposes is to keep track of a count of some form
public class TalentSecondSight extends Buff {
    {
        revivePersists = true;
    }

    public ArrayList<Integer> LevelID = new ArrayList<>() ;
    public ArrayList<Integer> CoolDown = new ArrayList<>() ;
    private static final String ID = "ID";
    private static final String CD = "CD";
    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    public String desc() {
        return Messages.get(this, "desc", descA());
    }
    public int icon() {
        boolean on = false;
        int num = 0;
        for (int i : CoolDown){
            if (i>0
                    &&LevelID.get(num)%1000<=Dungeon.depth+2
                    &&LevelID.get(num)%1000>=Dungeon.depth-2) {
                on = true;
                break;
            }
            num++;
        }
        if (on){
            return BuffIndicator.MIND_VISION;
        }else
            return super.icon();
    }

    public void tintIcon(Image icon) {
        icon.hardlight(1F, 2F, 3F);
    }
    private String descA(){
        String desc = "";
        int j = 0;
        for (int i : CoolDown){
            if (i>0) {
                if (desc!=""){
                    desc+="\n";
                }
                if (LevelID.get(j)%1000<=Dungeon.depth+2
                        &&LevelID.get(j)%1000>=Dungeon.depth-2){
                    int sub = 0;
                    int levelId = LevelID.get(j);
                    String level;
                    while (levelId != LevelID.get(j) % 1000) {
                        sub++;
                        levelId -= 1000;
                        if (levelId <= 0)
                            break;
                    }
                    level = String.valueOf(levelId);
                    if (sub != 0)
                        level += "/" + sub;
                    desc += "楼层" + level + "仍需：" + CoolDown.get(j) + " 回合";
                }
            }
            j++;
        }
        return desc;
    }
    public void Set(int id, int cd){
        if (LevelID.contains(id)){
            int j = 0;
            for (int i :LevelID){
                if (i==id){
                    break;
                }else {
                    j++;
                }
            }
            CoolDown.set(j, cd);
        }else {
            LevelID.add(id);
            CoolDown.add(cd);
        }
    }
    public boolean EndCD(int id){
        if (LevelID.contains(id)){
            int j = 0;
            for (int i :LevelID){
                if (i==id){
                    break;
                }else {
                    j++;
                }
            }
            return CoolDown.get(j)==0;
        }else {
            Set(id, 0);
            return true;
        }
    }
    @Override
    public boolean act() {
        int j = 0;
        for (int i : LevelID){
            if (i==Dungeon.levelId) {
                int cd = CoolDown.get(j);
                if (cd>0) {
                    CoolDown.set(j,cd-1);
                }
                break;
            }
            j++;
        }

        spend(1);
        return true;
    }

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
        int countA = 0;
        if (!LevelID.isEmpty()) {
            int[] IDToSave = new int[LevelID.size()];
            for (int i : LevelID) {
                IDToSave[countA++] = i;
            }
            bundle.put(ID, IDToSave);
            //楼层编号
        }
        if (!CoolDown.isEmpty()){
            int countB = 0;
            int[] CDToSave = new int[CoolDown.size()];
            for (int j : CoolDown) {
                CDToSave[countB++] = j;
            }
            bundle.put(CD, CDToSave);
            //对应楼层CD
        }
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

        LevelID = new ArrayList<>();
        CoolDown = new ArrayList<>();
        int[] IDToLoad;
        int[] CDToLoad;
        if (bundle.contains(ID)){
            IDToLoad = bundle.getIntArray(ID);
            CDToLoad = bundle.getIntArray(CD);
            if (IDToLoad != null) {
                for (int i = 0; i < IDToLoad.length; i++) {
                    if (i>=CDToLoad.length)
                        break;
                    try {
                        LevelID.add(IDToLoad[i]);
                        CoolDown.add(CDToLoad[i]);
                    } catch (Exception e) {
                        GirlsFrontlinePixelDungeon.reportException(e);
                    }
                }
            }
        }

	}
}
