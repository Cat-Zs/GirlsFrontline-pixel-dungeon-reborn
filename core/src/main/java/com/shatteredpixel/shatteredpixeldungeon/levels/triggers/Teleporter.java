package com.shatteredpixel.shatteredpixeldungeon.levels.triggers;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.utils.Bundle;
import com.watabou.noosa.Game;

public class Teleporter extends Trigger{
	public int targetPos;
	public int targetLevelDepth;
    public int sonId;

	private static final String TARGET_POS     ="target_pos";
	private static final String TARGET_LEVEL_DEPTH ="target_level_depth";
    private static final String SON_ID="son_id";

	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put(TARGET_POS     ,targetPos    );
		bundle.put(TARGET_LEVEL_DEPTH, targetLevelDepth);
        bundle.put(SON_ID,sonId);
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle){
		super.restoreFromBundle(bundle);
		targetPos    =bundle.getInt(TARGET_POS     );
		targetLevelDepth =bundle.getInt(TARGET_LEVEL_DEPTH);
        sonId=bundle.getInt(SON_ID);
	}

	public Teleporter create(int pos,int targetPos,int targetLevelDepth, int sonId){
		this.pos=pos;
		this.targetPos=targetPos;
		this.targetLevelDepth =targetLevelDepth;
        this.sonId=sonId;
		return this;
	}
	
	@Override
	public void activate(Char ch){
		if(Dungeon.hero==ch){
			InterlevelScene.accessPos=targetPos;
			InterlevelScene.accessLevelDepth = targetLevelDepth;
            InterlevelScene.sonId=sonId;
			InterlevelScene.mode=InterlevelScene.Mode.ACCESS;
			Game.switchScene(InterlevelScene.class);
		}
	}
}