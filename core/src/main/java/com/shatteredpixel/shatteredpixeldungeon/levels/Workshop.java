package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.levels.CoffeeRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;

public class Workshop extends Level {
    private static final int WIDTH = 12;   // 房间的宽度（12个瓦片）
    private static final int HEIGHT = 22;  // 房间的高度（12个瓦片）
    private static final int TEMP_MIN = 2;

    // 定义了传送触发器的位置（左上角）
    public static final int toCoffeeRoom = TEMP_MIN * WIDTH + TEMP_MIN;

    // 地形类型常量
    private static final int W = Terrain.WALL;
    private static final int e = Terrain.EMPTY;
    private static final int D = Terrain.DOOR;
    private static final int Z = Terrain.SIGN;
    
    // 硬编码的地图数组（12x22）
    private static final int[] MAP = {
        W, W, W, W, W, W, W, W, W, W, W, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, D, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, W, e, W, W, W, W, W, W, W, W, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, e, e, e, e, e, e, e, e, e, e, W,
        W, W, W, W, W, W, W, W, W, W, W, W
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

        entrance = toCoffeeRoom;
        exit = entrance;

        // 添加向上的楼梯(连接回CoffeeRoom)
        placeTrigger(new Teleporter().create(toCoffeeRoom, CoffeeRoom.toWorkshop, 0, 3));

        return true;
    }

    // 获取地块的描述
    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.SIGN:
                return "";
            default:
                return super.tileDesc( tile );
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
        // 对于Workshop，设置所有单元格为可见
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