package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ActHPtoGetFood;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;

public class HPtoFood extends BookSpell{

    public static final HPtoFood INSTANCE = new HPtoFood();

    public int icon() {
        return 8;
    }

    {
        chargeUse=0;
        timeUse=0;
    }

    @Override
    public void onCast(RedBook book, Hero hero) {
        HPtoGetFood();
    }
    private static float perHPtoFood(){
        switch (Dungeon.hero.pointsInTalent(Talent.GUN_2)){
            case 1:
                return 0.33f;
            case 2:
                return 0.5f;
            case 3:
                return 0.66f;
            case 0: default:
                return 0.27f;
        }
    }
    public static void HPtoGetFood(){
        int dmgA;
        int dmgB;
        float foodA;
        float foodB;
        float buffTimeA = 15;
        float buffTimeB;
        int changeA = ActHPtoGetFood.changeA;
        if (changeA==0){
                dmgA = 15;
                foodA = 100;
        }
        else if (changeA==1) {
            dmgA = Dungeon.hero.HT / 5;
            foodA = (float) (Dungeon.hero.HT * 2) / 3;
        }
        else if (changeA == 2) {
            dmgA = Dungeon.hero.HT * 3 / 10;
            foodA = dmgA * 10 * perHPtoFood();
            buffTimeA -= 3 * Dungeon.hero.pointsInTalent(Talent.GUN_2);
        }
        else if (changeA == 3) {
            dmgA = Math.max(15, Math.round(Dungeon.hero.HT * 0.2f));
            foodA = 120;
        }
        else {
            dmgA = 0;
            foodA = 0;
            buffTimeA = 0;
        }
        dmgB = dmgA;
        buffTimeB = buffTimeA;
        Hunger hunger = Dungeon.hero.buff(Hunger.class);
        if (hunger!=null){
            foodB = Math.min(foodA, hunger.hunger());
        } else {
            foodB = foodA;
        }
        String info ="";
        info+="你可以主动消耗一些血量换取饱食度(550-当前饥饿值)，此血量消耗为真实伤害，不受减伤、护盾影响。所获得的饱食度不受饥荒影响。\n" +
                "获得饱食度的持续期间内，玩家的回复不再受到深度睡眠、增压器的正面效果的额外收益\n" +
                "当你食用食物时，超过饱食度上限的部分将会用于减少这个限制的持续时间";
        info+="当前将消耗 _"+dmgA+"_ 血量并获得 _"+foodB+"_ 饱食度";
        if (changeA==2){
            info+="另外你还将会获得 _"+buffTimeB+"_ 回合的虚弱";
        }
        GameScene.show(
                new WndOptions(new HeroIcon(new HPtoFood()),
                        Messages.titleCase("技能名"),
                        info,
                        "确认",
                        "取消") {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0) {
                            Dungeon.hero.HP-=dmgB;
                            if (Dungeon.hero.HP<=0){
                                Dungeon.hero.die(this);
                            }else {
                                if (hunger!=null){
                                    hunger.satisfy(foodB);
                                    Buff.affect(Dungeon.hero, ActHPtoGetFood.LockReg.class, foodB);
                                }
                                if (changeA == 2){
                                    Buff.affect(Dungeon.hero, Weakness.class, buffTimeB);
                                }
                            }
                        }
                    }
                }
        );
    }

}
