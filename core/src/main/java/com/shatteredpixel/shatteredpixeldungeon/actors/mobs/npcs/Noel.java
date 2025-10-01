package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NoelSprite;

public class Noel extends NPC {

    {
        spriteClass = NoelSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    @Override
    protected boolean act() {
        return super.act();
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 1000;
    }

    @Override
    public void damage( int dmg, Object src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    public boolean interact() {
        sprite.turnTo( pos, Dungeon.hero.pos );
        //mark
        return false;
    }
}