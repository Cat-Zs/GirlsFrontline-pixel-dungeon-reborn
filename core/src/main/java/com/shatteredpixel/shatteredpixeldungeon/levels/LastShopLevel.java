package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.ImpShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.NoelShopRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.triggers.Teleporter;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

public class LastShopLevel extends Level {
    {
        color1 = 0x4b6636;
        color2 = 0xf2f2f2;
        viewDistance = 8;
    }
    private static final int WIDTH = 35;
    private static final int HEIGHT = 45;
    public static final int entrance = 40*WIDTH+31;
    public static final int exit = 4*WIDTH+4;
    private static final int center = 21*WIDTH+16;
    private static final ArrayList<Integer> TradeItem = new ArrayList<>(Arrays.asList(
            18*WIDTH+13,18*WIDTH+14,18*WIDTH+15,18*WIDTH+16,18*WIDTH+17,18*WIDTH+18,18*WIDTH+19,
            19*WIDTH+19,20*WIDTH+19,21*WIDTH+19,22*WIDTH+19,23*WIDTH+19,24*WIDTH+19,24*WIDTH+18,
            24*WIDTH+17,24*WIDTH+16,24*WIDTH+15,24*WIDTH+14,24*WIDTH+13,23*WIDTH+13,22*WIDTH+13,
            21*WIDTH+13,20*WIDTH+13,19*WIDTH+13,19*WIDTH+14,19*WIDTH+15,19*WIDTH+16,19*WIDTH+17,
            19*WIDTH+18,20*WIDTH+18,21*WIDTH+18,22*WIDTH+18,23*WIDTH+18,23*WIDTH+17,23*WIDTH+16,
            23*WIDTH+15,23*WIDTH+14,22*WIDTH+14,21*WIDTH+14,20*WIDTH+14,20*WIDTH+15,20*WIDTH+16,
            20*WIDTH+17,21*WIDTH+17,22*WIDTH+17,22*WIDTH+16,22*WIDTH+15,21*WIDTH+15
            ));
    private final NoelShopRoom Keeper = new NoelShopRoom();
    private boolean open = false;

    @Override
    public String tilesTex() {
        return Dungeon.isChallenged(Challenges.STRONGER_BOSSES)||Challenges.activeChallenges()>=2?
                Assets.Environment.TILES_HALLS:
                Assets.Environment.TILES_CITY;
    }

    @Override
    public String waterTex() {
        return Dungeon.isChallenged(Challenges.STRONGER_BOSSES)||Challenges.activeChallenges()>=2?
                Assets.Environment.WATER_HALLS:
                Assets.Environment.WATER_CITY;
    }

    @Override
    protected boolean build() {
        setSize(WIDTH, HEIGHT);

        // 使用硬编码的地图
        map = MAP.clone();
        placeTrigger(new Teleporter().create(exit, -1, 26));
        placeTrigger(new Teleporter().create(entrance, -2, 25));
        buildFlagMaps();
        cleanWalls();
        if (NoelShopRoom.openShop()){
            Keeper.PlaceShop(this,center,TradeItem);
            open = true;
        }
        return true;
    }

    @Override
    public void playLevelMusic() {
        Music.INSTANCE.play(Assets.Music.CITY_BOSS, true);
    }
    private static final String OPEN_SHOP = "open_shop";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( OPEN_SHOP, open );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        open = bundle.getBoolean( OPEN_SHOP );
        if (!open&&NoelShopRoom.openShop()){
            Keeper.PlaceShop(this, center,TradeItem);
            open = true;
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

    // 地形类型常量
    private static final int W = Terrain.WALL;
    private static final int O = Terrain.WALL_DECO;
    private static final int E = Terrain.EMPTY;
    private static final int D = Terrain.EMPTY_DECO;
    private static final int Z = Terrain.ENTRANCE;
    private static final int Y = Terrain.EXIT;
    private static final int X = Terrain.DOOR;
    private static final int L = Terrain.WATER;
    private static final int C = Terrain.CHASM;
    private static final int B = Terrain.EMPTY_SP;

    // 硬编码的地图数组
    private static final int[] MAP = {
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,W,O,O,W,W,W,O,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,W,E,D,D,E,E,D,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,W,B,E,E,D,E,E,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,W,D,E,Y,D,D,E,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,W,E,D,E,E,E,E,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,O,W,W,W,W,W,X,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,B,B,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,B,B,B,B,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,X,W,W,W,W,W,W,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,B,B,B,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,B,B,B,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,L,L,L,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,L,L,L,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,L,L,L,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,B,B,B,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,B,B,B,B,B,B,B,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,W,W,W,W,O,W,W,X,W,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,B,B,B,B,B,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,B,B,B,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,W,B,C,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,B,B,B,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,B,C,C,C,C,C,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,X,W,W,W,W,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,B,E,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,E,E,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,D,Z,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,D,E,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,E,E,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,E,E,E,E,E,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,W,O,W,W,W,W,W,C,
            C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,C,

    };

}