package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLevitation;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfParalyticGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfPurity;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.Brew;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.Elixir;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.Runestone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class Recycle extends InventorySpell {

    {
        image = ItemSpriteSheet.RECYCLE;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return (item instanceof Potion && !(item instanceof Elixir || item instanceof Brew || item instanceof AlchemicalCatalyst)) ||
                item instanceof Scroll ||
                item instanceof Plant.Seed ||
                item instanceof Runestone ||
                item instanceof TippedDart;
    }
    private static final HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();
    static{
        scrollChances.put( ScrollOfIdentify.class,      3f );
        scrollChances.put( ScrollOfRemoveCurse.class,   2f );
        scrollChances.put( ScrollOfMagicMapping.class,  2f );
        scrollChances.put( ScrollOfMirrorImage.class,   2f );
        scrollChances.put( ScrollOfRecharging.class,    2f );
        scrollChances.put( ScrollOfLullaby.class,       2f );
        scrollChances.put( ScrollOfRetribution.class,   2f );
        scrollChances.put( ScrollOfRage.class,          2f );
        scrollChances.put( ScrollOfTeleportation.class, 2f );
        scrollChances.put( ScrollOfTerror.class,        2f );
        scrollChances.put( ScrollOfTransmutation.class, 1f );
    }
    private static final HashMap<Class<? extends Potion>, Float> potionChances = new HashMap<>();
    static{
        potionChances.put(PotionOfHealing.class,        3f);
        potionChances.put(PotionOfMindVision.class,     2f);
        potionChances.put(PotionOfFrost.class,          2f);
        potionChances.put(PotionOfLiquidFlame.class,    2f);
        potionChances.put(PotionOfToxicGas.class,       2f);
        potionChances.put(PotionOfHaste.class,          2f);
        potionChances.put(PotionOfInvisibility.class,   2f);
        potionChances.put(PotionOfLevitation.class,     2f);
        potionChances.put(PotionOfParalyticGas.class,   2f);
        potionChances.put(PotionOfPurity.class,         2f);
        potionChances.put(PotionOfExperience.class,     1f);
    }

    @Override
    protected void onItemSelected(Item item) {
        Item result;
        do {
            if (item instanceof Potion) {
                result = Reflection.newInstance(Random.chances(potionChances));
                if (item instanceof ExoticPotion){
                    result = Reflection.newInstance(ExoticPotion.regToExo.get(result.getClass()));
                }
            } else if (item instanceof Scroll) {
                result = Reflection.newInstance(Random.chances(scrollChances));
                if (item instanceof ExoticScroll){
                    result = Reflection.newInstance(ExoticScroll.regToExo.get(result.getClass()));
                }
            } else if (item instanceof Plant.Seed) {
                result = Generator.random(Generator.Category.SEED);
            } else if (item instanceof Runestone) {
                result = Generator.random(Generator.Category.STONE);
            } else {
                result = TippedDart.randomTipped(1);
            }
        } while (result.getClass() == item.getClass() || Challenges.isItemBlocked(result));

        item.detach(curUser.belongings.backpack);
        GLog.p(Messages.get(this, "recycled", result.name()));
        if (!result.collect()){
            Dungeon.level.drop(result, curUser.pos).sprite.drop();
        }
        Transmuting.show(curUser, item, result);
        curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
    }

    @Override
    public int value() {
        //prices of ingredients, divided by output quantity
        return Math.round(quantity * ((50 + 40) / 12f));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScrollOfTransmutation.class, ArcaneCatalyst.class};
            inQuantity = new int[]{1, 1};

            cost = 8;

            output = Recycle.class;
            outQuantity = 12;
        }

    }
}
