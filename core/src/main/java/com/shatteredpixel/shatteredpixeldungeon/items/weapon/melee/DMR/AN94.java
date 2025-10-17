package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.DMR;

import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.Assets;

public class AN94 extends DesignatedMarksmanRifle {

	{
		image = ItemSpriteSheet.AN94;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 0.5f;

		tier = 4;
		RCH = 4;
		
	}

	@Override
	public int max(int lvl) {
		return  Math.round(5*(tier+1)) +        
				lvl*Math.round(1.6f*(tier+1));  
	}
}