package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Bee;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class DeadBomb extends TargetSpell{

    public static final DeadBomb INSTANCE = new DeadBomb();

    public int icon() {
        return 1;
    }

    {
        chargeUse=3;
        timeUse=1;
    }
    @Override
    protected void onSelectA(Integer cell){
        Char target=Char.findChar(cell);
        if (!(target instanceof Mob || target instanceof Hero)){
            Stop = true;
            return;
        }if(target.alignment==Char.Alignment.ENEMY){
            Stop = false;
            deadBomb(target);
        }else if(target.alignment==Char.Alignment.NEUTRAL&&(target instanceof Mimic ||target instanceof Bee)){
            Stop = false;
            deadBomb(target);
        }else {
            GLog.n("这是不可选中的单位");
            Stop = true;
        }
    }
    private void deadBomb(Char target){
        Char fear= Dungeon.hero;
        if(!target.isAlive()||Mob.Alignment.ALLY==target.alignment){
            //并非存活或者目标是友方单位，属于误入这个函数，将直接退出
            GLog.n("如果看到这里，请联系制作组反馈");
            return;
        }

        new Flare( 5, 32 ).color( 0xFF0000, true ).show(target.sprite,2f);
        Sample.INSTANCE.play( Assets.Sounds.READ );
        Invisibility.dispel();

        int damage = (int) Math.ceil(target.HP * 0.6f);
        if (target.properties().contains(Mob.Property.MINIBOSS)) {
            damage = Math.min(damage, (int) (target.HP * 0.4f));
        } else if (target.properties().contains(Mob.Property.BOSS)) {
            damage = Math.min(damage, (int) (target.HP * 0.3f));
        }
        target.damage(damage, this);

        Buff.affect(target, Terror.class, 3).object = fear.id();

        GameScene.flash( 0xFFFFFF );

        Sample.INSTANCE.play( Assets.Sounds.BLAST );
        Invisibility.dispel();

    }

}
