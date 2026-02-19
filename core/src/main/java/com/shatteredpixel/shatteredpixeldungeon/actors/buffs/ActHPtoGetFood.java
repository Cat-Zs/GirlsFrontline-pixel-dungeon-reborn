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

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell.HPtoFood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class ActHPtoGetFood extends CounterBuff implements ActionIndicator.Action {

    {
        revivePersists = true;
        type = buffType.POSITIVE;
        actPriority = HERO_PRIO+2;
    }

    // 检查是否可以使用技能
    public boolean canUse() {
        if (Dungeon.hero!=null&&Dungeon.hero.isAlive()&&Dungeon.hero.subClass==HeroSubClass.GUN_MASTER){
            Artifact.ArtifactBuff buff =Dungeon.hero.buff(RedBook.BookRecharge.class);
            return buff==null|| buff.isCursed();
        }else {
            return false;
        }
    }
    private static String CanUse = "CanUse";
    private static boolean can;
    private static String ChangeA = "ChangeA";
    public static int changeA = 3;
    private static String ChangeB = "ChangeB";
    public static int changeB = 2;
    private static String ChangeC = "ChangeC";
    public static int changeC = 1;

    @Override
    public boolean act() {
        can = canUse();
        resetAction();
        spendA(0.2F);
        return true;
    }
    public void setChangeA(int change){
        ActHPtoGetFood.changeA = change;
        ActHPtoGetFood.changeB = change;
        ActHPtoGetFood.changeC = change;
    }

    public void spendA(float time){
        TimekeepersHourglass.timeFreeze freeze = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
        if (freeze != null) {
            freeze.processTime(time);
            return;
        }

        Swiftthistle.TimeBubble bubble = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
        if (bubble != null){
            bubble.processTime(time);
            return;
        }
        spend(time);
    }
    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    // ActionIndicator.Action接口实现
    @Override
    public String actionName() {
        // 先使用硬编码文本测试按钮文本显示功能
        return "技能名";
    }

    @Override
    public Image actionIcon() {
        Image icon;
        // 使用武器占位符图标，类似于Combo类的实现
        icon = new HeroIcon(new HPtoFood());
        // 使用正确的tint方法签名（RGB颜色的16进制值）
        icon.tint(0xCC33CC); // 对应0.8f, 0.2f, 0.8f的颜色
        return icon;
    }

    private void resetAction(){
        // 检查是否需要显示/隐藏技能按钮
        if (canUse()) {
            ActionIndicator.setAction(this);
        }else if (!canUse()) {
            ActionIndicator.clearAction(this);
        }
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        can = false;
        if (bundle.contains(CanUse)) {
            can = bundle.getBoolean(CanUse);
        }
        if (can) {
            ActionIndicator.setAction(this);
        }
        changeA = bundle.getInt(ChangeA);
        changeB = bundle.getInt(ChangeB);
        changeC = bundle.getInt(ChangeC);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CanUse, can);
        bundle.put(ChangeA, changeA);
        bundle.put(ChangeB, changeB);
        bundle.put(ChangeC, changeC);
    }
    @Override
    public void doAction() {
        HPtoFood.HPtoGetFood();
    }
    public static class LockReg extends FlavourBuff{
        {
            type = buffType.POSITIVE;
        }
        public static float lost = 0;
        private static final String LOST = "LOST";

        @Override
        public void detach(){
            lost = 0;
            super.detach();
        }
        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            lost = bundle.getFloat(LOST);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(LOST, lost);
        }
    }
}