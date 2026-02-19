package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import static com.shatteredpixel.shatteredpixeldungeon.levels.Level.set;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Grass;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class GetGrass extends TargetSpell{

    public static final GetGrass INSTANCE = new GetGrass();

    public int icon() {
        return 3;
    }

    {
        chargeUse=1;
        timeUse=1;
    }
    protected void onSelectA(Integer cell){
        boolean near = false;
        for (int i: PathFinder.NEIGHBOURS9){
            int c = Dungeon.hero.pos+i;
            if (c==cell){
                near = true;
                break;
            }
        }
        if (!near){
            GLog.n("那里太远了");
            Stop=true;
            return;
        }
        if (Dungeon.level.map[cell] == Terrain.HIGH_GRASS){
            set(cell, Terrain.GRASS);
            Grass grass = new Grass();
            if (Dungeon.hero.pointsInTalent(Talent.Type56_23V4)>=2){
                grass.quantity(2);
            }
            if(grass.doPickUp(Dungeon.hero, cell)){
                Dungeon.hero.spendAndNext(-timeUse);
                //成功捡起则对冲时间
            }else {
                Dungeon.level.drop(grass, cell).sprite.drop(cell);
            }
            Stop=false;
        }else {
            GLog.n("收割失败");
            Stop=true;
            Sudden = Dungeon.hero.buff(LockedFloor.class)!=null && Dungeon.level.map[cell]==Terrain.BARRICADE;
            return;
        }
        GameScene.updateMap(cell);
        Dungeon.observe();
    }
    protected void onSelectB(Integer cell){
        for (int i: PathFinder.NEIGHBOURS9){
            int c = Dungeon.hero.pos+i;
            if (c==cell){
                if (Dungeon.hero.buff(LockedFloor.class)!=null){
                    if (Dungeon.level.map[cell]==Terrain.BARRICADE){
                        set(cell, Terrain.EMPTY);
                        Stop =true;
                        GLog.p("暂时作为boss层封死自己的解救措施");
                        break;
                    }
                }
            }
        }

        GameScene.updateMap(cell);
        Dungeon.observe();
    }

}
