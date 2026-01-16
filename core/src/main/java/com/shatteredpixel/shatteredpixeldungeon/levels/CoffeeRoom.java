package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.ZeroLevelSub;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;

public class CoffeeRoom extends Level {
    private static final int WIDTH = 24;   // 房间的宽度（24个瓦片）
    private static final int HEIGHT = 24;  // 房间的高度（24个瓦片）
    private static final int TEMP_MIN = 2;

    // 定义了传送触发器的位置（左上角）
    public static final int toZeroLevelSub = TEMP_MIN * WIDTH + TEMP_MIN;

    // 地形类型常量
    private static final int W = Terrain.WALL;
    private static final int e = Terrain.EMPTY;
    private static final int D = Terrain.DOOR;

    // 硬编码的地图数组（24x24）
    private static final int[] MAP = {
        W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, D, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
        W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
    };

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
        setSize(WIDTH, HEIGHT);

        // 使用硬编码的地图数组
        map = MAP.clone();

        entrance = toZeroLevelSub;
        exit = entrance;

        // 添加向上的楼梯(连接回ZeroLevelSub)
        placeTrigger(new Teleporter().create(toZeroLevelSub, ZeroLevelSub.toCoffeeRoom, 1000));

        // 添加底部覆盖贴图
        CustomTilemap customBottomTile = new CustomBottomTile();
        customBottomTile.setRect(0, 0, width(), height());
        customTiles.add(customBottomTile);
        
        // 添加墙体覆盖贴图
        CustomTilemap customWallTile = new CustomWallTile();
        customWallTile.setRect(0, 0, width(), height());
        customWalls.add(customWallTile);

        return true;
    }

    public static class CustomBottomTile extends CustomTilemap {
        {
            texture = Assets.Environment.COFFEE_ROOM;
            tileW = HEIGHT;
            tileH = WIDTH;
        }

        @Override
        public com.watabou.noosa.Tilemap create() {
            super.create();
            if (vis != null) {
                // 使用mapSimpleImage方法，将texW参数设置为24*24=576
                // 这样每个格子会使用CoffeeRoom.png中对应的24*24像素贴图
                int[] data = mapSimpleImage(0, 0, 576);
                vis.map(data, tileW);
            }
            return vis;
        }
    }
    
    public static class CustomWallTile extends CustomTilemap {
        {
            texture = Assets.Environment.COFFEE_ROOM_1;
            tileW = HEIGHT;
            tileH = WIDTH;
        }

        @Override
        public com.watabou.noosa.Tilemap create() {
            super.create();
            if (vis != null) {
                // 使用mapSimpleImage方法，将texW参数设置为24*24=576
                // 这样每个格子会使用CoffeeRoom.png中对应的24*24像素贴图
                int[] data = mapSimpleImage(0, 0, 576);
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
        // 对于CoffeeRoom，设置所有单元格为可见
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
}