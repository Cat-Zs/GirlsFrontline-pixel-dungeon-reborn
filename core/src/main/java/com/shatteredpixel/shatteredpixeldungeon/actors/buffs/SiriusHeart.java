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
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class SiriusHeart extends Buff implements ActionIndicator.Action {

    {
        type = buffType.NEUTRAL;
    }

    private static final String SIRIUS_HEART_COOLDOWN = "sirius_heart_cooldown";
    private static final String SIRIUS_HEART_BOOSTED = "sirius_heart_boosted";

    private float cooldown = 0f;
    private boolean boosted = false;

    // 检查是否可以使用技能
    public boolean canUse() {
        if (!(target instanceof Hero)) return false;
        Hero hero = (Hero) target;
        // 检查是否转职为未来之星
        if (hero.subClass != HeroSubClass.FUTURE_STAR) return false;
        // 检查是否有星之护盾
        StarShield starShield = hero.buff(StarShield.class);
        if (starShield == null || starShield.shielding() <= 0) return false;
        // 检查天赋是否加点
        if (hero.pointsInTalent(Talent.GSH18_SIRIUS_HEART) <= 0) return false;
        // 检查是否在冷却中
        if (cooldown > 0) return false;
        return true;
    }

    // 激活技能
    public void activate() {
        if (!canUse()) return;
        
        Hero hero = (Hero) target;
        int talentLevel = hero.pointsInTalent(Talent.GSH18_SIRIUS_HEART);
        
        // 施加buff
        Buff.affect(hero, Talent.SiriushHeartTracker.class);
        
        // 设置冷却时间
        cooldown = 300f;
        
        GLog.p(Messages.get(this, "activated"));
        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
        
        // 更新UI
        BuffIndicator.refreshHero();
        ActionIndicator.setAction(this);
    }

    @Override
    public boolean act() {
        // 减少冷却时间
        if (cooldown > 0) {
            cooldown -= TICK;
            if (cooldown <= 0) {
                cooldown = 0;
                // 检查是否可以显示技能按钮
                if (canUse()) {
                    ActionIndicator.setAction(this);
                }
                BuffIndicator.refreshHero();
            }
        }
        
        // 检查是否需要显示/隐藏技能按钮
        Hero hero = (Hero) target;
        if (hero != null) {
            if (canUse() && ActionIndicator.action != this) {
                ActionIndicator.setAction(this);
            } else if (!canUse() && ActionIndicator.action == this) {
                ActionIndicator.clearAction(this);
            }
        }
        
        spend(TICK);
        return true;
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    // 获取冷却时间的视觉显示
    public float visualcooldown() {
        return cooldown;
    }

    @Override
    public int icon() {
        return BuffIndicator.NONE; // 让buff不显示在屏幕上
    }

    @Override
    public void tintIcon(Image icon) {
        // 空实现，因为buff不显示
    }

    @Override
    public float iconFadePercent() {
        return 0f; // 不显示
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", 
                dispTurns(visualcooldown()),
                ((Hero)target).pointsInTalent(Talent.GSH18_SIRIUS_HEART) * 20
        );
    }

    // ActionIndicator.Action接口实现
    @Override
    public String actionName() {
        // 先使用硬编码文本测试按钮文本显示功能
        return "天狼星心脏";
    }

    @Override
    public Image actionIcon() {
        Image icon;
        // 使用武器占位符图标，类似于Combo类的实现
        icon = new ItemSprite(new Item(){{image = ItemSpriteSheet.WEAPON_HOLDER; }});
        // 使用正确的tint方法签名（RGB颜色的16进制值）
        icon.tint(0xCC33CC); // 对应0.8f, 0.2f, 0.8f的颜色
        return icon;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndSiriusHeart(this));
    }

    // 处理攻击时的效果
    public static void onAttack(Hero hero, Char enemy) {
        // 检查是否有SiriushHeartTracker buff
        Talent.SiriushHeartTracker tracker = hero.buff(Talent.SiriushHeartTracker.class);
        if (tracker == null) return;
        
        // 移除buff
        tracker.detach();
        
        // 获取星之护盾
        StarShield starShield = hero.buff(StarShield.class);
        if (starShield == null) return;
        
        int shieldValue = starShield.shielding();
        if (shieldValue <= 0) return;
        
        // 根据天赋等级计算附加伤害
        int talentLevel = hero.pointsInTalent(Talent.GSH18_SIRIUS_HEART);
        float multiplier = 0.2f;
        switch (talentLevel) {
            case 2:
                multiplier = 0.4f;
                break;
            case 3:
                multiplier = 1.0f;
                break;
        }
        
        int bonusDamage = (int) Math.ceil(shieldValue * multiplier);
        // 确保最低伤害为1
        bonusDamage = Math.max(1, bonusDamage);
        
        // 清除所有星之护盾
        starShield.absorbDamage(shieldValue);
        
        // 对敌人造成附加伤害
        enemy.damage(bonusDamage, hero);
        
        // 显示伤害信息
        GLog.p(Messages.get(SiriusHeart.class, "damage", bonusDamage));
        
        // 设置冷却时间
        SiriusHeart siriusHeart = hero.buff(SiriusHeart.class);
        if (siriusHeart != null) {
            siriusHeart.cooldown = 300f;
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SIRIUS_HEART_COOLDOWN, cooldown);
        bundle.put(SIRIUS_HEART_BOOSTED, boosted);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getFloat(SIRIUS_HEART_COOLDOWN);
        boosted = bundle.getBoolean(SIRIUS_HEART_BOOSTED);
    }

    // 技能窗口
    public static class WndSiriusHeart extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_HEIGHT = 18;
        private static final float GAP = 2;

        public WndSiriusHeart(final SiriusHeart buff) {
            super();

            Hero hero = (Hero) buff.target;
            int talentLevel = hero.pointsInTalent(Talent.GSH18_SIRIUS_HEART);

            // 技能描述
            String desc = Messages.get(this, "desc", 
                    (talentLevel == 1 ? 20 : (talentLevel == 2 ? 40 : 100)),
                    300
            );

            // 添加描述文本
            com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock description = new com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock(desc, 6);
            description.maxWidth(WIDTH);
            description.setSize(WIDTH, description.height());
            add(description);

            // 激活按钮
            RedButton btnActivate = new RedButton(Messages.get(this, "activate")) {
                @Override
                protected void onClick() {
                    hide();
                    buff.activate();
                }
            };
            btnActivate.setSize(WIDTH, BTN_HEIGHT);
            btnActivate.enable(buff.canUse());
            add(btnActivate);

            // 关闭按钮
            RedButton btnCancel = new RedButton(Messages.get(this, "cancel")) {
                @Override
                protected void onClick() {
                    hide();
                }
            };
            btnCancel.setSize(WIDTH, BTN_HEIGHT);
            add(btnCancel);

            // 布局
            description.setRect(0, 0, WIDTH, description.height());
            btnActivate.setRect(0, description.bottom() + GAP, WIDTH, BTN_HEIGHT);
            btnCancel.setRect(0, btnActivate.bottom() + GAP, WIDTH, BTN_HEIGHT);

            resize(WIDTH, (int) btnCancel.bottom());
        }
    }
}
