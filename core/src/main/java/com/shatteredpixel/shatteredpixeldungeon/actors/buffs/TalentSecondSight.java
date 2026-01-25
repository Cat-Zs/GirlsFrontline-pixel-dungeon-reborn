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

    public ArrayList<String> LevelID = new ArrayList<>() ;
    public ArrayList<Integer> CoolDown = new ArrayList<>() ;
    private static final String ID = "TSCID";
    private static final String CD = "TSCCD";
    @Override
    public String toString() {
        return Messages.get(this, "name");
    }
    public String desc() {
        return Messages.get(this, "desc", descA());
    }
    public int icon() {
        boolean on = false;
        for (int i : CoolDown){
            if (i>0) {
                on = true;
                break;
            }
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
                desc+="楼层"+LevelID.get(j)+"仍需："+CoolDown.get(j)+" 回合";
            }
            j++;
        }
        return desc;
    }
    public void Set(int id, int sub, int cd){
        String level = "["+id+"]";
        if (sub!=0)
            level += "/"+sub;
        if (LevelID.contains(level)){
            int j = 0;
            for (String i :LevelID){
                if (i.equals(level)){
                    break;
                }else {
                    j++;
                }
            }
            CoolDown.set(j, cd);
        }else {
            LevelID.add(level);
            CoolDown.add(cd);
        }
    }
    public boolean EndCD(int id, int sub){
        String level = "["+id+"]";
        if (sub!=0)
            level += "/"+sub;
        if (LevelID.contains(level)){
            int j = 0;
            for (String i :LevelID){
                if (i.equals(level)){
                    break;
                }else {
                    j++;
                }
            }
            return CoolDown.get(j)==0;
        }else {
            Set(id, sub,0);
            return true;
        }
    }
    @Override
    public boolean act() {
        String level = "["+Dungeon.depth+"]";
        if (Dungeon.SUBId!=0)
            level += "/"+Dungeon.SUBId;
        int j = 0;
        for (String i : LevelID){
            if (i.equals(level)) {
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
            String[] IDToSave = new String[LevelID.size()];
            for (String i : LevelID) {
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
        String[] IDToLoad = bundle.getStringArray(ID);
        if (IDToLoad != null) {
            for (int j = 0; j < IDToLoad.length; j++) {
                try {
                    LevelID.add(IDToLoad[j]);
                } catch (Exception e) {
                    GirlsFrontlinePixelDungeon.reportException(e);
                }
            }
        }

        CoolDown = new ArrayList<>();
        int[] CDToLoad = bundle.getIntArray(CD);
        if (CDToLoad != null) {
            for (int i = 0; i < CDToLoad.length; i++) {
                try {
                    CoolDown.add(CDToLoad[i]);
                } catch (Exception e) {
                    GirlsFrontlinePixelDungeon.reportException(e);
                }
            }
        }
	}
}
