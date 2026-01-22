package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import java.util.ArrayList;
import java.util.Arrays;

public class TierOfTalent {
    private static final ArrayList<Talent> One = new ArrayList<>(Arrays.asList(
            //Ump45 t1
            Talent.HEARTY_MEAL,Talent.ARMSMASTERS_INTUITION,Talent.TEST_SUBJECT,Talent.IRON_WILL,
            //G11 t1
            Talent.EMPOWERING_MEAL,Talent.SCHOLARS_INTUITION,Talent.TESTED_HYPOTHESIS,Talent.BACKUP_BARRIER,
            //Ump9 t1
            Talent.CACHED_RATIONS,Talent.THIEFS_INTUITION,Talent.SUCKER_PUNCH,Talent.PROTECTIVE_SHADOWS,
            //HK416 t1
            Talent.NATURES_BOUNTY,Talent.SURVIVALISTS_INTUITION,Talent.FOLLOWUP_STRIKE,Talent.NATURES_AID,
            //Type56 t1
            Talent.Type56One_FOOD,Talent.Type56One_Identify,Talent.Type56One_Damage,Talent.Type56_14,
            //Type56 t1 test
            Talent.Type56_14V2,
            //Type56 t1 old
            Talent.NICE_FOOD,Talent.OLD_SOLDIER,Talent.FAST_RELOAD,
            //GSH18 t1
            Talent.GSH18_MEAL_TREATMENT,Talent.GSH18_DOCTOR_INTUITION,Talent.GSH18_CLOSE_COMBAT,Talent.GSH18_STAR_SHIELD
    ));
    private static final ArrayList<Talent> Two = new ArrayList<>(Arrays.asList(
            //Ump45 t2
            Talent.IRON_STOMACH, Talent.RESTORED_WILLPOWER, Talent.RUNIC_TRANSFERENCE, Talent.LETHAL_MOMENTUM, Talent.IMPROVISED_PROJECTILES,
            //G11 t2
            Talent.ENERGIZING_MEAL, Talent.ENERGIZING_UPGRADE, Talent.WAND_PRESERVATION, Talent.ARCANE_VISION, Talent.SHIELD_BATTERY,
            //Ump9 t2
            Talent.MYSTICAL_MEAL, Talent.MYSTICAL_UPGRADE, Talent.WIDE_SEARCH, Talent.SILENT_STEPS, Talent.ROGUES_FORESIGHT,
            //HK416 t2
            Talent.INVIGORATING_MEAL, Talent.RESTORED_NATURE, Talent.REJUVENATING_STEPS, Talent.HEIGHTENED_SENSES, Talent.DURABLE_PROJECTILES,
            //Type56 t2
            Talent.Type56Two_FOOD, Talent.Type56Two_Armor, Talent.Type56Two_Grass, Talent.Type56Two_Sight, Talent.Type56Two_Damage,
            //Type56 t2 test
            Talent.Type56_21V2, Talent.Type56_21V3, Talent.Type56_22V2, Talent.Type56_23V2, Talent.Type56_23V3,
            //GSH18 t2
            Talent.GSH18_ENERGIZING_MEAL, Talent.GSH18_CHAIN_SHOCK, Talent.GSH18_LOGISTICS_SUPPORT, Talent.GSH18_COMIC_HEART, Talent.GSH18_MEDICAL_COMPATIBILITY
    ));
    private static final ArrayList<Talent> Three = new ArrayList<>(Arrays.asList(
            //Ump45 t3
            Talent.HOLD_FAST, Talent.STRONGMAN,
            //Berserker T3
            Talent.ENDLESS_RAGE, Talent.BERSERKING_STAMINA, Talent.ENRAGED_CATALYST,
            //Gladiator T3
            Talent.CLEAVE, Talent.LETHAL_DEFENSE, Talent.ENHANCED_COMBO,

            //G11 T3
            Talent.EMPOWERING_SCROLLS, Talent.ALLY_WARP,
            //Battlemage T3
            Talent.EMPOWERED_STRIKE, Talent.MYSTICAL_CHARGE, Talent.EXCESS_CHARGE,
            //Warlock T3
            Talent.SOUL_EATER, Talent.SOUL_SIPHON, Talent.NECROMANCERS_MINIONS,

            //Ump9 T3
            Talent.ENHANCED_RINGS, Talent.LIGHT_CLOAK,
            //Assassin T3
            Talent.ENHANCED_LETHALITY, Talent.ASSASSINS_REACH, Talent.BOUNTY_HUNTER,
            //FreeRunner T3
            Talent.EVASIVE_ARMOR, Talent.PROJECTILE_MOMENTUM, Talent.SPEEDY_STEALTH,

            //HK416 T3
            Talent.POINT_BLANK, Talent.SEER_SHOT,
            //Sniper T3
            Talent.FARSIGHT, Talent.SHARED_ENCHANTMENT, Talent.SHARED_UPGRADES,
            //Warden T3
            Talent.DURABLE_TIPS, Talent.BARKSKIN, Talent.SHIELDING_DEW,

            //type561 T3
            Talent.Type56Three_Bomb, Talent.Type56Three_Book,
            //type561 T3-1 EMP
            Talent.EMP_One, Talent.EMP_Two, Talent.EMP_Three,
            //type561 T3-2 GUN
            Talent.GUN_1, Talent.GUN_2, Talent.GUN_3,

            //type561 T3-2 GUN test
            Talent.GUN_1V2, Talent.GUN_1V3, Talent.GUN_2V2,

            //GSH18 t3
            Talent.GSH18_INTELLIGENCE_AWARENESS, Talent.GSH18_AGILE_MOVEMENT,

            //GSH18 t3-1
            Talent.GSH18_SIRIUS_HEART
    ));
    private static final ArrayList<Talent> Four = new ArrayList<>(Arrays.asList(
            //Heroic Leap T4
            Talent.BODY_SLAM, Talent.IMPACT_WAVE, Talent.DOUBLE_JUMP,
            //Shockwave T4
            Talent.EXPANDING_WAVE, Talent.STRIKING_WAVE, Talent.SHOCK_FORCE,
            //Endure T4
            Talent.SUSTAINED_RETRIBUTION, Talent.SHRUG_IT_OFF, Talent.EVEN_THE_ODDS,

            //Elemental Blast T4
            Talent.BLAST_RADIUS, Talent.ELEMENTAL_POWER, Talent.REACTIVE_BARRIER,
            //Wild Magic T4
            Talent.WILD_POWER, Talent.FIRE_EVERYTHING, Talent.CONSERVED_MAGIC,
            //Warp Beacon T4
            Talent.TELEFRAG, Talent.REMOTE_BEACON, Talent.LONGRANGE_WARP,

            //Smoke Bomb T4
            Talent.HASTY_RETREAT, Talent.BODY_REPLACEMENT, Talent.SHADOW_STEP,
            //Death Mark T4
            Talent.FEAR_THE_REAPER, Talent.DEATHLY_DURABILITY, Talent.DOUBLE_MARK,
            //Shadow Clone T4
            Talent.SHADOW_BLADE, Talent.CLONED_ARMOR, Talent.PERFECT_COPY,

            //Spectral Blades T4
            Talent.FAN_OF_BLADES, Talent.PROJECTING_BLADES, Talent.SPIRIT_BLADES,
            //Natures Power T4
            Talent.GROWING_POWER, Talent.NATURES_WRATH, Talent.WILD_MOMENTUM,
            //Spirit Hawk T4
            Talent.EAGLE_EYE, Talent.GO_FOR_THE_EYES, Talent.SWIFT_SPIRIT,

            //RatMogrify T4
            Talent.RATSISTANCE, Talent.RATLOMACY, Talent.RATFORCEMENTS,

            //ENERGY T4
            Talent.HEROIC_ENERGY,


            //type561 T4-1
            Talent.Type56FourOneOne, Talent.Type56FourOneTwo, Talent.Type56FourOneThree,
            //type561 T4-2
            Talent.Type56FourTwoOne, Talent.Type56FourTwoTwo, Talent.Type56FourTwoThree,
            //type561 T4-3
            Talent.Type56_431, Talent.Type56_432, Talent.Type56_433
    ));
    private static final ArrayList<Talent> Old = new ArrayList<>(Arrays.asList(
            Talent.NICE_FOOD, Talent.OLD_SOLDIER, Talent.FAST_RELOAD, Talent.BETTER_FOOD, Talent.BARGAIN_SKILLS,
            Talent.TRAP_EXPERT, Talent.HOW_DARE_YOU, Talent.JIEFANGCI, Talent.NIGHT_EXPERT, Talent.SEARCH_ARMY,
            Talent.ELITE_ARMY
    ));

    public static int Tier(Talent talent){
        if (One.contains(talent)){
            return 1;
        }else if (Two.contains(talent)){
            return 2;
        }else if (Three.contains(talent)){
            return 3;
        }else if (Four.contains(talent)){
            return 4;
        }else if (Old.contains(talent)){
            return 2;
        }
        return 1;
    }
}
