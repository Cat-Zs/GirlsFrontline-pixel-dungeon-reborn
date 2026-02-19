//
// Decompiled by Jadx - 571ms
//
package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NoelShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFinder;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.ShopRoom;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class NoelShopRoom extends ShopRoom {
    private boolean KeeperSpawned = false;
    public Room.Door entrance() {
        return connected.isEmpty() ? new Room.Door(left, top + 2) : super.entrance();
    }

    public int maxConnections(int i) {
        return 2;
    }

    public int maxHeight() {
        return 9;
    }

    public int maxWidth() {
        return 9;
    }

    public int minHeight() {
        return 9;
    }

    public int minWidth() {
        return 9;
    }

    public void onLevelLoad(Level level) {
        super.onLevelLoad(level);
        if (!openShop() || KeeperSpawned) {
            return;
        }
        KeeperSpawned = true;
        placeItems(level);
        placeShopkeeper(level);
    }

    public void paint(Level level) {
        Painter.fill(level, this, 4);
        Painter.fill(level, this, 1, 14);
        Painter.fill(level, this, 3, 29);
        for (Door door : connected.values()) {
            door.set(Door.Type.REGULAR);
        }
        if (Imp.Quest.isCompleted()) {
            KeeperSpawned = true;
            placeItems(level);
            placeShopkeeper(level);
            return;
        }
        KeeperSpawned = false;
    }

    public static boolean openShop(){
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)||Challenges.activeChallenges()>=2){
            return Dungeon.hero.buff(Elphelt.Finish.class)!=null|| SeedFinder.SeedFinding;
        }else
            return Imp.Quest.isCompleted()||SeedFinder.SeedFinding;
    }
    public void PlaceShop(Level level, int center, ArrayList<Integer> list){
        placeShopkeeper(level, center);
        placeItems( level, center, list );
    }
    protected void placeShopkeeper(Level level, int center) {
        Shopkeeper shopkeeper;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)||Challenges.activeChallenges()>=2){
            shopkeeper = new NoelShopkeeper();
        }else {
            shopkeeper = new ImpShopkeeper();
        }
        shopkeeper.pos = center;
        level.mobs.add(shopkeeper);
    }
    protected void placeShopkeeper(Level level) {
        int pointToCell = level.pointToCell(center());
        Shopkeeper shopkeeper;
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)||Challenges.activeChallenges()>=2){
            shopkeeper = new NoelShopkeeper();
        }else {
            shopkeeper = new ImpShopkeeper();
        }
        shopkeeper.pos = pointToCell;
        level.mobs.add(shopkeeper);
    }

    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        KeeperSpawned = bundle.getBoolean("keeper_spawned");
    }

    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put("keeper_spawned", KeeperSpawned);
    }
}
