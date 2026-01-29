package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Empulse;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EnergyParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell.GunBomb;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShootGun extends MeleeWeapon {
    private static final String AC_RELOAD = "RELOAD";
    private static final String AC_SHOOT = "SHOOT";
    private int effectIndex = 2;
    private   int     reloadTime    = 2;
    private   Charger charger       = null;
    protected boolean needReload    = true;
    protected boolean hasCharge     = true;
    protected int cooldownTurns = 200;
    public    int     cooldownLeft  = 0;
    public static boolean cooldown = false;
    protected float rate = 1;
    protected float EMPduration = 0;

    private static final String HAS_CHARGE    = "hasCharge";
    private static final String COOLDOWN_LEFT = "cooldownLeft";
    private static final String COOLDOWN = "cooldown";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put(HAS_CHARGE   ,hasCharge   );
        bundle.put(COOLDOWN_LEFT,cooldownLeft);
        bundle.put(COOLDOWN, cooldown);
    }
    
    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        hasCharge    = bundle.getBoolean(HAS_CHARGE   );
        cooldownLeft = bundle.getInt    (COOLDOWN_LEFT);
        cooldown = bundle.getBoolean(COOLDOWN);
    }

    {
        image = ItemSpriteSheet.MAGESSTAFF;
        tier = 1;
        defaultAction = AC_SHOOT;
        usesTargeting = true;
        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        if (needReload && !hasCharge){
            actions.add(AC_RELOAD);
        } else
            actions.add(AC_SHOOT);
        return actions;
    }

    @Override
    public String defaultAction(){
        if (hasCharge) {
            defaultAction = AC_SHOOT;
        } else if(needReload){
            defaultAction = AC_RELOAD;
        }

        return defaultAction;
    }

    @Override
    public void execute(Hero hero,String action) {
        super.execute(hero,action);

        if (action.equals(AC_RELOAD)) {
            reload();
        } else if (action.equals(AC_SHOOT)) {
            shoot();
        }
    }

    private void reload() {
        if (0 == cooldownLeft) {
            hasCharge=true;

            curUser.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "reload") );
            curUser.sprite.operate( curUser.pos );
            updateQuickslot();

            curUser.spendAndNext(reloadTime);
        } else {
            GLog.n(Messages.get(this, "cd_status", cooldownLeft));
        }

    }

    private void shoot(){
        if (hasCharge){
            GameScene.selectCell(zapper);
        }else{
            GLog.n(Messages.get(this, "empty",cooldownLeft));
        }
    }

    public void onShootComplete(int cell, int lvl) {
        BombDestory(cell);
        int shield = BombAttack(cell, lvl);
        if(!Dungeon.hero.isAlive()){
            Dungeon.fail(getClass());
        }
        if (Dungeon.hero.hasTalent(Talent.GUN_3)){
            shield+=(Dungeon.hero.HT-Dungeon.hero.HP)*Dungeon.hero.pointsInTalent(Talent.GUN_3)/10;
            if (Dungeon.hero.HP<=Dungeon.hero.HT*(1+Dungeon.hero.pointsInTalent(Talent.GUN_3))/10){
                shield+=Math.round((Dungeon.hero.HT-Dungeon.hero.HP)*(0.05F+0.05F*Dungeon.hero.pointsInTalent(Talent.GUN_3)));
            }
        }
        if (shield>0) {
            Buff.affect(Dungeon.hero, Barrier.class).setShield(shield);
        }
        hasCharge=false;
        int down = 0;
        if(Dungeon.hero.hasTalent(Talent.Type56Three_Bomb)){
            switch (Dungeon.hero.pointsInTalent(Talent.Type56Three_Bomb)){
                case 1:
                    down=15;
                    break;
                case 2:
                    down=35;
                    break;
                case 3:
                    down=50;
                    break;
                default:
                    down=0;
                    break;
            }
        }
        if (Dungeon.hero.hasTalent(Talent.Type56_14V2)){
            Buff.prolong(Dungeon.hero, ShootTracker.class, 6f);
        }
        EMPCharge();
        cooldownLeft=cooldownTurns-down;
        cooldown = true;
        updateQuickslot();
        curUser.spendAndNext(1f);
    }
    protected void BombDestory(int cell){
        if(rate>0){
            //伤害倍率大于0
            Sample.INSTANCE.play(Assets.Sounds.BLAST);
            if (Dungeon.level.heroFOV[cell]) {
                CellEmitter.get(cell).burst(BlastParticle.FACTORY,30);
                //爆炸粒子
            }
        }
        for(int n : PathFinder.NEIGHBOURS9) {
            //对九格先执行一遍破坏
            int c =cell + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if(rate<=0){
                    //伤害倍率为0时不执行对地形和物品的破坏
                    continue;
                }
                if (Dungeon.level.flamable[c]) {
                    Dungeon.level.destroy(c);
                    GameScene.updateMap(c);
                }

                // destroys items / triggers bombs caught in the blast.
                Heap heap = Dungeon.level.heaps.get(c);
                if (heap != null)
                    heap.explode();

                if (Dungeon.level.heroFOV[c]) {
                    CellEmitter.get(c).burst(SmokeParticle.FACTORY,4);
                }
            }
        }
    }
    protected int BombAdd(){
        int add = 0;
        if (Dungeon.hero.subClass==HeroSubClass.GUN_MASTER){
            int min = 1;
            int max = 5;
            if (Dungeon.hero.hasTalent(Talent.GUN_1)){
                max+=Dungeon.hero.pointsInTalent(Talent.GUN_1);
            }
            int time = (Dungeon.hero.HT-Dungeon.hero.HP)/5;
            min*=time;
            max*=time;
            add = Random.Int(min, max);
        }
        return add;
    }
    protected int BombAttack(int cell, int lvl){
        int shield = 0;
        resetEMP();
        //重置EMP回合数
        int attack = 0;
        int[] path = PathFinder.NEIGHBOURS9;
        if (Dungeon.hero.buff(GunBomb.BombDamage.class)!=null){
            path = PathFinder.NEIGHBOURS25;
            Dungeon.hero.buff(GunBomb.BombDamage.class).detach();
        }
        if (Dungeon.hero.hasTalent(Talent.GUN_2V2)){
            path = PathFinder.NEIGHBOURS25;
        }
        for(int m : path) {
            //再执行伤害，以完整保留掉落物
            int d =cell + m;
            if (d >= 0 && d < Dungeon.level.length()) {

                Char target = Actor.findChar(d);

                if (target != null) {
                    if(Dungeon.hero.hasTalent(Talent.EMP_Three)) {
                        attack += Dungeon.hero.pointsInTalent(Talent.EMP_Three);
                    }
                    //天赋3计数
                    int damage= BombDamage(lvl);
                    damage+=BombAdd();
                    if (Dungeon.hero.hasTalent(Talent.GUN_1V3)){
                        damage = (int) (damage + (damage*(0.02f*Dungeon.hero.pointsInTalent(Talent.GUN_1V3)))*((float) (Dungeon.hero.HT - Dungeon.hero.HP) /Dungeon.hero.HT*100));
                    }
                    boolean in = false;
                    for (int n:PathFinder.NEIGHBOURS9){
                        //与内圈比对
                        int e = cell + n;
                        if (e == d){
                            in = true;
                        }
                    }
                    if (!in){
                        //非内圈时伤害*50%
                        damage/=2;
                    }
                    target.damage((int)(damage*rate),this);

                    if(target.isAlive()){
                        int bufftime = 6;
                        if (target == Dungeon.hero){
                            shield = damage;
                            //炸到自身给等量盾
                            EMPduration=Math.min(3, EMPduration);
                            //EMP最多生效3回合
                            bufftime = 3;
                        }
                        if (Dungeon.hero.hasTalent(Talent.GUN_2V2)){
                            if (Dungeon.hero.pointsInTalent(Talent.GUN_2V2)>=1){
                                Buff.affect(target, Vulnerable.class,bufftime);
                            }
                            if (Dungeon.hero.pointsInTalent(Talent.GUN_2V2)>=3){
                                Buff.affect(target, Cripple.class,bufftime);
                            }
                        }
                        if (EMPduration>0) {
                            //EMP
                            Buff.prolong(target, Empulse.class, EMPduration);
                            CellEmitter.get(d).burst(EnergyParticle.FACTORY, 10);
                            //EMP粒子
                        }
                    }
                }

            }
        }
        if (attack>0){
            attack = Math.min(attack, 6);
            for (Buff b : Dungeon.hero.buffs()){
                if (b instanceof Artifact.ArtifactBuff){
                    if (!((Artifact.ArtifactBuff) b).isCursed()) ((Artifact.ArtifactBuff) b).charge(Dungeon.hero, attack);
                }
            }
            Dungeon.hero.belongings.charge((float) attack /4);
            ScrollOfRecharging.chargeParticle(Dungeon.hero);
        }
        return shield;
    }
    protected void resetEMP(){
        if (Dungeon.hero.subClass== HeroSubClass.EMP_BOMB){
            EMPduration = 3;
            if(Dungeon.hero.hasTalent(Talent.EMP_One)){
                EMPduration+=2*Dungeon.hero.pointsInTalent(Talent.EMP_One);
            }
        }
    }
    protected void EMPCharge(){
        if (Dungeon.hero.hasTalent(Talent.EMP_Two)){
            Buff.affect(Dungeon.hero, EMPCharge.class, 7+5*Dungeon.hero.pointsInTalent(Talent.EMP_Two));
        }
    }
    protected int BombDamage(int lvl){
        return 0;
    }

    @Override
    public String status() {
        if(hasCharge){
            return "1/1";
        }else if(0 == cooldownLeft){
            return "0/1";
        }else{
            return "CD:" + cooldownLeft;
        }
    }

    public static CellSelector.Listener zapper = new  CellSelector.Listener() {
        //格子选择监听器
        @Override
        public void onSelect(Integer target) {
            if (target != null) {//由于底层原因会调用两次，所以必须判断非null
                ShootGun curShootGun = (ShootGun)curItem;

                final Ballistica shot = new Ballistica( curUser.pos, target, Ballistica.PROJECTILE);
                int cell = shot.collisionPos;

                if (target == curUser.pos || cell == curUser.pos) {
                    GLog.i( Messages.get(ShootGun.class, "self_target") );
                    return;
                }

                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                curUser.sprite.zap(cell);
                MagicMissile.boltFromChar(
                    curUser.sprite.parent,
                    curShootGun.effectIndex,
                    curUser.sprite,
                    cell,
                    new Callback(){
                        @Override
                        public void call() {
                            curShootGun.onShootComplete(cell, curShootGun.buffedLvl());
                        }
                    }
                );
            }
        }

        @Override
        public String prompt() {
            return Messages.get(ShootGun.class, "prompt");
        }
    };

    public String desc() {
        return Messages.get(this, "desc", cooldownLeft);
    }


    @Override
    public boolean collect( Bag container ) {
        if (super.collect( container )) {
            if (container.owner != null) {
                if (charger == null) {
                    charger = new Charger();
                }
                charger.attachTo(container.owner);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void activate(Char owner) {
        if (charger == null) {
            charger = new Charger();
        }
        charger.attachTo(owner);
    }

    @Override
    public void onDetach( ) {
        if(charger != null){
            charger.detach();
            charger = null;
        }
    }

    public class Charger extends Buff {
        {
            revivePersists = true;
            type = buffType.POSITIVE;
        }
        @Override
        public boolean act() {
            if (Dungeon.hero.buff(LostInventory.class)!=null)
                return true;
            cooldownLeft=Math.max(0,cooldownLeft);
            //小于0则幅值0
            LockedFloor lock = target.buff(LockedFloor.class);
            if((lock == null || lock.regenOn())
            && !hasCharge){
                if (cooldownLeft>0){
                    cooldownLeft--;
                }
                if (cooldownLeft==0)
                    cooldown = false;

                if (0==cooldownLeft && !needReload){
                    hasCharge=true;
                    updateQuickslot();
                }
            }
            cooldownLeft=Math.max(0,cooldownLeft);
            spend( TICK );
            return true;
        }
    }
    public static class EMPCharge extends FlavourBuff {

        public EMPCharge() {
            this.type = buffType.POSITIVE;
        }

        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        public void proc(Char enemy){
            if (Random.Int(5-Dungeon.hero.pointsInTalent(Talent.EMP_Two)) == 0) {
                Buff.affect(enemy, Empulse.class, 2);
                enemy.sprite.emitter().burst(EnergyParticle.FACTORY, 10);
            }
        }
        public void tintIcon(Image icon) {
            icon.hardlight(1.0F, 1.0F, 0.0F);
        }
    }

    public static class ShootTracker extends FlavourBuff {
        {
            revivePersists = true;
        }
    }
}
