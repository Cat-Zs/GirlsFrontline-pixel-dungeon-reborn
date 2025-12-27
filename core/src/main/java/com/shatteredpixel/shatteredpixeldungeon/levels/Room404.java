package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Trigger;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Bundle;

public class Room404 extends Level {
    private static final int SIZE = 10;
    private static final int WIDTH = SIZE;
    private static final int HEIGHT = SIZE;
    private static final int TEMP_MIN = 1;
    private static final int TEMP_MAX = SIZE - 2;

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_ZERO_LEVEL;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    public static class UpStairsTrigger extends Trigger {
        @Override
        public boolean canInteract(Char ch) {
            return Dungeon.hero == ch && Dungeon.level.adjacent(pos, ch.pos);
        }

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

        public Trigger create(int pos) {
            this.pos = pos;
            return this;
        }
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);
        
        // 使用硬编码的地图
        map = MAP.clone();

        buildFlagMaps();
        cleanWalls();

        int center = (TEMP_MAX + TEMP_MIN) / 2;
        entrance = center * width() + center;
        exit = center * width() + center;

        // 添加向上的楼梯（连接回ZeroLevelSub）
        int stairsUp = (SIZE - 1) * width() + (SIZE - 3);
        placeTrigger(new UpStairsTrigger().create(stairsUp));

        CustomTilemap customBottomTile = new CustomBottomTile();
        customBottomTile.setRect(0, 0, width(), height());
        customTiles.add(customBottomTile);

        return true;
    }

    public static class CustomBottomTile extends CustomTilemap {
        {
            texture = Assets.Environment.ROOM;
            tileW = SIZE;
            tileH = SIZE;
        }

        @Override
        public Tilemap create() {
            super.create();
            if (vis != null) {
                // 使用mapSimpleImage方法，将texW参数设置为240（10*24）
                // 这样每个格子会使用room.png中对应的24*24像素贴图
                int[] data = mapSimpleImage(0, 0, 240);
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

    // 初始化locked变量为true以禁用饥饿值增加
    {
        locked = true;
    }

    // 重写updateFieldOfView方法，实现永久视野
    @Override
    public void updateFieldOfView(Char c, boolean[] fieldOfView) {
        // 对于404房间，设置所有单元格为可见
        for (int i = 0; i < fieldOfView.length; i++) {
            fieldOfView[i] = true;
        }

        // 同时将所有单元格标记为已映射和已访问，确保完全可见
        if (mapped != null) {
            for (int i = 0; i < mapped.length; i++) {
                mapped[i] = true;
            }
        }
        if (visited != null) {
            for (int i = 0; i < visited.length; i++) {
                visited[i] = true;
            }
        }
    }
    
    // 地形类型常量
    private static final int W = Terrain.WALL;
    private static final int e = Terrain.EMPTY;
    private static final int X = Terrain.EXIT;
    
    // 硬编码的地图数组
    // 10x10的房间，四周是墙，内部是空地，右下角有一个向上的楼梯
    private static final int[] MAP = {
        W, W, W, W, W, W, W, W, W, W,
        W, W, W, W, W, W, W, W, W, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, W,
        W, W, W, e, e, e, e, X, W, W
    };
}