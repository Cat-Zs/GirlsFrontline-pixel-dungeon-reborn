package com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActHPtoGetFood;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.TestItem;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUseItem;
import com.shatteredpixel.shatteredpixeldungeon.windows.debugSelectCode;
import com.shatteredpixel.shatteredpixeldungeon.windows.debugSelectTalent;

import java.util.ArrayList;

public class debug extends TestItem {
    {
        image = ItemSpriteSheet.SCROLL_HOLDER;
        defaultAction =AC_CHOOSE;
    }
    private static final String AC_SETMODE = "setmode";
    private static final String AC_SETCODE = "setcode";
    private static final String AC_CHOOSE  = "choose";
    public static ArrayList<ArrayList<Talent>> debugTalent = new ArrayList<>();
    private static ArrayList<Talent> T14 = new ArrayList<>();
    private static boolean AddT14 = false;
    private static boolean AddT21 = false;
    private static boolean AddT22 = false;
    private static boolean AddT23 = false;
    private static boolean AddT321 = false;
    private static boolean AddT322 = false;
    {
        T14.add(Talent.Type56_14);
        T14.add(Talent.Type56_14V2);
    }
    private static ArrayList<Talent> T21 = new ArrayList<>();
    {
        T21.add(Talent.Type56Two_FOOD);
        T21.add(Talent.Type56_21V2);
        T21.add(Talent.Type56_21V3);
    }
    private static ArrayList<Talent> T22 = new ArrayList<>();
    {
        T22.add(Talent.Type56Two_Armor);
        T22.add(Talent.Type56_22V2);
    }
    private static ArrayList<Talent> T23 = new ArrayList<>();
    {
        T23.add(Talent.Type56Two_Grass);
        T23.add(Talent.Type56_23V2);
        T23.add(Talent.Type56_23V3);
        T23.add(Talent.Type56_23V4);
    }
    private static ArrayList<Talent> T321 = new ArrayList<>();
    {
        T321.add(Talent.GUN_1);
        T321.add(Talent.GUN_1V2);
        T321.add(Talent.GUN_1V3);
    }
    private static ArrayList<Talent> T322 = new ArrayList<>();
    {
        T322.add(Talent.GUN_2);
        T322.add(Talent.GUN_2V2);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_SETMODE);
        actions.add(AC_SETCODE);
        return actions;
    }
    private void ResetA(){
        AddT14 = false;
        AddT21 = false;
        AddT22 = false;
        AddT23 = false;
        AddT321 = false;
        AddT322 = false;
    }
    private void ResetB(){
        ResetA();
        for (Talent T14: T14){
            if (Dungeon.hero.hasTalentA(T14)&&Dungeon.hero.pointsInTalentA(T14)==0)
                AddT14 = true;
        }
        for (Talent T21: T21){
            if (Dungeon.hero.hasTalentA(T21)&&Dungeon.hero.pointsInTalentA(T21)==0)
                AddT21 = true;
        }
        for (Talent T22: T22){
            if (Dungeon.hero.hasTalentA(T22)&&Dungeon.hero.pointsInTalentA(T22)==0)
                AddT22 = true;
        }
        for (Talent T23: T23){
            if (Dungeon.hero.hasTalentA(T23)&&Dungeon.hero.pointsInTalentA(T23)==0)
                AddT23 = true;
        }
        for (Talent T321: T321){
            if (Dungeon.hero.hasTalentA(T321)&&Dungeon.hero.pointsInTalentA(T321)==0)
                AddT321 = true;
        }
        for (Talent T322: T322){
            if (Dungeon.hero.hasTalentA(T322)&&Dungeon.hero.pointsInTalentA(T322)==0)
                AddT322 = true;
        }
    }
    private void ResetC(){
        ResetB();
        debugTalent = new ArrayList<>();
        if (AddT14)
            debugTalent.add(T14);
        if (AddT21)
            debugTalent.add(T21);
        if (AddT22)
            debugTalent.add(T22);
        if (AddT23)
            debugTalent.add(T23);
        if (AddT321)
            debugTalent.add(T321);
        if (AddT322)
            debugTalent.add(T322);
    }
    private static ArrayList<Integer> AC_HP = new ArrayList<>();
    {
        AC_HP = new ArrayList<>();
        AC_HP.add(0);
        AC_HP.add(1);
        AC_HP.add(2);
        AC_HP.add(3);
    }
    private static ArrayList<Integer> AC_DMG = new ArrayList<>();
    {
        AC_DMG = new ArrayList<>();
        AC_DMG.add(0);
        AC_DMG.add(1);
        AC_DMG.add(2);
    }
    private static ArrayList<Integer> AC_SKI = new ArrayList<>();
    {
        AC_SKI = new ArrayList<>();
        AC_SKI.add(0);
        AC_SKI.add(1);
    }
    private static ArrayList<ArrayList<Integer>> SelectCode = new ArrayList<>();
    {
        SelectCode = new ArrayList<>();
        SelectCode.add(AC_HP);
        SelectCode.add(AC_DMG);
        SelectCode.add(AC_SKI);
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CHOOSE)){
            GameScene.show(new WndUseItem(null, this) );
        }else if (action.equals(AC_SETMODE)) {
            ResetC();
            //TalentButton.DebugTalent(Talent.Type56_23V2, Talent.Type56Two_Grass, 2);
            GameScene.show(new debugSelectTalent(new debug(), debugTalent));
        }else if (action.equals(AC_SETCODE)){
            GameScene.show(new debugSelectCode(new debug(), SelectCode));
        }
    }
    public static String shortDesc(ArrayList list){
        return Messages.get(debug.class,getName(list));
    }
    public static String shortDesc(ArrayList list,int i){
        return Messages.get(debug.class,getName(list)+i);
    }
    public static String LongDesc(ArrayList list,int i){
        return Messages.get(debug.class,getName(list)+"_"+i);
    }
    private static String getName(ArrayList list){
        if (list == T14)
            return "T14";
        else if (list == T21)
            return "T21";
        else if (list == T22)
            return "T22";
        else if (list == T23)
            return "T23";
        else if (list == T321)
            return "T321";
        else if (list == T322)
            return "T322";
        else if (list == AC_HP)
            return "AC_HP";
        else if (list == AC_DMG)
            return "AC_DMG";
        else if (list == AC_SKI)
            return "AC_SKI";
        else if (list == SelectCode)
            return "SelectCode";
        else if (list == debugTalent)
            return "debugTalent";
        else
            return "";
    }
    public static String getInfo(ArrayList list){
        String info = "";
        info += Messages.get(debug.class, getName(list)+"info");
        return info;
    }

    public static void setCode(ArrayList list, int i){
        if (list==AC_HP){
            ActHPtoGetFood.changeA = i;
        }
        else if (list==AC_DMG){
            ActHPtoGetFood.changeB = i;
        }
        else if (list==AC_SKI){
            ActHPtoGetFood.changeC = i;
        }
    }
}