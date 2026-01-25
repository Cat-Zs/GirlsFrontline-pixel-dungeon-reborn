package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.PrismaticImage;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class NoelShopKeeperSprite extends MobSprite {
    public NoelShopKeeperSprite() {
        super();

        texture( Dungeon.hero.heroClass.spritesheet() );
        updateTexture(  );
        idle();
    }

    @Override
    public void link( Char ch ) {
        super.link( ch );
        updateTexture();
    }

    public void updateTexture() {

        texture( Assets.Sprites.NOEL );

        TextureFilm frames = new TextureFilm( texture, 14, 16 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2, 2, 3, 3 );

        run = new Animation( 20, true );
        run.frames( frames, 0 );

        die = new Animation( 20, false );
        die.frames( frames, 0 );

        play( idle );
    }

    @Override
    public void update() {
        super.update();

        if (flashTime <= 0){
            float interval = (Game.timeTotal % 9 ) /3f;
            tint(interval > 2 ? interval - 2 : Math.max(0, 1 - interval),
                    interval > 1 ? Math.max(0, 2-interval): interval,
                    interval > 2 ? Math.max(0, 3-interval): interval-1, 0.5f);
        }
    }

    @Override
    public void die() {
        super.die();

        emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );

        if (visible) {
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
        }
    }
}