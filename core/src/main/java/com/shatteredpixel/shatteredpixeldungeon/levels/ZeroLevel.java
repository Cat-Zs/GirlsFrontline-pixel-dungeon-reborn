package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.SceneSwitcher;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.WindowTrigger;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import java.io.IOException;

public class ZeroLevel extends Level {
	private static final int SIZE = 17;
	private static final int TEMP_MIN = 2;
	private static final int TEMP_MAX = SIZE-3;

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_ZERO_LEVEL;
	}
	
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}
	
	@Override
	protected boolean build() {
		setSize(SIZE, SIZE);

		for(int i=1;i<SIZE-1;i++){
			for(int j=1;j<SIZE-1;j++){
				map[i*width()+j]=Terrain.EMPTY;
			}
		}
		
		int center=(TEMP_MAX+TEMP_MIN)/2;
		entrance  =center*width()+center;
		//map[entrance]=Terrain.ENTRANCE;
		exit      =center*width()+center;

		int title=(TEMP_MIN+2)*width()+TEMP_MIN   ;
		map[title]=Terrain.WATER;
		placeTrigger(new SceneSwitcher().create(title,TitleScene.class));

		int teleporter=title+2;
		map[teleporter]=Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter,-1,1000));

		int teleporter2=teleporter+2;
		map[teleporter2]=Terrain.DOOR;
		placeTrigger(new Teleporter().create(teleporter2,-1,1025));

		CustomTilemap customBottomTile=new CustomBottomTile();
		customBottomTile.setRect(0,0,width(),height());
		customTiles.add(customBottomTile);

		return true;
	}

	public static class CustomBottomTile extends CustomTilemap {
		{
			texture = Assets.Environment.ZERO_LEVEL;
		}

		@Override
		public Tilemap create() {
			super.create();
			if (vis != null){
				int[] data = new int[tileW*tileH];
				for (int i = 0; i < data.length; i++){
					data[i] = -1;
				}
				vis.map(data, tileW);
			}

			return vis;
		}
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}

	@Override
	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
	}
}