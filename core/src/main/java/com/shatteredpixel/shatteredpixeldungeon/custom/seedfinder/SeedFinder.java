package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmoredStatue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.CrystalMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GoldenMimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mimic;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Statue;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Ghost.Quest;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Wandmaker;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.EnergyCrystal;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap.Type;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.CrystalKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.GoldenKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CeremonialCandle;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.CorpseDust;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Embers;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.Pickaxe;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.levels.CityBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.DungeonSeed;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SeedFinder {
    public static FINDING findingStatus;
    List<Class<? extends Item>> blacklist;
    ArrayList<String> itemList;
    public static boolean SeedFinding = false;

    // 新增：接收Scene引用，用于无间隔刷新
    private SeedFindScene sceneRef;

    private void addTextItems(String caption, ArrayList<HeapItem> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for(HeapItem item : items) {
                Item i = item.item;
                Heap h = item.heap;
                if ((i instanceof Armor && ((Armor)i).hasGoodGlyph() || i instanceof Weapon && ((Weapon)i).hasGoodEnchant() || i instanceof Ring || i instanceof Wand) && i.cursed) {
                    builder.append("- " + Messages.get(this, "cursed")).append(i.toString().toLowerCase());
                } else {
                    builder.append("- ").append(i.toString().toLowerCase());
                }
                if (h.type != Type.HEAP) {
                    String heap = h.toString();
                    if (h.type == Type.FOR_SALE)
                        heap = Shopkeeper.sellPrice(h.peek())+"钻石";
                    builder.append(" ("+heap.toLowerCase()+")");
                }
                builder.append("\n");
            }
            builder.append("\n");
        }
    }

    private void addTextQuest(String caption, ArrayList<Item> items, StringBuilder builder) {
        if (!items.isEmpty()) {
            builder.append(caption).append(":\n");
            for(Item i : items) {
                if (i.cursed) {
                    builder.append("- " + Messages.get(this, "cursed")).append(i.toString().toLowerCase()).append("\n");
                } else {
                    builder.append("- ").append(i.toString().toLowerCase()).append("\n");
                }
            }
            builder.append("\n");
        }
    }

    // 重载findSeed方法，接收Scene引用（无间隔）
    public String findSeed(ArrayList<String> wanted, int floor, int Challenges, HeroClass heroclass, SeedFindScene scene) {
        this.sceneRef = scene; // 保存Scene引用
        return findSeed(wanted, floor, Challenges, heroclass);
    }

    public String checkSeed(String seedCode, HeroClass heroclass){
        seedCode = DungeonSeed.formatText(seedCode);
        long seedNum = DungeonSeed.convertFromText(seedCode);
        SeedFinding = true;
        String text = this.logSeedItems(seedNum, 31, SPDSettings.challenges(), heroclass);
        SeedFinding = false;
        return text;
    }
    public String findSeed(ArrayList<String> wanted, int floor, int Challenges, HeroClass heroclass) {
        String result = "NONE";
        SeedFinding = true;
        this.itemList = wanted;
        long seedDigits = DungeonSeed.randomSeed();
        if (seedDigits>200000)
            seedDigits-=100000;
        findingStatus = SeedFinder.FINDING.CONTINUE;
        SeedFinder.Options.condition = SeedFinder.Condition.ALL;

        // 无间隔遍历种子（移除所有sleep）
        for(int i = Random.Int(9999999); (long)i < DungeonSeed.TOTAL_SEEDS && findingStatus == SeedFinder.FINDING.CONTINUE; ++i) {
            long currentSeed = seedDigits + i;

            // 无间隔刷新UI（直接回调，无延迟）
            if (sceneRef != null) {
                sceneRef.updateCurrentSeed(currentSeed);
            }

            if (this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)
                    &&this.testSeedALL(currentSeed, floor, heroclass)) {
                result = this.logSeedItems(currentSeed, floor, Challenges, heroclass);
                break;
            }

            // 仅检测线程停止，无间隔
            if (Thread.currentThread().isInterrupted() || !SeedFinding) {
                findingStatus = SeedFinder.FINDING.STOP;
                break;
            }
        }
        SeedFinding = false;
        return result;
    }

    private ArrayList<Heap> getMobDrops(Level l) {
        ArrayList<Heap> heaps = new ArrayList<>();
        for(Mob m : l.mobs) {
            if (m instanceof Statue && !(m instanceof ArmoredStatue)) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((Statue)m).weapon().identify());
                h.type = Type.HEAP;
                heaps.add(h);
            } else if (m instanceof ArmoredStatue) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                h.items.add(((ArmoredStatue)m).armor().identify());
                h.items.add(((ArmoredStatue)m).weapon().identify());
                h.type = Type.HEAP;
                heaps.add(h);
            } else if (m instanceof Mimic) {
                Heap h = new Heap();
                h.items = new LinkedList<>();
                for(Item item : ((Mimic)m).items) {
                    h.items.add(item.identify());
                }
                if (m instanceof GoldenMimic) {
                    h.type = Type.HEAP;
                } else if (m instanceof CrystalMimic) {
                    h.type = Type.HEAP;
                } else {
                    h.type = Type.HEAP;
                }
                heaps.add(h);
            }
        }
        return heaps;
    }

    private boolean testSeedALL(long seed, int floors, HeroClass heroclass) {
        Dungeon.hero = null;
        GamesInProgress.selectedClass = heroclass;
        String seedCode = DungeonSeed.convertToCode(seed);
        if (Game.lockXMAS) {
            Dungeon.init(seedCode);
            Game.lockXMAS = true;
        }else
            Dungeon.init(seedCode);
        boolean[] itemsFound = new boolean[this.itemList.size()];
        Arrays.fill(itemsFound, false);
        int LevelSub = 0;
        for(int i = 1; i <= floors;) {
            Level l;
            if (i==25&&LevelSub==0){
                l = Dungeon.newLevel(i);
                LevelSub = 1;
            }
            else if (i==25&&LevelSub==1){
                l = Dungeon.newLevel(LevelSub*1000+i);
                i++;
                LevelSub=0;
            }
            else {
                l = Dungeon.newLevel(i);
                i++;
            }
            if (l instanceof CityBossLevel)
                ((CityBossLevel) l).spawnShop();
            ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
            heaps.addAll(this.getMobDrops(l));
            if (Quest.armor != null) {
                for(int j = 0; j < this.itemList.size(); ++j) {
                    String wantingItem = this.itemList.get(j);
                    boolean precise = wantingItem.startsWith("\"") && wantingItem.endsWith("\"");
                    if (precise) {
                        wantingItem = wantingItem.replaceAll(" ", "");
                    } else {
                        wantingItem = wantingItem.replaceAll("\"", "");
                    }
                    if ((!precise && Quest.armor.identify().toString().toLowerCase().replaceAll(" ", "").contains(wantingItem) || precise && Quest.armor.identify().toString().toLowerCase().equals(wantingItem)) && !itemsFound[j]) {
                        itemsFound[j] = true;
                        break;
                    }
                }
            }
            if (Wandmaker.Quest.wand1 != null) {
                for(int j = 0; j < this.itemList.size(); ++j) {
                    String wantingItem = this.itemList.get(j);
                    String wand1 = Wandmaker.Quest.wand1.identify().toString().toLowerCase();
                    String wand2 = Wandmaker.Quest.wand2.identify().toString().toLowerCase();
                    boolean precise = wantingItem.startsWith("\"") && wantingItem.endsWith("\"");
                    if (precise) {
                        wantingItem = wantingItem.replaceAll("\"", "");
                        if ((wand1.equals(wantingItem) || wand2.equals(wantingItem)) && !itemsFound[j]) {
                            itemsFound[j] = true;
                            break;
                        }
                    } else {
                        wantingItem = wantingItem.replaceAll(" ", "");
                        wand1 = wand1.replaceAll(" ", "");
                        wand2 = wand2.replaceAll(" ", "");
                        if ((wand1.contains(wantingItem) || wand2.contains(wantingItem)) && !itemsFound[j]) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                    if (Wandmaker.Quest.type() == 1 && Messages.get(this, "corpsedust", new Object[0]).contains(wantingItem.replaceAll(" ", ""))) {
                        if (!itemsFound[j]) {
                            itemsFound[j] = true;
                            break;
                        }
                    } else if (Wandmaker.Quest.type() == 2 && Messages.get(this, "embers", new Object[0]).contains(wantingItem.replaceAll(" ", ""))) {
                        if (!itemsFound[j]) {
                            itemsFound[j] = true;
                            break;
                        }
                    } else if (Wandmaker.Quest.type() == 3 && Messages.get(this, "rotberry", new Object[0]).contains(wantingItem.replaceAll(" ", "")) && !itemsFound[j]) {
                        itemsFound[j] = true;
                        break;
                    }
                }
            }
            if (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp.Quest.reward != null) {
                for(int j = 0; j < this.itemList.size(); ++j) {
                    String wantingItem = this.itemList.get(j);
                    boolean precise = wantingItem.startsWith("\"") && wantingItem.endsWith("\"");
                    String ring = com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp.Quest.reward.identify().toString().toLowerCase();
                    if ((!precise && ring.replaceAll(" ", "").contains(wantingItem.replaceAll(" ", "")) || precise && ring.equals(wantingItem)) && !itemsFound[j]) {
                        itemsFound[j] = true;
                        break;
                    }
                }
            }
            for(Heap h : heaps) {
                for(Item item : h.items) {
                    item.identify();
                    String itemName = item.toString().toLowerCase();
                    for(int j = 0; j < this.itemList.size(); ++j) {
                        String wantingItem = this.itemList.get(j);
                        boolean precise = wantingItem.startsWith("\"") && wantingItem.endsWith("\"");
                        if ((!precise && itemName.replaceAll(" ", "").contains(wantingItem.replaceAll(" ", "")) || precise && itemName.equals(wantingItem.replaceAll("\"", ""))) && !itemsFound[j]) {
                            itemsFound[j] = true;
                            break;
                        }
                    }
                }
            }
            if (areAllTrue(itemsFound)) {
                return true;
            }
        }
        return false;
    }

    private static boolean areAllTrue(boolean[] array) {
        for(boolean b : array) {
            if (!b) {
                return false;
            }
        }
        return true;
    }

    public String logSeedItems(long seed, int floors, int Challenges, HeroClass heroclass) {
        String seedCode = DungeonSeed.convertToCode(seed);
        SeedFindScene.seedCode= seedCode;
        Dungeon.hero = null;
        GamesInProgress.selectedClass = heroclass;
        if (Game.lockXMAS) {
            Dungeon.init(seedCode);
            Game.lockXMAS = true;
        }else
            Dungeon.init(seedCode);
        StringBuilder result = new StringBuilder(Messages.get(this, "seed") + seedCode + " (" + seed + ") " + Messages.get(this, "items") + ":\n\n");
        this.blacklist = Arrays.asList(Dewdrop.class, IronKey.class, GoldenKey.class, CrystalKey.class, EnergyCrystal.class, CorpseDust.class, Embers.class, CeremonialCandle.class, Pickaxe.class);
        int LevelSub = 0;
        for(int i = 1; i <= floors;) {
            Level l;
            if (i==25&&LevelSub==0){
                l = Dungeon.newLevel(i);
                LevelSub = 1;
            }
            else if (i==25&&LevelSub==1){
                l = Dungeon.newLevel(LevelSub*1000+i);
                i++;
                LevelSub=0;
            }
            else {
                l = Dungeon.newLevel(i);
                i++;
            }
            if (l instanceof CityBossLevel)
                ((CityBossLevel) l).spawnShop();
            result.append("\n_----- ").append((long) Dungeon.depth).append(" ").append(Messages.get(this, "floor") + " -----_\n\n");
            ArrayList<Heap> heaps = new ArrayList<>(l.heaps.valueList());
            StringBuilder builder = new StringBuilder();
            ArrayList<HeapItem> scrolls = new ArrayList<>();
            ArrayList<HeapItem> potions = new ArrayList<>();
            ArrayList<HeapItem> equipment = new ArrayList<>();
            ArrayList<HeapItem> rings = new ArrayList<>();
            ArrayList<HeapItem> artifacts = new ArrayList<>();
            ArrayList<HeapItem> wands = new ArrayList<>();
            ArrayList<HeapItem> others = new ArrayList<>();
            ArrayList<HeapItem> forSales = new ArrayList<>();
            if (Quest.armor != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Quest.armor.identify());
                rewards.add(Quest.weapon.identify());
                Quest.complete();
                this.addTextQuest("[ " + Messages.get(this, "sad_ghost_reward") + " ]", rewards, builder);
            }
            if (Wandmaker.Quest.wand1 != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(Wandmaker.Quest.wand1.identify());
                rewards.add(Wandmaker.Quest.wand2.identify());
                Wandmaker.Quest.complete();
                builder.append("[ " + Messages.get(this, "wandmaker_need") + " ]:\n ");
                switch (Wandmaker.Quest.type()) {
                    case 1:
                    default:
                        builder.append(Messages.get(this, "corpsedust") + "\n\n");
                        break;
                    case 2:
                        builder.append(Messages.get(this, "embers") + "\n\n");
                        break;
                    case 3:
                        builder.append(Messages.get(this, "rotberry") + "\n\n");
                }
                this.addTextQuest("[ " + Messages.get(this, "wandmaker_reward") + " ]", rewards, builder);
            }
            if (com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp.Quest.reward != null) {
                ArrayList<Item> rewards = new ArrayList<>();
                rewards.add(com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp.Quest.reward.identify());
                com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.Imp.Quest.complete();
                this.addTextQuest("[ " + Messages.get(this, "imp_reward") + " ]", rewards, builder);
            }
            heaps.addAll(this.getMobDrops(l));
            int gold = 0;
            for(Heap h : heaps) {
                for(Item item : h.items) {
                    item.identify();
                    if (h.type == Type.FOR_SALE) {
                        forSales.add(new HeapItem(item, h));
                    } else if (!this.blacklist.contains(item.getClass())) {
                        if (item instanceof Scroll) {
                            scrolls.add(new HeapItem(item, h));
                        } else if (item instanceof Potion) {
                            potions.add(new HeapItem(item, h));
                        } else if (!(item instanceof MeleeWeapon) && !(item instanceof Armor)) {
                            if (item instanceof Ring) {
                                rings.add(new HeapItem(item, h));
                            } else if (item instanceof Artifact) {
                                artifacts.add(new HeapItem(item, h));
                            } else if (item instanceof Wand) {
                                wands.add(new HeapItem(item, h));
                            } else if (item instanceof Gold){
                                gold+=item.quantity();
                            }else {
                                others.add(new HeapItem(item, h));
                            }
                        } else {
                            equipment.add(new HeapItem(item, h));
                        }
                    }
                }
            }
            if (gold!=0){
                Gold goldA = new Gold(gold);
                Heap heapA = new Heap();
                heapA.items = new LinkedList<>();
                heapA.items.add(goldA);
                others.add(new HeapItem(goldA, heapA));
            }
            this.addTextItems("[ " + Messages.get(this, "scrolls") + " ]", scrolls, builder);
            this.addTextItems("[ " + Messages.get(this, "potions") + " ]", potions, builder);
            this.addTextItems("[ " + Messages.get(this, "equipment") + " ]", equipment, builder);
            this.addTextItems("[ " + Messages.get(this, "rings") + " ]", rings, builder);
            this.addTextItems("[ " + Messages.get(this, "artifacts") + " ]", artifacts, builder);
            this.addTextItems("[ " + Messages.get(this, "wands") + " ]", wands, builder);
            this.addTextItems("[ " + Messages.get(this, "for_sales") + " ]", forSales, builder);
            this.addTextItems("[ " + Messages.get(this, "others") + " ]", others, builder);
            result.append(builder);
        }
        return result.toString();
    }

    // 补充缺失的内部类/枚举（避免编译错误）
    public enum FINDING { CONTINUE, STOP }
    public static class Options { public static Condition condition; }
    public enum Condition { ALL }
    public static class HeapItem {
        public Item item;
        public Heap heap;
        public HeapItem(Item item, Heap heap) {
            this.item = item;
            this.heap = heap;
        }
    }
}