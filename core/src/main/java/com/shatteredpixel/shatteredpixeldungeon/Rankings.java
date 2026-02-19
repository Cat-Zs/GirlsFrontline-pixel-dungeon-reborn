/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public enum Rankings {
	
	INSTANCE;
	
	public static final int TABLE_SIZE	= 30;
	
	public static final String RANKINGS_FILE = "rankings.dat";
	
	public ArrayList<Record> records;
	public int lastRecord;
	public int totalNumber;
	public int wonNumber;
    public int LockNumber;

	public void submit( boolean win, Class cause ) {

        for (Item i :Dungeon.hero.belongings){
            if (i.buffedLvl()!=i.level()) {
                int lvlPoint = i.buffedLvl() - i.level();
                i.BuffLevelPoint = lvlPoint;
            }
        }

		if(Dungeon.isChallenged(Challenges.TEST_MODE)){
			return;
		}

		load();
		
		Record rec = new Record();
		
		rec.cause     = cause;
		rec.win       = win;
		rec.heroClass = Dungeon.hero.heroClass;
		rec.armorTier = Dungeon.hero.tier();
		rec.herolevel = Dungeon.hero.lvl;
		rec.seed      = Dungeon.seed;
		// 检查是否使用了自定义种子
		rec.customSeed = !Dungeon.customSeedText.isEmpty();
		rec.depth     = Dungeon.curDepth();
		rec.score     = score( win );
		rec.isLock    = false;
        //默认不锁定
		INSTANCE.saveGameData(rec);

		rec.gameID = UUID.randomUUID().toString();
		
		records.add( rec );
		
		Collections.sort( records, scoreComparator );
		
		lastRecord = records.indexOf( rec );
        //最后一次存档的序号
		int size = records.size();
		while (size > TABLE_SIZE+LockNumber) {
            int rem = size-1;
			if (lastRecord == size - 1) {
                //最后一次在存档末尾时
                rem--;
                //将要删除倒数第二位
				lastRecord--;
                //最后一次存档序号提前
			}
            while (rem>=0){
                if (records.get(rem).isLock) {
                    //所要删除的存档是锁定的
                    rem--;
                    //跳过这一位判断下一位
                    continue;
                }
                records.remove(rem);
                break;
            }
			size = records.size();
            //更新存档数量
		}
		
		totalNumber++;
		if (win) {
			wonNumber++;
		}

		Badges.validateGamesPlayed();
		
		save();
	}

	private int score( boolean win ) {
		return (Statistics.goldCollected + Dungeon.hero.lvl * (win ? 26 : Dungeon.curDepth() ) * 100) * (win ? 2 : 1);
	}

	public static final String HERO = "hero";
	public static final String STATS = "stats";
	public static final String BADGES = "badges";
	public static final String HANDLERS = "handlers";
	public static final String CHALLENGES = "challenges";

	public void saveGameData(Record rec){
		rec.gameData = new Bundle();

		Belongings belongings = Dungeon.hero.belongings;

		//save the hero and belongings
		ArrayList<Item> allItems = (ArrayList<Item>) belongings.backpack.items.clone();
		//remove items that won't show up in the rankings screen
		for (Item item : belongings.backpack.items.toArray( new Item[0])) {
			if (item instanceof Bag){
				for (Item bagItem : ((Bag) item).items.toArray( new Item[0])){
					if (Dungeon.quickslot.contains(bagItem)) belongings.backpack.items.add(bagItem);
				}
			}
			if (!Dungeon.quickslot.contains(item)) {
				belongings.backpack.items.remove(item);
			}
		}

		//remove all buffs (ones tied to equipment will be re-applied)
		for(Buff b : Dungeon.hero.buffs()){
			Dungeon.hero.remove(b);
		}

		rec.gameData.put( HERO, Dungeon.hero );

		//save stats
		Bundle stats = new Bundle();
		Statistics.storeInBundle(stats);
		rec.gameData.put( STATS, stats);

		//save badges
		Bundle badges = new Bundle();
		Badges.saveLocal(badges);
		rec.gameData.put( BADGES, badges);

		//save handler information
		Bundle handler = new Bundle();
		Scroll.saveSelectively(handler, belongings.backpack.items);
		Potion.saveSelectively(handler, belongings.backpack.items);
		//include potentially worn rings
		if (belongings.misc != null)        belongings.backpack.items.add(belongings.misc);
		if (belongings.ring != null)        belongings.backpack.items.add(belongings.ring);
		Ring.saveSelectively(handler, belongings.backpack.items);
		rec.gameData.put( HANDLERS, handler);

		//restore items now that we're done saving
		belongings.backpack.items = allItems;
		
		//save challenges
		rec.gameData.put( CHALLENGES, Dungeon.challenges );
	}

	public void loadGameData(Record rec){
		Bundle data = rec.gameData;

		Actor.clear();
		Dungeon.hero  = null;
		Dungeon.seed  = rec.seed;
		Dungeon.level = null;
		Generator.fullReset();
		Notes.reset();
		Dungeon.quickslot.reset();
		QuickSlotButton.reset();

		Bundle handler = data.getBundle(HANDLERS);
		Scroll.restore(handler);
		Potion.restore(handler);
		Ring.restore(handler);

		Badges.loadLocal(data.getBundle(BADGES));

		Dungeon.hero = (Hero)data.get(HERO);

		Statistics.restoreFromBundle(data.getBundle(STATS));
		
		Dungeon.challenges = data.getInt(CHALLENGES);

	}
	
	private static final String RECORDS	= "records";
	private static final String LATEST	= "latest";
	private static final String TOTAL	= "total";
	private static final String WON     = "won";
    private static final String LOCKNUM     = "locknum";

	public void save() {
		Bundle bundle = new Bundle();
		bundle.put( RECORDS, records );
		bundle.put( LATEST, lastRecord );
		bundle.put( TOTAL, totalNumber );
		bundle.put( WON, wonNumber );
        bundle.put( LOCKNUM, LockNumber );
		try {
			FileUtils.bundleToFile( RANKINGS_FILE, bundle);
		} catch (IOException e) {
			GirlsFrontlinePixelDungeon.reportException(e);
		}

	}
	
	public void load() {
		
		if (records != null) {
			return;
		}
		
		records = new ArrayList<>();
		
		try {
			Bundle bundle = FileUtils.bundleFromFile( RANKINGS_FILE );
			
			for (Bundlable record : bundle.getCollection( RECORDS )) {
				records.add( (Record)record );
			}
			lastRecord = bundle.getInt( LATEST );
			LockNumber = bundle.getInt( LOCKNUM );
			totalNumber = bundle.getInt( TOTAL );
			if (totalNumber == 0) {
				totalNumber = records.size();
			}

			wonNumber = bundle.getInt( WON );
			if (wonNumber == 0) {
				for (Record rec : records) {
					if (rec.win) {
						wonNumber++;
					}
				}
			}

		} catch (IOException e) {
		}
	}
	
	public static class Record implements Bundlable {

		private static final String CAUSE   = "cause";
		private static final String WIN		= "win";
		private static final String SCORE	= "score";
		private static final String CLASS	= "class";
		private static final String TIER	= "tier";
		private static final String LEVEL	= "level";
		private static final String SEED	= "seed";
		private static final String CUSTOM_SEED = "custom_seed";
		private static final String DEPTH	= "depth";
		private static final String DATA	= "gameData";
		private static final String ID      = "gameID";
        private static final String isLOCK      = "gameLOCK";

		public Class cause;
		public boolean win;
		public HeroClass heroClass;
		public int armorTier;
		public int herolevel;
		public long seed;
		public boolean customSeed; // 是否使用了自定义种子
		public int depth;
		
		public Bundle gameData;
		public String gameID;

		public int score;
        public boolean isLock;

		public String desc(){
			if (cause == null) {
				return Messages.get(this, "something");
			} else {
				String result = Messages.get(cause, "rankings_desc", (Messages.get(cause, "name")));
				if (result.contains("!!!NO TEXT FOUND!!!")){
					return Messages.get(this, "something");
				} else {
					return result;
				}
			}
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			
			if (bundle.contains( CAUSE )) {
				cause   = bundle.getClass( CAUSE );
			} else {
				cause = null;
			}
			
			win		= bundle.getBoolean( WIN );
			score	= bundle.getInt( SCORE );
			isLock    = bundle.getBoolean( isLOCK );
			heroClass	= bundle.getEnum( CLASS, HeroClass.class );
			armorTier	= bundle.getInt( TIER );
			
			if (bundle.contains(DATA))  gameData = bundle.getBundle(DATA);
			if (bundle.contains(ID))   gameID = bundle.getString(ID);
			
			if (gameID == null) gameID = UUID.randomUUID().toString();

			seed=bundle.getLong(SEED);
			customSeed = bundle.contains(CUSTOM_SEED) ? bundle.getBoolean(CUSTOM_SEED) : false;
			depth = bundle.getInt( DEPTH );
			herolevel = bundle.getInt( LEVEL );

		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			
			if (cause != null) bundle.put( CAUSE, cause );

			bundle.put( WIN, win );
			bundle.put( SCORE, score );
			bundle.put( isLOCK, isLock);
			bundle.put( CLASS, heroClass );
			bundle.put( TIER, armorTier );
			bundle.put( LEVEL, herolevel );
			bundle.put( SEED, seed );
			bundle.put( CUSTOM_SEED, customSeed );
			bundle.put( DEPTH, depth );
			
			if (gameData != null) bundle.put( DATA, gameData );
			bundle.put( ID, gameID );
		}
	}

	private static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
		@Override
		public int compare( Record lhs, Record rhs ) {
			int result = (int)Math.signum( rhs.score - lhs.score );
			if (result == 0) {
				return (int)Math.signum( rhs.gameID.hashCode() - lhs.gameID.hashCode());
			} else{
				return result;
			}
		}
	};
}
