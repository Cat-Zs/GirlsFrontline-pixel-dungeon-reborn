package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.RatKing;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.SceneSwitcher;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.WindowTrigger;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Trigger;
import com.shatteredpixel.shatteredpixeldungeon.scenes.TitleScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;
import java.io.IOException;

// 零层(ZeroLevel) - 游戏的起始房间或特殊房间
public class ZeroLevel extends Level {
	// 房间尺寸常量，宽16格，高10格
	private static final int SIZE = 16;
	// 临时最小和最大位置常量，用于定位房间内的元素
	private static final int TEMP_MIN = 2;
	private static final int TEMP_MAX = 9;

	// 获取零层的瓦片纹理
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_ZERO_LEVEL;
	}
	
	// 获取零层的水纹理
	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HALLS;
	}
	
	// 电脑触发器 - 用于切换到标题场景
	public static class ComputerTriger extends SceneSwitcher{
		// 检查角色是否可以与电脑交互
		@Override
		public boolean canInteract(Char ch){
			return Dungeon.hero==ch && Dungeon.level.adjacent(pos,ch.pos);
		}
	}

	// 向下楼梯触发器 - 用于进入特殊关卡
	public static class DownStairsTrigger extends Trigger {
		// 检查角色是否可以与楼梯交互
		@Override
		public boolean canInteract(Char ch) {
			return Dungeon.hero == ch && Dungeon.level.adjacent(pos, ch.pos);
		}

		// 激活楼梯，进入特殊关卡
		@Override
		public void activate(Char ch) {
			try {
				Dungeon.saveAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
			InterlevelScene.mode = InterlevelScene.Mode.ACCESS;
			InterlevelScene.accessLevelId = 1000;
			InterlevelScene.accessPos = -2;
			Game.switchScene(InterlevelScene.class);
		}

		// 创建触发器实例
		public Trigger create(int pos) {
			this.pos = pos;
			return this;
		}
	}

	// 构建零层房间
	@Override
	protected boolean build() {
		// 设置房间尺寸
		setSize(SIZE, 10);

		// 应用硬编码的地图数据
		map = MAP.clone();

		// 计算房间中心位置
		int center=(TEMP_MAX+TEMP_MIN)/2;
		// 设置入口和出口位置（均为中心）
		entrance  =center*width()+center;
		//map[entrance]=Terrain.ENTRANCE;
		exit      =center*width()+center;

		// 计算雕像位置
		int title=(TEMP_MIN+2)*width()+TEMP_MIN;
		//map[title]=Terrain.STATUE;

		// 创建并放置鼠王NPC
        RatKing king = new RatKing();
        king.pos = title+1;
        mobs.add( king );

		// 放置电脑触发器
		placeTrigger(new ComputerTriger().create(title,TitleScene.class));

		// 添加向下的楼梯触发器
		int stairsDown = (8) * width() + (13);
		//map[stairsDown] = Terrain.DOOR;
		placeTrigger(new DownStairsTrigger().create(stairsDown));

		//int teleporter=title+2;
		//map[teleporter]=Terrain.DOOR;
		//placeTrigger(new Teleporter().create(teleporter,-1,1000));

		//int teleporter2=teleporter+2;
		//map[teleporter2]=Terrain.DOOR;
		//placeTrigger(new Teleporter().create(teleporter2,-1,1025));

		// 创建并添加自定义底部瓦片
		CustomTilemap customBottomTile=new CustomBottomTile();
		customBottomTile.setRect(0,0,width(),height());
		customTiles.add(customBottomTile);

		return true;
	}

	// 定义地形常量，用于构建硬编码地图
	private static final int W = Terrain.WALL;      // 墙
	private static final int e = Terrain.EMPTY;     // 空地
	private static final int S = Terrain.STATUE;    // 雕像
	private static final int D = Terrain.DOOR;      // 门
	private static final int Z = Terrain.ZERO_WALL; // 空地和墙的组合
	private static final int C = Terrain.STATUE;    // 电脑（用于切换到标题场景的地块）

	// 硬编码的地图数据 (16x10)
	private static final int[] MAP = {
		W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
		W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
		W, e, e, Z, Z, Z, Z, Z, e, e, e, e, Z, Z, Z, W,
		W, e, e, e, e, e, e, e, e, e, e, e, e, Z, Z, W,
		W, e, C, e, e, e, e, e, e, e, e, e, e, Z, Z, W,
		W, e, e, e, Z, Z, Z, Z, Z, Z, Z, e, e, e, e, W,
		W, Z, Z, e, e, e, Z, Z, Z, e, e, e, e, e, e, W,
		W, Z, Z, e, e, e, e, e, e, e, e, e, e, e, W, W,
		W, W, W, W, W, W, W, W, W, W, W, W, W, e, W, W,
		W, W, W, W, W, W, W, W, W, W, W, W, W, D, W, W
	};

	// 自定义底部瓦片 - 用于绘制零层的特殊底部纹理
	public static class CustomBottomTile extends CustomTilemap {
		// 初始化纹理和尺寸
		{
			texture = Assets.Environment.FORWARD_CAMP;
			tileW = SIZE;
			tileH = 10;
		}

		// 创建瓦片映射
		@Override
		public Tilemap create() {
			super.create();
			if (vis != null){
				// 使用mapSimpleImage方法生成数据数组并应用到瓦片映射
				// texW参数设置为贴图的总宽度（像素）：16*24=384
				int[] data = mapSimpleImage(0, 0, 384);
				vis.map(data, tileW);
			}
			return vis;
		}
	}
	
	// 创建怪物（零层不自动生成怪物）
	@Override
	public Mob createMob() {
		return null;
	}
	
	// 创建怪物（零层仅手动添加特殊NPC）
	@Override
	protected void createMobs() {
	}

	// 添加重生点（零层不添加重生点）
	@Override
	public Actor addRespawner() {
		return null;
	}

	// 创建物品（零层不自动生成物品）
	@Override
	protected void createItems() {
	}
	
	// 初始化锁定变量为true，禁用饥饿值增加
	{
		locked = true;
	}
	
	// 重写视野更新方法，实现零层的永久视野
	@Override
	public void updateFieldOfView(Char c, boolean[] fieldOfView) {
		// 对于零层，设置所有单元格为可见
		for (int i = 0; i < fieldOfView.length; i++) {
			fieldOfView[i] = true;
		}
		
		// 同时将所有单元格标记为已映射，确保完全可见
		if (mapped != null) {
			for (int i = 0; i < mapped.length; i++) {
				mapped[i] = true;
			}
		}
	}
}