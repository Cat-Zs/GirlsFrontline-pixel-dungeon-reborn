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

package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroAction;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class FieldPot extends NPC {

	{
        spriteClass = GhostSprite.class;
        alignment = Alignment.ALLY;
        properties.add(Property.INORGANIC);
        properties.add(Property.IMMOVABLE);
		state = WANDERING;
        viewDistance = 3;
        HP = HT = 20+10*Dungeon.hero.pointsInTalent(Talent.Type56_431);
	}

	@Override
	protected boolean act() {
        super.act();
        for (int i : PathFinder.NEIGHBOURS25){
            int cell = pos+i;
            Char ch = findChar(cell);
            if (ch!=null&&ch instanceof Mob &&ch.alignment!=Alignment.ALLY){
                ((Mob) ch).aggro(this);
            }
        }
        if (--HP <= 0){
            destroy();
            sprite.die();
        }

        return true;
	}

    @Override
    public int attackSkill( Char target ) {
        return INFINITE_ACCURACY;
    }

    @Override
    protected boolean canAttack( Char enemy ){
        return Dungeon.hero.pointsInTalent(Talent.Type56_433)>0;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 10);
    }

    @Override
    protected boolean doAttack( Char enemy ){
        Buff.affect(enemy, Blindness.class,5);
        return super.doAttack(enemy);
    }
    @Override
    public float attackDelay() {
        return 10-2*Dungeon.hero.pointsInTalent(Talent.Type56_433);
    }
    @Override
    public int defenseSkill( Char target ) {
        return 0;
    }

    @Override
    public int drRoll() {
        return Random.NormalIntRange(Dungeon.depth/2,Dungeon.depth);
    }
	
	@Override
	public void add( Buff buff ) {
	}

    @Override
    public void destroy() {
        super.destroy();
        Dungeon.observe();
        GameScene.updateFog(pos, viewDistance+1);
    }

	
	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );
		
		Sample.INSTANCE.play( Assets.Sounds.GHOST );

		if (c != Dungeon.hero){
			return super.interact(c);
		}else {
            ((Hero) c).curAction = new HeroAction.Alchemy( pos );
        }

		return true;
	}

    public static FieldPot getPot(){
        for (Char ch : Actor.chars()){
            if (ch instanceof FieldPot){
                return (FieldPot) ch;
            }
        }
        return null;
    }
}
