package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import java.util.ArrayList;

public class TierOfTalent {
    private static ArrayList<Talent> One = new ArrayList<>();
    private static ArrayList<Talent> Two = new ArrayList<>();
    private static ArrayList<Talent> Three = new ArrayList<>();
    private static ArrayList<Talent> Four = new ArrayList<>();
    private static ArrayList<Talent> Old = new ArrayList<>();

    private static void ResetOne(){
        One = new ArrayList<>();
        //Ump45 t1
        One.add(Talent.HEARTY_MEAL);
        One.add(Talent.ARMSMASTERS_INTUITION);
        One.add(Talent.TEST_SUBJECT);
        One.add(Talent.IRON_WILL);
        //G11 t1
        One.add(Talent.EMPOWERING_MEAL);
        One.add(Talent.SCHOLARS_INTUITION);
        One.add(Talent.TESTED_HYPOTHESIS);
        One.add(Talent.BACKUP_BARRIER);
        //Ump9 t1
        One.add(Talent.CACHED_RATIONS);
        One.add(Talent.THIEFS_INTUITION);
        One.add(Talent.SUCKER_PUNCH);
        One.add(Talent.PROTECTIVE_SHADOWS);
        //HK416 t1
        One.add(Talent.NATURES_BOUNTY);
        One.add(Talent.SURVIVALISTS_INTUITION);
        One.add(Talent.FOLLOWUP_STRIKE);
        One.add(Talent.NATURES_AID);
        //Type56 t1
        One.add(Talent.Type56One_FOOD);
        One.add(Talent.Type56One_Identify);
        One.add(Talent.Type56One_Damage);
        One.add(Talent.Type56_14);
        //Type56 t1 test
        One.add(Talent.Type56_14V2);
        //Type56 t1 old
        One.add(Talent.NICE_FOOD);
        One.add(Talent.OLD_SOLDIER);
        One.add(Talent.FAST_RELOAD);
        //GSH18 t1
        One.add(Talent.GSH18_MEAL_TREATMENT);
        One.add(Talent.GSH18_DOCTOR_INTUITION);
        One.add(Talent.GSH18_CLOSE_COMBAT);
        One.add(Talent.GSH18_STAR_SHIELD);
    }

    private static void ResetTwo(){
        Two = new ArrayList<>();
        //Ump45 t2
        Two.add(Talent.IRON_STOMACH);
        Two.add(Talent.RESTORED_WILLPOWER);
        Two.add(Talent.RUNIC_TRANSFERENCE);
        Two.add(Talent.LETHAL_MOMENTUM);
        Two.add(Talent.IMPROVISED_PROJECTILES);
        //G11 t2
        Two.add(Talent.ENERGIZING_MEAL);
        Two.add(Talent.ENERGIZING_UPGRADE);
        Two.add(Talent.WAND_PRESERVATION);
        Two.add(Talent.ARCANE_VISION);
        Two.add(Talent.SHIELD_BATTERY);
        //Ump9 t2
        Two.add(Talent.MYSTICAL_MEAL);
        Two.add(Talent.MYSTICAL_UPGRADE);
        Two.add(Talent.WIDE_SEARCH);
        Two.add(Talent.SILENT_STEPS);
        Two.add(Talent.ROGUES_FORESIGHT);
        //HK416 t2
        Two.add(Talent.INVIGORATING_MEAL);
        Two.add(Talent.RESTORED_NATURE);
        Two.add(Talent.REJUVENATING_STEPS);
        Two.add(Talent.HEIGHTENED_SENSES);
        Two.add(Talent.DURABLE_PROJECTILES);
        //Type56 t2
        Two.add(Talent.Type56Two_FOOD);
        Two.add(Talent.Type56Two_Armor);
        Two.add(Talent.Type56Two_Grass);
        Two.add(Talent.Type56Two_Sight);
        Two.add(Talent.Type56Two_Damage);
        //Type56 t2 test
        Two.add(Talent.Type56_21V2);
        Two.add(Talent.Type56_21V3);
        Two.add(Talent.Type56_22V2);
        Two.add(Talent.Type56_23V2);
        Two.add(Talent.Type56_23V3);
        //GSH18 t2
        Two.add(Talent.GSH18_ENERGIZING_MEAL);
        Two.add(Talent.GSH18_CHAIN_SHOCK);
        Two.add(Talent.GSH18_LOGISTICS_SUPPORT);
        Two.add(Talent.GSH18_COMIC_HEART);
        Two.add(Talent.GSH18_MEDICAL_COMPATIBILITY);
    }

    private static void ResetThree(){
        Three = new ArrayList<>();
        //Ump45 t3
        Three.add(Talent.HOLD_FAST);
        Three.add(Talent.STRONGMAN);
        //Berserker T3
        Three.add(Talent.ENDLESS_RAGE);
        Three.add(Talent.BERSERKING_STAMINA);
        Three.add(Talent.ENRAGED_CATALYST);
        //Gladiator T3
        Three.add(Talent.CLEAVE);
        Three.add(Talent.LETHAL_DEFENSE);
        Three.add(Talent.ENHANCED_COMBO);

        //G11 T3
        Three.add(Talent.EMPOWERING_SCROLLS);
        Three.add(Talent.ALLY_WARP);
        //Battlemage T3
        Three.add(Talent.EMPOWERED_STRIKE);
        Three.add(Talent.MYSTICAL_CHARGE);
        Three.add(Talent.EXCESS_CHARGE);
        //Warlock T3
        Three.add(Talent.SOUL_EATER);
        Three.add(Talent.SOUL_SIPHON);
        Three.add(Talent.NECROMANCERS_MINIONS);

        //Ump9 T3
        Three.add(Talent.ENHANCED_RINGS);
        Three.add(Talent.LIGHT_CLOAK);
        //Assassin T3
        Three.add(Talent.ENHANCED_LETHALITY);
        Three.add(Talent.ASSASSINS_REACH);
        Three.add(Talent.BOUNTY_HUNTER);
        //Freerunner T3
        Three.add(Talent.EVASIVE_ARMOR);
        Three.add(Talent.PROJECTILE_MOMENTUM);
        Three.add(Talent.SPEEDY_STEALTH);

        //HK416 T3
        Three.add(Talent.POINT_BLANK);
        Three.add(Talent.SEER_SHOT);
        //Sniper T3
        Three.add(Talent.FARSIGHT);
        Three.add(Talent.SHARED_ENCHANTMENT);
        Three.add(Talent.SHARED_UPGRADES);
        //Warden T3
        Three.add(Talent.DURABLE_TIPS);
        Three.add(Talent.BARKSKIN);
        Three.add(Talent.SHIELDING_DEW);

        //type561 T3
        Three.add(Talent.Type56Three_Bomb);
        Three.add(Talent.Type56Three_Book);
        //type561 T3-1 EMP
        Three.add(Talent.EMP_One);
        Three.add(Talent.EMP_Two);
        Three.add(Talent.EMP_Three);
        //type561 T3-2 GUN
        Three.add(Talent.GUN_1);
        Three.add(Talent.GUN_2);
        Three.add(Talent.GUN_3);

        //type561 T3-2 GUN test
        Three.add(Talent.GUN_1V2);
        Three.add(Talent.GUN_1V3);
        Three.add(Talent.GUN_2V2);

        //GSH18 t3
        Three.add(Talent.GSH18_INTELLIGENCE_AWARENESS);
        Three.add(Talent.GSH18_AGILE_MOVEMENT);

        //GSH18 t3-1
        Three.add(Talent.GSH18_SIRIUS_HEART);

    }

    private static void ResetFour(){
        Four = new ArrayList<>();
        //Heroic Leap T4
        Four.add(Talent.BODY_SLAM);
        Four.add(Talent.IMPACT_WAVE);
        Four.add(Talent.DOUBLE_JUMP);
        //Shockwave T4
        Four.add(Talent.EXPANDING_WAVE);
        Four.add(Talent.STRIKING_WAVE);
        Four.add(Talent.SHOCK_FORCE);
        //Endure T4
        Four.add(Talent.SUSTAINED_RETRIBUTION);
        Four.add(Talent.SHRUG_IT_OFF);
        Four.add(Talent.EVEN_THE_ODDS);

        //Elemental Blast T4
        Four.add(Talent.BLAST_RADIUS);
        Four.add(Talent.ELEMENTAL_POWER);
        Four.add(Talent.REACTIVE_BARRIER);
        //Wild Magic T4
        Four.add(Talent.WILD_POWER);
        Four.add(Talent.FIRE_EVERYTHING);
        Four.add(Talent.CONSERVED_MAGIC);
        //Warp Beacon T4
        Four.add(Talent.TELEFRAG);
        Four.add(Talent.REMOTE_BEACON);
        Four.add(Talent.LONGRANGE_WARP);

        //Smoke Bomb T4
        Four.add(Talent.HASTY_RETREAT);
        Four.add(Talent.BODY_REPLACEMENT);
        Four.add(Talent.SHADOW_STEP);
        //Death Mark T4
        Four.add(Talent.FEAR_THE_REAPER);
        Four.add(Talent.DEATHLY_DURABILITY);
        Four.add(Talent.DOUBLE_MARK);
        //Shadow Clone T4
        Four.add(Talent.SHADOW_BLADE);
        Four.add(Talent.CLONED_ARMOR);
        Four.add(Talent.PERFECT_COPY);

        //Spectral Blades T4
        Four.add(Talent.FAN_OF_BLADES);
        Four.add(Talent.PROJECTING_BLADES);
        Four.add(Talent.SPIRIT_BLADES);
        //Natures Power T4
        Four.add(Talent.GROWING_POWER);
        Four.add(Talent.NATURES_WRATH);
        Four.add(Talent.WILD_MOMENTUM);
        //Spirit Hawk T4
        Four.add(Talent.EAGLE_EYE);
        Four.add(Talent.GO_FOR_THE_EYES);
        Four.add(Talent.SWIFT_SPIRIT);

        //Ratmogrify T4
        Four.add(Talent.RATSISTANCE);
        Four.add(Talent.RATLOMACY);
        Four.add(Talent.RATFORCEMENTS);

        //ENERGY T4
        Four.add(Talent.HEROIC_ENERGY);


        //type561 T4-1
        Four.add(Talent.Type56FourOneOne);
        Four.add(Talent.Type56FourOneTwo);
        Four.add(Talent.Type56FourOneThree);
        //type561 T4-2
        Four.add(Talent.Type56FourTwoOne);
        Four.add(Talent.Type56FourTwoTwo);
        Four.add(Talent.Type56FourTwoThree);
        //type561 T4-3
        Four.add(Talent.Type56_431);
        Four.add(Talent.Type56_432);
        Four.add(Talent.Type56_433);
    }

    private static void ResetOld(){
        Old = new ArrayList<>();
        Old.add(Talent.NICE_FOOD);
        Old.add(Talent.OLD_SOLDIER);
        Old.add(Talent.FAST_RELOAD);
        Old.add(Talent.BETTER_FOOD);
        Old.add(Talent.BARGAIN_SKILLS);
        Old.add(Talent.TRAP_EXPERT);
        Old.add(Talent.HOW_DARE_YOU);
        Old.add(Talent.JIEFANGCI);
        Old.add(Talent.NIGHT_EXPERT);
        Old.add(Talent.SEARCH_ARMY);
        Old.add(Talent.ELITE_ARMY);
    }
    public static void ResetAllTalentList(){
        ResetOne();
        ResetTwo();
        ResetThree();
        ResetFour();
        ResetOld();
    }

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
