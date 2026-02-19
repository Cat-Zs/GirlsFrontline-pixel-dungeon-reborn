package com.shatteredpixel.shatteredpixeldungeon.ui.changelist.girlpd;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.EquipLevelUp;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Education;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Cyclops;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.GolyatFactory;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Guard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Rat;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Snake;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Succubus;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.FieldPot;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.Amulet;
import com.shatteredpixel.shatteredpixeldungeon.items.Dewdrop;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.LloydsBeacon;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Recycle;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.SMG.Ump45;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SentryRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.AgentSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CrabSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CyclopsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM300Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElpheltSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GnollSWAPSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolyatFactorySprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GolyatPlusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RipperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SnakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpawnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerCatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TyphoonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WandmakerSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;

import java.util.ArrayList;
import java.util.Arrays;

public class v0_5_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_0_5_7_1_Changes(changeInfos);
        add_0_5_7_Changes(changeInfos);
        add_0_5_6_Changes(changeInfos);
    	add_0_5_5_4_Changes(changeInfos);
    	add_0_5_5_3_Changes(changeInfos);
    	add_0_5_5_2_Changes(changeInfos);
    	add_0_5_5_1_Changes(changeInfos);
    	add_0_5_5_Changes(changeInfos);
    	add_0_5_4_5_Changes(changeInfos);
    	add_0_5_4_3_Changes(changeInfos);
    	add_0_5_4_1_Changes(changeInfos);
    	add_0_5_3_Changes(changeInfos);
		add_0_5_2_Changes(changeInfos);
		add_0_5_1_Changes(changeInfos);
		add_0_5_0_Changes(changeInfos);
    }

    public static void add_0_5_7_1_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.7v1", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new BuffIcon(new EquipLevelUp(), false),1.8F, "临时升级", "现在所有的临时升级都可以保存到排行榜了，那么现在排行榜武器最高等级是多少呢？"));
        changes.addButton( new ChangeButton(new HeroIcon(new Education()), 0.8F, "高等教育",
                "_-_ 新增一个通用的四层转职，以放弃护甲技能为代价，将四层天赋点轮流加到前三层。\n\n"+
                "某人依旧没有考过四级......QAQ"
        ));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS), "查种器",
                "_-_ 现在把破碎的查种器搬了过来了，这个查种器的逻辑就是依赖物品的文本查询的，我仔细想了想，好像也没有更好的方案来了。\n\n"
                +"_-_ 建议先仔细阅读查询界面右下角感叹号的注意事项再进行使用，不要再说什么查询了一个多小时都没有查询到啦！！！"
        ));
        changes.addButton( new ChangeButton(new TyphoonSprite(),0.3F,
                new TyphoonSprite().zap, true,
                "代码整理",
                "_-_ 我对更改界面的按钮爆改了三轮，从几十行改到一千三，删掉重做，改到八九百，删掉重做，最后就是现在这样的一千两百行的成品了。\n\n" +
                        "_-_ 最终得到现在这样的作品，能够在更改界面给物品显示变色，给怪物显示精英光环、执行动作。\n\n"+
                        "_-_ 在此界面滑动到最下方，右侧的提丰就是_见面送十炮_了。\n\n"+
                        "_-_ 激光类的四个敌人的逻辑依旧是瞄准一个格子之后必定释放而非储存，在释放的那一回合会进行二次校准。\n\n"+
                        "_-_ 如果有啥好玩的想法也能够向我提议，我会挑选一些我也觉得不错的点子加进来。鉴于我现在不在群里了，可以向2085785947QQ邮箱发送邮件向我发表提议。"
                )
        );

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Elphelt(), 0.8F, new ElpheltSprite().run, "加回0.4.9及以前的彩蛋触发方式，并且保留当前的开启_ 深入敌腹 _的触发方式。"));
        changes.addButton(new ChangeButton(new Recycle(), "_-_ 对磁盘、药水使用转换棱晶不再占用楼层物品生成的概率。\n\n_-_ 现在转换棱晶对磁盘和药水使用获得道具的概率与两种催化剂一致"));
        changes.addButton(new ChangeButton(new Ump45(), "代码整理", "我将Ump45、C96、灵刀的CD相关代码整理了一下，旧版本的这三个武器有可能CD不动了，这种情况下再用一下技能就又能动了。"));
        changes.addButton(new ChangeButton(new FieldPot(), 0.8F, "修复了行军锅卡死、不消耗护甲充能等的bug。"));
        changes.addButton(new ChangeButton(new HeroSprite(HeroClass.TYPE561, 6), 0.8F, "56-1式", "修复了56-1式初始饱食度上限少了300点的bug。"));
        changes.addButton(new ChangeButton(new WandmakerSprite(), 0.8F, "未知小彩蛋", "为了方便新玩家尽快玩到想玩的内容，我在某些地方留了点后门，快去找一找吧~awa"));
        changes.addButton(new ChangeButton(new Image(Icons.WARNING.get()), "代码整理", "由于1月底到2月中旬期间的一系列改动与回退，我不经意间把0.5.3前后因修改底层导致在此之前的存档作废这个问题给修好了。(如果还有人保留着0.5.3及以前的存档的话)"));
    }
    public static void add_0_5_7_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.7", true, "");
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

		changes.addButton( new ChangeButton(new HeroSprite(HeroClass.TYPE561, 5),
                0.8F,
                "56-1式",
        "_-_ 全面重置了角色_56-1式_的基础能力及天赋，以及其他联带道具，以期增强其游戏性与平衡性。\n" +
        "_-_ 同时，在本次更新中56-1式角色获得了_专属的护甲技能_\n" +
        "_-_ 这意味着56-1式角色将不再必须回到5层从FNC处获取护甲技能了！\n" +
        "_-_ 更多角色能力详情请前往游戏内阅读。"
         ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_DIAMOND, null),
                "战术瞄准镜",
        "_-_ 增强了_战术瞄准镜_对攻击_速度极慢_的武器的增幅效果。\n" +
        "_-_ 这意味着，那些需要极长回合才能攻击一次的_狙击枪类武器_会从中获取更高的_攻击速度_收益。"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH, null),
                "快速修复契约",
        "_-_ 未强化的_快速修复契约_现在可以用以替换被感染的装备。"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        ArrayList<String> pageMessage2 = new ArrayList<>();
        {
            pageMessage2.add(
                    "_-_ 优化了更新公告的呈现方式，以后冗杂的_小项优化内容_将以此_页面形式_呈现\n\n" +
                            "_-_ _节日彩蛋敌人_加入了_敌人生成器_，目前尚未进行专类分页\n\n" +
                            "_-_ 修复了_感知符石_不能猜两次的BUG\n\n" +
                            "_-_ 修复了未鉴定的需要选择目标的磁盘在首次使用时会产生_额外消耗_的BUG\n\n" +
                            "_-_ 为所有_提供护甲的武器_加入了实时_属性显示_文本。"
            );
            pageMessage2.add(
                    "_-_ _测试模式_的道具_测试书_内容得到了丰富，玩家可以_更加自由_的在测试模式下调节一些不易调节的属性数值。\n\n" +
                            "_-_ 在_手机版_加入独立_退出游戏按钮_，玩家可以不必使用手机的_返回按钮_来非正式的退出游戏了。(防止手机端在主菜单使用手机的返回键无法退出游戏的极端情况）\n\n" +
                            "_-_ 优化了_角色选择界面_，可以使用翻页按钮切换角色栏目，避免界面拥挤。\n\n" +
                            "_-_ 修复了作为_主脑护卫_的铁血BOSS_攻击特效_错误的BUG\n\n"
            );
            pageMessage2.add(
                    "_-_ 挑战_精英强敌_不再会令_初始中立_的怪物初始处于敌对状态。\n\n" +
                            "_-_ _二选一水晶补给箱_逻辑优化，在获取了所有的遗物后，补给箱将不会_同时刷新_两个瞄准镜宝箱。\n\n" +
                            "_-_ 改动了UMP45的_受击音效_（鼻梁骨警告）。\n\n"+
                            "_-_ 修复了部分极端情况下，特效_大量堆叠_造成崩溃的BUG。\n\n"
            );
            pageMessage2.add(
                    "_-_ 修复了因为消耗物品时机改动而导致的，_嬗变磁盘_可以_嬗变自身_的BUG。\n\n" +
                            "_-_ 修复了UMP45的2层天赋_+2 刻印转移_总保留护甲刻印而无法_自选刻印_的BUG。\n\n" +
                            "_-_ 优化了测试模式的_地形编辑器_，现在会自动更新改动_地格的视野_。\n\n" +
                            "_-_ 修复了玩家死亡导致游戏结束后，_不显示GAME OVER标志_的BUG。\n"
            );
            pageMessage2.add(
                    "_-_ 修复了PC版_移动按键_在部分情况下实际功能错乱的BUG。\n\n" +
                            "_-_ 在_设置界面_加入了_切换UI风格_的按钮。\n\n" +
                            "_-_ _启用种子_进行的游戏将在_排行榜_有标记以_区分_。\n\n" +
                            "_-_ 在角色属性界面增加了_复制按钮_，以便捷的_复制种子_。\n"
            );
            pageMessage2.add(
                    "_-_ 新增了装备同类瞄准镜，将显示_连携升级_提供的属性。\n\n" +
                            "_-_ 扩充了_排行榜_可显示查看的游戏记录上限，为30条，可翻页进行查阅。\n\n" +
                            "_-_ 测试模式新增道具_清图炸弹_，可以高效肃清楼层的_一切_（包括地形），请谨慎使用。\n\n" +
                            "_-_ 修复了_物品标签_在部分情况下退出游戏重进后消失的BUG。对各种物品窗口适配标签系统。\n\n" +
                            "_-_ _诅咒的财富瞄准镜_视为等级-3，但加成不超过0%。负等级的财富也可以掉落额外的物品，但其每拥有的1级负数等级都会使掉落额外物品所需的击杀数+1。\n"
            );
        }        changes.addButton( new ChangeButton(new BlacksmithSprite(),
                0.8F,
                "游戏优化", pageMessage2));
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY,null),
                "水晶房间",
        "_-_ 对需要三把钥匙才能完全开启的_水晶房间_的六选三变体进行了优化。\n\n"+
                "_-_ 只有玩家在经验药水与嬗变磁盘都_已鉴定_的情况下，两边的最后的房间才会同时刷新，否则将为随机刷新。\n\n"+
                "_-_ 不过在只鉴定了其中一者的情况下，非保底稀有道具的基座将会被毁坏，即便经验/嬗变从默认池子的1/30的概率杀了出来()。"
        ));

        ArrayList<Char> MobChange = new ArrayList<>(Arrays.asList(new Rat(), new GolyatFactory(), new Guard(), new Cyclops(), new Succubus(), new Warlock()));
        MovieClip.Animation[] GolyatFactoryAction = new MovieClip.Animation[2];
        GolyatFactoryAction[1]=new GolyatFactorySprite().charging;
        ArrayList<String> MobChangeBody = new ArrayList<>(Arrays.asList(
                "_-_ 此版本新增了一些怪物。\n"+
                        "_-_ 修改了部分掉落治疗药水的的怪物可掉落数量。\n"+
                        "_-_ 修改了部分怪物不合理的经验等级。\n"+
                        "_-_ 为部分怪物补充了掉落物。",
                "_-_ 加入了新敌人_歌利亚工厂_，将在_8-9层_随机刷新，击败后掉落随机药水（概率为治疗）。\n" +
                        "_-_ 歌利亚工厂没有主动攻击手段，但在遇敌后将制造一个歌利亚或者将已制造的歌利亚传送过来进行作战，然后对其加血或者赋予激素涌动。\n"+
                        "_-_ 歌利亚工厂被击败后，由其制造的歌利亚将_同步死亡_。\n\n"+
                        "_-_ 放轻松，歌利亚工厂制造的歌利亚_大小不一_是正常情况。\n",
                "盾娘的可获取经验等级提高到28级，可掉落治疗药水数量提高到9瓶。",
                "为独眼巨人A型补充掉落物-随机药水，但不包括治疗。",
                "为蜘蛛雷的可获取经验等级提高到33，以减少通过五区就超出蜘蛛雷可掉落物品等级的情况，",
                "4区狙娘可掉落治疗数量从实际的7瓶修复到理应的8瓶。"
        ));
        changes.addButton(new ChangeButton(MobChange, GolyatFactoryAction, "怪物改动",new Float[]{1F, 0.6F, 0.8F ,0.8F, 0.8F, 0.8F}, MobChangeBody));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight( Window.TITLE_COLOR );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "0层：格里芬基地",
			"_-_ 修缮了0层的_格里芬基地_现在成为了拥有实际功能的区域了。\n\n" +
			"_-_ _前进营地_区域可以直接进入游戏，也可查看成就与排行榜。\n\n" +
			"_-_ _404安全屋_暂无实际功能，现阶段仅为实装前代作者未实装的区域。\n\n"+
			"_-_ _咖啡厅_暂无实际功能，未来将加入可互动的NPC。\n\n"+
			"_-_ 不要让RFB知道前进营地的_阀门控制台_可以用来_打电动_！\n\n"
		));

        ArrayList<String> pageMessage1 = new ArrayList<>();
        {
            pageMessage1.add(
                    "_-_ 点击主页面_下一页_，再点击_返回地表_将进入0层基地，返回地表将在第一次带_获救的M4A1_从1层上楼解锁\n\n" +
                            "_-_ 若遇见0层基地_背景未刷新_的问题（通常出现在旧版本创建的0层存档被带到了新版本），需点击右上角菜单_重置楼层_，而后重新进入\n\n" +
                            "_-_ _重置楼层_按钮同样可以用于应对在0层基地中因任何未知原因卡死但未崩溃的情况"
            );
            pageMessage1.add(
                    "_-_ 通过点击_实时转播终端_可以查看_排行榜_与_成就_。\n\n" +
                            "_-_ 通过点击_消毒通道_可以_进入游戏存档_。\n\n" +
                            "_-_ 通过点击_先进阀门控制台_可以_返回主页面_。\n"
            );
            pageMessage1.add(
                    "_-_ 0层基地开放了多个房间已供探索，但大多数暂无实际功能。\n\n" +
                            "_-_ 通过点击FNC可以选择开启节日彩蛋模式，未来将加入更多节日彩蛋。\n\n" +
                            "_-_ _先进阀门控制台_的电脑看起来很适合_打电动_……\n\n"
            );
        }
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.IRON_KEY, null),
                "0层基地指南", pageMessage1));

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),"排行榜调整","_-_现在排行榜默认保存30个记录。\n\n_-_新增锁定排行榜记录功能，在对应记录的窗口的右上角，点击其可以将此记录锁定，不再会被清理。\n\n_-_被锁定的记录不会占用默认保存数量的30次。"));
//        Hero hero = new Hero();
//        hero.heroClass = HeroClass.TYPE561;
//        Object[] aaaa = new Object[]{new YogFist.DarkFist(), new Hero(), new Waterskin(),hero,new Amulet(), new Gold(10), new Dewdrop()};
//        changes.addButton(new ChangeButton("标题","内容",aaaa));
        //现在加入了自动分拣，所以可以任意缺项、打乱顺序，只需要明确末端是数组（Object[]或者ArrayList）还是值即可
        //可以接受贴图、生物（包括怪物和玩家）、物品
        //由于自动分拣过程中，需要对空的标题列表和内容列表按照生物/物品的相关文案填充
        //现在数组的填充要在new ChangeButton之前（理论上除了贴图/生物/物品、标题、内容的ArrayList可以在之后填充）
        //这种填充可以加入一对大括号{}，然后就可以在左边收起来了
        //自动分拣的过程中，内容是按照从末项-次末项-...-次项-首项这样的逆序获取的，标题是按照次末项-首项-次项-...这样的顺序获取的，并且会让出唯一String项给内容
        //所以请按照左边标题右边内容的顺序输入参数
        //以及为了避免不必要的性能消耗，最好还是按照以下顺序输入，虽然到目前为止我都没遇到过卡顿
        //目前可接受参数为 贴图(Image)/生物(Char/Mob/Hero)/物品(Item)、大小(float)、动作(MovieClip.Animation)、修改动作循环逻辑(Boolean)、颜色(int)、标题(String)、内容(String)
        //输入参数时，缺项会补默认值，依次是波波沙贴图、不改动（非正数）、默认动作（null）、不改动（null）、不闪光/发光（非正数）、生物或物品名称/无文本、生物或物品描述/无文本
        //请留意末项的使用，末项是数组就会使用翻页窗口，前面是值会填充成数组，长度不够的数组也会补充到足够
        //末项是值就会使用普通窗口，哪怕前面是数组，只会获取到首位非空项展示，如果得到的是空项则按照上面的来填充
        //翻页窗口填充的时候，null/空数组/全null数组的时候按照上面的来填充，否则首项为空的情况下会遍历数组找到第一个非空的值复制过来
        //在补长度或者对null填充的过程中，贴图、标题、内容复制首项，动作、循环逻辑填充null
        //大小、颜色的逻辑有所变化，如果当前贴图项与首项一致，则复制首项（无论当前贴图项是复制的还是原本就填写的这个，至少复制首项不会出错），否则填充不改动
        //数组可以填入ArrayList<Object>()或者Object[]，Object可以包含所有，Char可以包含Mob和Hero，Object[]包含除了int[]、float[]、boolean[]这种小写数组（基础数组）以外的所有普通数组
        //输入ArrayList的时候，float要换成Float，int要换成Integer，boolean要换成Boolean(虽然我要求的参数就是Boolean就是了)，其他的就把其类型复制到<>里边就行了
        //加入动作基本就是new 对应生物的材质加上“.”+“对应动作”
        //Boolean对比boolean多个null，false是强制不循环，true是强制循环，对单窗口来说，null是在窗口处不执行动作，对翻页窗口来说，null是不改变循环逻辑
        //大小、颜色只在正数部分生效，输入非正数的话，就什么改动也没有
        //大小很好理解，字面意思，颜色的话，对于贴图/物品，是令得到的贴图闪光，对于生物，是令得到的贴图发光
        //目前闪光和发光不可共存，我在考虑要不要再引入一个参数Long来控制按照哪一种来使用
        changes.addButton(new ChangeButton(new AgentSprite(), 0.6F,
                new AgentSprite().zap,
                "代理人",
        "_-_ 加入了敌人_代理人_作为主脑可呼叫护卫的其中之一。\n"+
        "_-_ 代理人替换了_黑暗之拳_，但在未来将获得全新的战斗方式和能力\n\n"+
        "_-_ 她刚刚是不是_开火_？"
        ));

        changes.addButton( new ChangeButton(new TyphoonSprite(), 0.3F,
                new TyphoonSprite().charging,
                "弹道测算",
            "_-_ 为人形安装了弹道测算装置，当你被高能武器瞄准时会_提示其弹道_。\n"+
            "_-_ 这意味着，玩家将更不易被_激光_类敌人在_视野外斩杀_。\n\n"+
            "_-_ 若未来加入了更多此类敌人，此能力将同步更新。"
        ));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(), 0.8F,
                "商店",
            "_-_ 在25层加入了_商店_，为玩家提供先前在深入下层时缺少的补给。\n"+
                    "_-_ 该商店同样需要玩家完成_一些任务_以开启。(需要完成什么任务呢？)\n"+
                    "_-_ 该商店额外配置了_抽奖机_，玩家可以在此消耗掉多余的钻石来获取随机优质资源。\n"+
                    "_-_ 该商店允许出售_带等级_的强力武器，但价格十分_高昂_！\n" +
                    "_-_ 修复了部分情况下商店有空地格但_出售失败_的问题。\n" +
                    "_-_ 修复了出售给商人的物品与_商人重叠_而无法购回的BUG。\n" +
                    "_-_ 格琳娜进入更深入的楼层开店后，前面楼层的商店将由其幻影负责。"
        )); 

        ArrayList<?> ArtifactChange = new ArrayList<>(Arrays.asList(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null), new HornOfPlenty(), new LloydsBeacon(), new DriedRose()));
        ArrayList<String> ArtifactBody = new ArrayList<>(Arrays.asList(
                "_-_ 充能逻辑调整：当遗物单回合回复充能数量大于1点的情况下，将会一次性全部生效，而非一回合一点。\n"+
                        "_-_ 依赖经验升级的遗物，在获取经验的时候将会消耗经验尽可能的升级，而非只升一级然后暂存剩下的经验。\n"+
                        "_-_ 由于现在加入了查种器，所以顺便将遗物的生成顺序依据种子固定化了。",
                "_-_ 修复了炊事妖精将不同算法的充能计数混用导致瞬间满充能的bug。",
                "_-_ 新增被感染的效果，当玩家装备被感染的空降妖精时，在玩家进行攻击或者被攻击的时候，空降妖精可能会将被攻击的单位视为要保护的目标，令其_随机传送_！\n"+
                        "_-_ 修复了携带未满级空降妖精击杀部分boss时，空降妖精没有升级的bug。\n"+
                        "_-_ 现在空降妖精仅能从刽子手的掉落物或者嬗变遗物的方式获得。",
                "_-_ 现在未完成ST-AR15的任务也能装备刺刀并在正常楼层生成心智碎片以进行升级，召唤ST-AR15及为其穿戴装备依旧需要完成任务。"
        ));
        changes.addButton(new ChangeButton(ArtifactChange,
                "遗物改动",
            ArtifactBody
        )); 

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new CrabSprite(), 1,
                new CrabSprite().attack, true,
                0xFF8800,
                "烈焰精英",
            "_-_ 削弱了_烈焰精英_类敌人的点燃能力，烈焰精英将不能点燃站在水面上的单位。\n"+
            "_-_ 同时，当_烈焰精英_被击败时，其不再会点燃_周围水格_。"
        ));

        changes.addButton(new ChangeButton(new CyclopsSprite(), 1,
                new CyclopsSprite().attack,
                "独眼巨人A型",
            "_-_ 较大幅度的削弱了_独眼巨人A型_过于强悍的_闪避与命中_数值。\n"+
            "_-_ 尽管玩家不再需要在战斗中对独眼巨人望而生畏了，但并不意味着独眼巨人没有战斗力了，请注意。"
        ));
    }

	public static void add_0_5_6_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.6", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new HeroSprite(HeroClass.MAGE, 4),
                0.8F,
                "职业强化",
        	"_-_ 角色_G11_的职业_鹰眼_获得强化\n"+
            "_-_ 该转职本该实现的_装载不同子弹获得不同近战攻击效果_得到了正常实现，以此保证了两个转职实用性的平衡。\n"+
            "_-_ 同时，天赋_蓄能打击_获得同步强化，在增强_子弹效果_的同时还会增加特殊_近战攻击_强度。"
        ));

        changes.addButton( new ChangeButton(new HeroSprite(HeroClass.ROGUE, 4),
                0.8F,
                "天赋强化",
        	"_-_ 角色_UMP9_的天赋_行窃预知_获得强化。\n"+
            "_-_ 该天赋+1时发现隐藏房间的概率提高为_67%_，+2时的概率提高为_100%_。"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null),
                "新型火控元件",
		"_-_ 可以将外骨骼进行升级的_新型火控元件_现在可以在不同的外骨骼间进行转移。\n" +
		"_-_ 转移后原外骨骼将直接被_销毁_，而新外骨骼成为_人形专属配件_。\n"+
		"_-_ 将_新型火控元件_进行转移时，被加强的外骨骼将直接被鉴定。"
		));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ElpheltSprite(), 0.8F,
                new ElpheltSprite().charging,
                "艾尔菲尔特",
        	"_-_ 修复了BOSS_艾尔菲尔特_战斗地图中的_无敌点位_。\n"+
        	"_-_ 艾尔菲尔特在遇见特殊的_无法接近玩家_的情况时，将会进行_穿墙索敌_请避免故意卡_无敌点位_。"
        ));

        changes.addButton(new ChangeButton(new GolemSprite(), 0.5F,
                new GolemSprite().attack,
                "蝎甲兽",
        "_-_ 敌人_蝎甲兽_的生命值上限提高，且对单次高额伤害获得伤害阈值。\n" +
		"_-_ 这意味着蝎甲兽将强制性需要多次攻击才能击败。"
		));

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "游戏优化",
            "_-_ 更正了_测试模式_怪物生成器的部分错误。\n"+
			"_-_ 实现了先前没能实现的GSH18天赋_元气一餐_的效果。\n"+ 
			"_-_ 实现了先前没能实现的UMP45天赋_刻印转移_的效果。\n"+
			"_-_ 调整了部分一次性消耗品_被消耗_的时机，避免因为各种_意外_导致物品未生效便消失。\n"+
			"_-_ 优化了代码，避免因为部分角色的_特殊原因_导致_固定种子_出现不同的物品_分化_。\n"+
			"_-_ 修复了部分情况下，_切割者_贴墙死亡会丢失部分掉落物的BUG。\n"+
			"_-_ 修复了部分情况下，_切割者_会掉落相同遗物，相同遗物可以同时使用的BUG。\n"+
			"_-_ 修复了本该初始中立的怪物，在循环刷新中以敌对态度出现的问题。\n"+
			"_-_ 优化了测试模式部分测试工具的_使用手感_,消除了_跳层器_的下楼延迟。\n"+
			"_-_ 优化了了_种子系统_现在可以更自由的使用种子系统了。\n"+
			"_-_ 迭代了调用转换方法，并兼容了双端错误报告。\n"+
			"_-_ 将_空降妖精_与_曼蒂装甲_加入了遗物嬗变池。\n"+
			"_-_ 将_标签系统_加入了游戏，玩家可以给物品自定义标签以便利记忆物品情况。\n"+
			"_-_ 进行了_文案_优化。\n"+
			"_-_ 进行了_贴图_优化。"
        ));
        
        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		 changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CANDY_CANE, null),
                 "圣诞节！",
		"_-_ 全面扩展了_圣诞节彩蛋_！\n" +
		"_-_ 圣诞节期间将出现_限时稀有敌人_，掉落_限时稀有奖励_，同时有_隐藏徽章_等待你的收集！\n\n"+
		"_-_ 注：圣诞节期间指每年12月24-25号前后各7天（共14天）时间"
		));

		changes.addButton( new ChangeButton(Icons.get(Icons.WARNING),
                "历史错误报告收集系统",
        	"_-_ 加入了_历史错误报告_系统，可自动保留近期的错误报告，点击主页面右下角按钮可以进入。\n"+
        	"_-_ 此系统旨在玩家可以便利的提供给开发者未能及时保留的错误报告，便于BUG的溯源与修复。\n"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY,null),
                "水晶房间",
        "_-_ 需要三把钥匙才能完全开启的_水晶房间_增新了变体。\n"+
        "_-_ 此类房间将有概率变体为共有6个小间，玩家可以进行6选3猜选奖励的特殊房间。"
        ));

		changes = new ChangeInfo("测试条目", false, null);
        changes.hardlight( CharSprite.WARNING );
        changeInfos.add(changes);

        ArrayList<String> pageMessage0 = new ArrayList<>();
        {
            pageMessage0.add(
                    "_-_ 这一条目是为了测试更新公告新的新呈现形式——翻页模式而写在这里的，我会在这里写一些不知所云的东西以测试稳定性。\n" +
                            "_-_ 卡尔卡诺M91/38小姐说的话大多是相反的假话，但是你该如何判断哪一些是正面的真话呢？\n" +
                            "_-_ 虽然AUG小姐喜欢在雨中散步以抒发哀思，但是你不能真的让她一直淋雨，至少给她打把伞……\n" +
                            "_-_ 不要给HK416提供烈性酒！你不会想知道这条经验是怎么总结出来的。\n"
            );
            pageMessage0.add(
                    "_-_ 艾莫号是指挥官所拥有的大型陆地移动单元，它不能展开成固定基地！\n" +
                            "_-_ 要找什么东西先自己动手，而不是直接去问马卡洛夫，否则她会告诉你“没有，滚吧”。\n" +
                            "_-_ M200小姐是一个身高足够捅破云层，喜欢把脚架在矮个子人形的肩膀上，内置了完整的维修手册和烹饪手册的全能型人形。\n"
            );
            pageMessage0.add(
                    "_-_ 和索米分享耳机的时候一定要小心，如果你不想耳膜爆炸的话……\n" +
                            "_-_ 萨曼莎·肖博士真是一个武器天才，可惜……\n" +
                            "_-_ 格里芬不是一个地方，而是一群人，只要他们依然坚守信仰，格里芬就依然存在。\n"
            );
            pageMessage0.add(
                    "_-_ 曾经我们执握烽火，守望寂寂黎明\n" +
                            "_-_ 现在我们高擎火炬，只为点燃这片理想的天空\n" +
                            "_-_ 此致，更新世界的锋芒！\n"
            );
        }
        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY, null),
                "测试条目", pageMessage0));
    }



    public static void add_0_5_5_4_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.5.4", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

       changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARMOR_SCALE, null),
               0xFF4400,
               "狱火附魔",
		"_-_ _狱火附魔_强化了其能力，使其回归到过往版本的强度\n" +
		"_-_ _狱火附魔_除了完全免疫火焰的能力外，处于燃烧状态还将概率获得_护盾_。"
		));

        changes.addButton(new ChangeButton(new ShopkeeperSprite(),0.8F,
                "商店",
            "_-_ 格琳娜商店与P7商店现在可以以_10倍售价_买回_卖出的物品_，玩家卖出的商品将会出现在_玩家周围的空地上_并作为特殊的商店货物存在。\n"+
			"_-_ 格琳娜与P7在_首次遭遇_不利情况时将会进行警告，_警告后_才会因为各种不利情况而_撤离_。\n"+ 
			"_-_ 以_信用卡_进行_盗窃失败_依然会导致商人直接撤离！"
        ));  

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.PICKAXE, null),
                "测试模式",
		"_-_ 测试模式道具_流形护盾_实现了其计划中应有的效果。\n" +
		"_-_ 启动_流形护盾_后，玩家将进入_无敌_状态，直至关闭。\n\n"+"_-_ 测试模式加入道具_玩家等级调整器_、_装备等级调整器_、_装备遗忘器_。"
		));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.TELE_GRAB, null),
                "念力晶柱",
		"_-_ _念力晶柱_取回掉落地面的物品的能力获得了强化。\n" +
		"_-_ 现在_念力晶柱_将可以取回一格内_尽可能多_的全部物品，直到玩家_无法拿取_。"
		));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CLEANSING_DART, null),
                ".50口径子弹",
		"_-_ _净化子弹_新增对敌使用效果为清除目标_正面效果_，并_消除对玩家仇恨_。\n" +
		"_-_ _激素子弹_新增对敌使用效果为_令目标残废_。"
		));

        changes.addButton( new ChangeButton(new TalentIcon(Talent.RUNIC_TRANSFERENCE),
                0.9F,
                "刻印转移",
		"_-_ UMP45角色2层天赋_刻印转移_效果改动，现在破碎的外骨骼配件_在角色6级后_就可以携带_普通或稀有护甲刻印_。\n" +
		"_-_ _刻印转移_+1效果改动，破碎的外骨骼配件可以携带_全稀有度的护甲刻印_。\n"+
		"_-_ _刻印转移_+2效果改动，破碎的外骨骼配件可以从带刻印的护甲上_吸取刻印并保留和转移_。"
		));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CROWN, null),
                "外骨骼升级组件",
		"_-_ 现在被外骨骼升级组件_升级的护甲_不再具有唯一性。\n" +
		"_-_ _升级的护甲_可以_卸下升级组件_并将组件安装到另一件护甲，但会_破坏原有的护甲_。"
		));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new BlacksmithSprite(),0.8F,
                "游戏优化",
            "_-_ 现在玩家可以在_排行榜界面_查看已完成游戏的_种子码_了，但该版本前完成游戏的种子码将默认为9个A。\n"+
			"_-_ 修订了数处文本错误。\n"+ 
			"_-_ 将_梦想家_与_末日魔犬_的贴图加入游戏。\n"+
			"_-_ 将_传送器_的贴图优化。\n"+
			"_-_ 优化了部分地图的_渲染模式_，避免了_贴图重叠_的情况发生。\n"+
			"_-_ 差分了_西蒙诺夫_与_带护甲的西蒙诺夫_的贴图与介绍文本，防止玩家无法正确估算其战斗力。\n"+
			"_-_ 为离开兔子层增加了_提示文本_，防止有人在此_遗漏重要物品_。\n"+
			"_-_ 将_M99_武器恢复为破碎地牢'关刀'\n"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new GolyatPlusSprite(),0.8F,
                "歌利亚Plus",
        "_-_ 增加新敌人_歌利亚Plus_作为普通_歌利亚_的精英版本。\n" +
		"_-_ _歌利亚Plus_移动速度较慢但_自爆威力极高_，请_不要近距离击杀！_\n"+
		"_-_ _歌利亚Plus_将掉落_断腿的歌利亚_,可以破坏墙体和地面，且能_直接破坏任何容器_并获取内部物资。"
		));

        changes.addButton(new ChangeButton(new GnollSWAPSprite(),0.8F,
                "切割者",
        "_-_ 增加新敌人_切割者_作为_巡游者_的精英版本。（从代码原理上来说是这样的）\n" +
		"_-_ _切割者_攻击距离较远且初始处于中立，其_携带了大量的物资_，在受到威胁后，切割者将_转变为敌对状态！_"
		));

	}

    public static void add_0_5_5_3_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.5.3", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.UMP45, null),
                "HK UMP45",
		"_-_ 武器_UMP45_实验性的加入了_烟雾弹_技能\n" +
		"_-_ _烟雾弹_可以短暂阻挡敌人的视野，但需要较长的_冷却时间_。"
		));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.C96, null),
                "毛瑟C96",
		"_-_ 武器_毛瑟C96_的_照明弹_技能回归，并且变为主动技能\n" +
		"_-_ _照明弹_能够为自身提供短暂的照明，以在黑暗环境中拥有更大范围的视野。"
		));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_DISARM, null),
                "晰除符石",
		"_-_ 将_拆除符石_替换为了_晰除符石_，由_疫苗磁盘_拆解而来。\n" +
		"_-_ _晰除符石_保留丢出_拆除范围陷阱_的能力，并增加了可使用以_鉴定物品_是否带有_病毒_的能力。"
		));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "地图种子编辑器",
			"_-_ 新增了_地图种子编辑器_，使用编辑器可以锚定特定地形的地图进行游戏了！\n"+
			"_-_ 解锁方式与_挑战_的解锁方式相同，完成15层的冒险即可解锁\n"+
			"_-_ 地图种子编辑器解锁后，位于_开始游戏_按钮的左侧"
		));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "BUG修复",
            "_-_ 修复了_人权组织成员_贴图错位的问题。\n"+
			"_-_ 鹰眼的_充能秘术_天赋1/2/3级将不再提供4回合遗物充能。\n"+ 
			"_-_ _虹色傀儡_和_增压器_将受益于_树肤_效果。\n"+
			"_-_ _荆棘_和_反斥_附魔不再会对盟友造成伤害了。\n"
        ));  
    }

    public static void add_0_5_5_2_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.5.2", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RETURN_BEACON,null),
                 0xFFFFFF,
                "返回结晶",
        "_-_ _返回结晶_的传送功能不再因为周围存在敌人而被_禁用_。\n"+
        "_-_ 取而代之的代价是传送需要_多消耗一个回合_。\n"
        ));


        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.CRYSTAL_KEY,null),
                "水晶房间",
        "_-_ 需要三把钥匙才能完全开启的_水晶房间_内的奖励进行了优化\n"+
        "_-_ 房间内的_少量钻石_奖励改为了_随机数量_的钻石奖励\n"+
        "_-_ 房间内的随机_符石/种子_奖励改为了_随机药水_奖励\n"+
        "_-_ 房间内的随机_药水/磁盘_奖励改为了_随机磁盘_奖励\n"+
        "_-_ 房间内的随机_武器/外骨骼_奖励改为了_稀有药水/磁盘/符石_奖励"
        ));

        changes.addButton(new ChangeButton(new SentryRoom.SentrySprite(), 0.8F,
                "指南针",
        	"_-_ _指南针_现在不会对心智不完整的人形发动攻击了。\n"+
        	"_-_ 这意味着现在如果意外死亡在_哨卫房间_里，玩家不会因为无法取回_遗物_而变为死局了。"
        ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new CrabSprite(), new CrabSprite().run,
                true,
                0x0000FF,
                "索敌精英",
        	"_-_ 优化了_索敌精英_类敌人的索敌逻辑，索敌精英将不能越过单位直接攻击玩家。\n"+
        	"_-_ 这意味着现在不会在被围困时被_索敌精英_类敌人从远处直接攻击到了。"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MACCOL,null),
                "大麦味可乐",
        "_-_ _大麦味可乐_重新回到了游戏！\n"+
        "_-_ 现在商店中的_压缩饼干_将有概率变为_大麦味可乐_！"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,null),
                "灵刀·樱吹雪",
			"_-_ _灵刀·樱吹雪_使用居合击杀后获得的升级冷却不会再因为全面净化合剂而停止计数了。\n"
		));

        changes.addButton( new ChangeButton(Icons.get(Icons.WARNING),
                "在线更新系统重置",
        	"_-_ 旧版在线更新系统因为一些原因_不再被支持_，新的在线更新系统已经重新加入游戏！\n"+
        	"_-_ 新版在线更新系统需要以本版本为基础，因此本版本必须_手动下载_安装包并且进行更新。\n"
        ));

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "BUG修复",
            "_-_ 现在艾尔菲尔特被击败后，将不会清理掉地面的_掉落物_了。\n"+
            "_-_ _ND-B子弹配件_击败艾尔菲尔特将不再会导致闪退。\n"+
            "_-_ 艾尔菲尔特被击败后，玩家的_盟友_将不再会被清除。\n"
        )); 
        
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GLAIVE, null), "M99",
            "_-_ M99的攻击速度略微降低，但射程略微增加。\n"
        ));
	}
	
	public static void add_0_5_5_1_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.5.1", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.WARNING),
                "紧急修复",
			"_-_ 紧急修复了未能实现的更新内容\n\n" +
			"_-_ _圣盾_现在将可以正常掉落_核心装甲_了！\n"+
			"_-_ 正式取消了挑战_饥饿游戏_中食物减半的特性。\n"+
			"_-_ 地图贴图优化：下水道和监狱层的贴图现在更加清晰了！\n"+
			"_-_ 文本优化：优化部分文本，后续会继续优化。\n"
		));

		changes.addButton( new ChangeButton(new SpinnerCatSprite(),0.8F,
                new SpinnerCatSprite().attack, true,
                "圆头耄蛛",
			"_-_ 为稀有敌人_耄耋_增加了稀有掉落物，以符合其_稀有_的特性。"
		));
	}

    public static void add_0_5_5_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.5", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_CRIMSON , null),
                "魔药改动",
        	"_-_ 将所有魔药可转换为的炼金能量提高为12点\n"+
        	"_-_ 现在分解不需要的魔药能获得更高的炼金能量收益"
        ));

    changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "游戏优化",
            "_-_ 对部分文本进行了修改和优化，使符合实际情况。\n"+
			"_-_ 对少量物品贴图进行了优化。\n"+
			"_-_ 我们正在努力的修正其它已知的BUG以保障玩家的良好游戏体验。\n"
        ));  

        changes.addButton(new ChangeButton(new ElpheltSprite(), 0.8F,
                new ElpheltSprite().attack, true,
                "BUG修复",
        	"_-_ 修复了一系列和_艾尔菲尔特_有关的BUG\n\n"+
        	"_-_ 在与艾尔菲尔特的战斗中退出游戏，而后返回游戏将不会再崩溃了（但依然请不要在战斗中多次退出重进）\n"+
        	"_-_ 在特殊条件下，使用刺客斩杀艾尔菲尔特将不会再导致卡关了\n"+
        	"_-_ 一些情况下BOSS血条无端消失的问题得到了修复"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,null),
                "灵刀·樱吹雪",
			"_-_ _灵刀·樱吹雪_技能改动，使用技能斩杀敌人将有50%-10%的阶梯概率获得1级特殊升级，最多升级5次。\n"+
			"_-_ 每次使用技能后，斩杀升级将进入100回合冷却，无论是否实现了斩杀。"
		));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MEAT_PIE,null),
                "挑战：饥饿游戏",
			"_-_ 挑战_饥饿游戏_优化，降低了不必要的游戏难度\n\n"+
			"_-_ 移除了挑战中所有食物生成减半的特性，仅保留食物回复饱食度降低为原来的33%\n"+
			"_-_ 玩家将不需要再面对_饥饿游戏_挑战中理论饱食度获取率降低为正常的87%的极端饥荒环境。"
		));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"),
                false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new HeroSprite(HeroClass.GSH18, 4),
                0.8F,
                "新角色：GSH18",
        	"_-_ 新角色_GSH18_的加入\n\n"+
            "_-_ GSH18虽然较为脆弱，但拥有多样化的获取护盾的能力，且在拥有护盾时会获得额外能力。\n"+
            "_-_ 该角色_尚不完善_，仅供_测试体验_。"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.AN94, null),
                "AN-94",
			"_-_ 加入了新武器_AN-94_，这把4阶武器拥有较远的攻击距离\n" +
			"_-_ 这把武器自然刷新权重较低，且正在进行平衡性完善。"
		));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "0层：前进营地",
			"_-_ 将_前进营地_加入了游戏（虽然现在还只是毛坯）\n\n" +
			"_-_ 未来在前进营地中可以进行局外养成，以及未来更多样化的游戏体验。\n" +
			"_-_ 达成_完美结局_成就后，同步解锁0层的准入权限。"
		));

		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.KILL_ELPHELT.image),
                "成就徽章",
			"_-_ 击败_艾尔菲尔特_，获得成就勋章，记录您的荣耀！"
		));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CAPE, null),
                "核心装甲",
			"_-_ 重新加回了_核心装甲_，击败_圣盾_将有25%的概率掉落。\n\n旧版译名：_曼蒂装甲_(原破碎_荆棘披风_)" 
		));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REDBOOK, null),
                "语录书",
            "_-_ 考虑到语录书过高的伤害足以蒸发BOSS为56-1式带来了_过高的强度_，对语录书进行了_削弱_\n"+
            "_-_ 现在语录书对BOSS造成的直接伤害有所降低。"
        ));

    }

    public static void add_0_5_4_5_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.4.5", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_ROSE3, null),
                "某人的刺刀",
			"_-_ 当_AR15_被召唤出期间，刺刀通过收集心智碎片获得了升级，AR15将获得生命值回复。"
		 ));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGNUMWEDDING, null),
                "BUG修复",
        	 "_-_ 修复了_艾尔菲尔特_系列武器导致游戏崩溃的一系列问题。\n"
        	));
    }

    public static void add_0_5_4_3_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.4.3", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

    changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.LAR, null),
                "灰熊MK-V",
			"_-_ _灰熊MK-V_减少了1点使用所需的基础力量要求。\n" +
			"_-_ 略微提高了升级提供了伤害提升。"
		));
        
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.NTW20, null),
                "NTW-20",
            "_-_ 为_NTW-20_增加了武器技能，在_瞄准模式_下将可以更快的进行强力攻击\n"+
            "_-_ _瞄准模式_下，攻击的伤害区间为武器最高伤害的85%-120%，且精准度提升"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REDBOOK, null),
                "袖珍本",
            "_-_ 新增了可以消耗1点充能自己阅读袖珍本，同时获得短时间的祝福效果\n"+
            "_-_ 对袖珍本升级条件进行了补充说明，便于玩家理解并实现遗物升级"
        ));
       
    changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "BUG修复",
            "_-_ 修复了点击排行榜导致存档损坏的BUG。\n"+
			"_-_ 修复了AR15和M16A1对话文本错乱的BUG。\n"+
			"_-_ 修复了移动端击败艾尔菲尔特可能出现崩溃的BUG。\n"
        ));  

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CHALICE1, null),
                "增压器",
            "_-_ 对增压器进行下次充能需要多少能量供给增加了实时文本提示\n"+
            "_-_ 现在不熟悉的游戏机制的玩家将不会因为贪增压器而_Game Over_了（大概）"
        ));
        
    changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FOOD_POUCH, null),
                "野餐篮",
            "_-_ 增加了_野餐篮_，用来专门存放食物类物品，同时优化pc端背包按钮显示\n"
        ));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_BEACON, null),
                "空降妖精",
            "_-_ 重新加回了_空降妖精_，击败侩子手后将有_12.5%_的概率掉落"
        ));    

    changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

        changes.addButton( new ChangeButton(new HeroSprite(HeroClass.TYPE561, 5),
                0.8F,
                "56-1式",
            "_-_ _56-1式_重新获得了在饥肠辘辘状态下_力量-1_的特性\n"+
            "_-_ 该特性仅在角色力量大于12时生效"
        ));
        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GLAIVE, null), "M99",
            "_-_ 将_M99_的实际能力改为与描述文本相同，现在无法进行偷袭。"
        ));
    }    

    public static void add_0_5_4_1_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.5.4.1", true, "");
        changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ElpheltSprite(),0.8F,
                new ElpheltSprite().run,
                "BUG修复",
        	"_-_ 将_【少女前线X罪恶装备/苍翼默示录】_相关联动内容作为彩蛋加入游戏，玩家在开启_深入敌腹_挑战后进行闯关即可体验！\n"
        ));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GSH18,null),
                "新增游戏内容",
			"_-_ 在测试模式加入了_地块编辑器_，虽然有些繁琐，但玩家可以自定义地形了。\n"+
			"_-_ 增加了角色饱食度指示UI，现在角色的饱食度数值可视化了。\n"+
			"_-_ FNC带着格里芬的补给来到了地牢！与其对话将能获取一些有趣的补给！\n"+
			"_-_ 新增了一份节日彩蛋！\n"+
			"_-_ 全新武器_GSh-18_加入。\n"
		));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATAXE,null),
                "灵刀·樱吹雪",
			"_-_ 灵刀·樱吹雪添加了新技能，使其不再只是一把‘刀’\n"
		));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

        changes.addButton(new ChangeButton(new BlacksmithSprite(), 0.8F,
                "优化游戏体验",
			"_-_ 修复了部分情况下新建游戏会闪退的BUG。\n"+
			"_-_ 优化了_泛黄的袖珍本_，增强了其与其它遗物的相关性。\n"+
			"_-_ 修复了第6大区_远处的井_贴图错误的BUG。\n"+
			"_-_ 因为一些特殊目的，删除了_塌方陷阱_。\n"+
			"_-_ 强化了_Type 56-2_武器榴弹的伤害。\n"+
			"_-_ 修正了大量文本错误，对部分文本进行了优化。\n"
        ));

		changes.addButton( new ChangeButton(new TyphoonSprite(),0.3F,
                new TyphoonSprite().run,
                "提丰",
			"_-_ 将_提丰_的生成率改回了在全局28-29层中有1%刷新率，而不是之前的1%概率替换28-29楼层初始怪组。\n"+
			"_-_ 所以_提丰_现在在地牢中出现的频率会更高了。"
        ));
    }    

    public static void add_0_5_3_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.3", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN562, null),
                "游戏改动",
			"_-_ 字体渲染：现已可使用系统字体和像素字体。\n"+
			"_-_ 主界面图标调整\n"+
			"_-_ 调整了宝箱怪的贴图，现在更容易辨识了\n"+
			"_-_ 新功能：在线更新系统\n"+
			"_-_ 减小了56-2榴弹的爆炸和伤害范围\n"+
			"_-_ 调整了56-1和56-2的弹道逻辑\n"+
			"_-_ 调整了56-1快捷栏的CD显示：现在更加明了了\n"+
			"_-_ 修改了\"咸派的认可\"天赋：提高了其后期收益\n"+
			"_-_ 增强了鉴定符石，现在不需要猜对也能使用两次\n"+
			"_-_ 现在炼化物品时能顺便将其鉴定了\n"+
			"_-_ 修改了矮人层怪组，加入了蚁群，强度更合理了\n"+
			"_-_ 将坠落音效和死亡音效换成原来的\n"+
			"_-_ 修改了军方小队boss层地形，防止\"空间信标\"返回进去"
		));

		changes.addButton(new ChangeButton(new SpinnerSprite(),
                Messages.get(ChangesScene.class, "bugfixes"),
			"_-_ 更换了第三boss层遗漏的地块贴图\n"+
			"_-_ 修复了56-2在楼层封锁时CD显示不正确的问题\n"+
			"_-_ 恢复G11的初始伤害为1-8\n"+
			"_-_ 修复了楼层封锁时56-1的CD不受限制的问题\n"+
			"_-_ 更正了56-1的介绍文本\n"+
			"_-_ 补充了被猎鸥远程杀死时缺少的文本\n"+
			"_-_ 修复了眼镜(先见护符)相关的文本问题\n"+
			"_-_ 修复了\"不动如山\"的文本问题(现在不再是小南娘了^-^)\n"+
			"_-_ 修复了徽章\"全能大师\"的文本问题\n"+
			"_-_ 修复了\"多面手\"与\"全面手\"的文本问题\n"+
			"_-_ 修复了徽章文本问题:“戒指研究员”-->“瞄准镜研究员”,“瞄具研究员”-->“药水研究员”\n"+
			"_-_ 修复了魔法免疫时使用卷轴的文本问题\n"+
			"_-_ 修复了虚弱buff的文本问题\n"+
			"_-_ 修复了HK416两个绒布袋的问题\n"+
			"_-_ 修复了HK416浆果的文本问题\n"+
			"_-_ 修复了\"斥候\"的动量在角色死亡后没有清除的bug\n"+
			"_-_ 修改了矮人城地块贴图，现在不会混淆能种草的地和不能种草的地了\n"+
			"_-_ 修复了木星和钢狮的粒子效果在某些情况下不能被正常消除的问题\n"+
			"_-_ 修改了矮人城草地贴图不对的问题\n"+
			"_-_ 补充、修改了元素风暴的文本\n"+
			"_-_ 修复了军方小队boss层两个梯子的问题"
		));
	}

    public static void add_0_5_2_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.2", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(new SpinnerSprite(),
                Messages.get(ChangesScene.class, "bugfixes"),
		"_-_ 现在鼠王护甲能正常使用了，561也可以使用鼠王护甲\n"+
				"_-_ 修改了Ppsh-41的贴图\n"+
				"_-_ 更改了侦查中枢的显示偏移，现在视觉上不会阻挡其他怪物了\n"+
				"_-_ 修复了G11 虹卫，镜像 攻击崩溃的bug\n"+
				"_-_ 修复了骷髅文本错误问题\n"+
				"_-_ 修复了Vector冲锋枪buff未生效的问题\n"+
				"_-_ 修复了元素风暴不能使用的问题\n"+
				"_-_ 修复了法杖(子弹)鉴定时的文本\n"+
				"_-_ 修复了部分文本问题\n"+
				"_-_ 删除了G11，将其功能合并到magesstaff，修复了”法杖回收“天赋不能正常发挥作用的问题\n"+
				"_-_ 调整了坠落音效的音量大小为原来的2/3\n"+
				"_-_ 修复了G11的装弹强化天赋不能正常触发的bug，同时调小了死亡音效的音量\n"+
				"_-_ 修复了561饭是钢天赋不能正常触发的问题\n"+
				"_-_ 现在鼠王护甲能正常使用了，561也可以使用鼠王护甲了\n"+
				"_-_ 修复了护卫者无限掉落血瓶的问题，现在上限为5"
		));
	}

    public static void add_0_5_1_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.1", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(BadgeBanner.image(Badges.Badge.KILL_CALC.image),
                "游戏优化",
			"现在只需要击败15层计量官Boss，即可解锁挑战。"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new SpinnerCatSprite(),0.8F,
                new SpinnerCatSprite().run,
                "特别稀有怪",
			"现在 _提丰_仅在27~29层有1%的生成，且生成时会有文本提示，并且限制生成一个。\n\n" +
				"而_耄耋_生成概率仅有0.2%，在矿洞层有概率生成。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN562, null), "56-2式 武器优化",
			"现在装填冷却会显示在快捷栏中，且提示文本也有具体的回合冷却显示。"));

		changes.addButton( new ChangeButton(new GhoulSprite(), 0.7F,
                "五区敌人生成调整",
			"_-_ 21层 2护卫者\n" +
				"_-_ 22层 3护卫者1龙骑\n" +
				"_-_ 23层 4护卫者2龙骑1木星\n" +
				"_-_ 24层 4护卫者3龙骑2木星\n\n" +
				"注意此代表敌人生成权重，实际数量可能会更高"));

		changes.addButton(new ChangeButton(new SpinnerSprite(),
                Messages.get(ChangesScene.class, "bugfixes"),
			"以下Bug已被修复:\n" +
				"_-_ 修复了所有无味果相关的文本bug\n" +
				"_-_ 修复了飞槌图标错误问题\n" +
				"_-_ 修复梦夜花(未使用的老版本)，使其成为魔皇草(当前版本正在使用)\n" +
				"_-_ 修改了.50子弹贴图缺失问题\n" +
				"_-_ 修复了所有.50子弹文本错误问题\n" +
				"_-_ 修复了所有投掷武器耐久度相关的文本问题\n" +
				"_-_ 修复了所有not cursed相关的文本问题\n" +
				"_-_ 修复了所有未鉴定的瞄准镜的文本问题\n" +
				"_-_ 修复了所有奥术聚酯相关的文本(顺带修复了法杖和魔法免疫的文本)\n" +
				"_-_ 检查了所有卷轴，戒指，子弹，阅读，充能相关文本，修改了其中不合理的部分\n" +
				"_-_ 检查了所有老魔杖，能量相关文本，修改了其中不合理的部分\n" +
				"_-_ 检查了所有\"战斗G11\"\"术士\"相关文本，修改了其中不合理的部分\n" +
				"_-_ 为痛击者添加了关于精神集中buff的介绍\n" +
				"_-_ 修复了耄耋蜘蛛的bug\n" +
				"_-_ 优化了猎头蟹和耄耋蜘蛛的贴图位置，现在更难被墙之类的挡住了\n" +
				"_-_ 修复了投石索和苦无的贴图错误问题\n" +
				"_-_ 修复了提丰死亡崩溃的异常\n" +
				"_-_ 修复了投石索贴图大小不对的问题\n" +
				"_-_ 修复了炊事妖精的文本问题\n" +
				"_-_ 修复了能使用未解锁角色的bug\n" +
				"_-_ 修复562充能异常，以及修复投掷武器描述显示错误" ));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new HeroSprite(HeroClass.WARRIOR, 5),
                0.7F,
                HeroClass.WARRIOR.title(),
			"UMP-45 初始物品调整：\n\n" +
				"_-_ 初始获得一瓶_力量药水_"));
    }

    public static void add_0_5_0_Changes( ArrayList<ChangeInfo> changeInfos ){
		ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(Icons.get(Icons.GIRLPDS),
                0.5F,
                "新版少前地牢制作组",
			"_-_ 2025新版少前地牢是基于破碎1.2.3底层进行开发\n" +
				"_-_ 这是一个全新的开始，我们会在未来带来更多内容\n" +
				"_-_ 感谢所有支持我们的指挥官，祝各位新版游玩愉快！"));

		changes.addButton( new ChangeButton(new DM300Sprite(), 0.65F,
                "Boss调整",
			"由于少前地牢新版本基于的破碎地牢底层版本改变，大部分原有BOSS的战斗机制也发生了改变\n" +
				"\n" +
				"_-_ 衔尾蛇：一阶段陷阱会逐渐隐形，二阶段将利用更多样化的投掷物攻击\n\n" +
				"_-_ 计量官：改为三段式战斗，每一阶段结束会进入狂暴，获得强化的同时需要破坏特定单位才能进入下一阶段\n\n" +
				"_-_ 破坏者：改为三段式战斗，所能召唤的敌人更加多样化，且召唤的敌人与破坏者本身有着某种链接，不断击败召唤的敌人才能最终击败破坏者\n\n" +
				"_-_ 军方小队：它还是大号敌人，但是在挑战模式下获得额外增强\n\n" +
				"_-_ 主脑：替换M4成为了新BOSS，改为五段式战斗，所能召唤的精锐铁血变得更加多样化，且自身攻击能力也得到了加强"));

		changes.addButton( new ChangeButton(Icons.get(Icons.LIBGDX),
                "LibGDX引擎系统",
			"_-_ 游戏的文本渲染器现已采用libGDX Freetype。这与现有文本几乎完全相同，但略微更加清晰，且具有平台独立性，效率也大幅提升!\n\n" +
				"_-_ 文字渲染是最后一段依赖于Android的代码，因此游戏的核心代码模块（约占其代码的98%）现在正在作为通用代码进行编译，而非Android专属代码！\n\n" +
				"_-_ 基于LibGDX引擎，现在游戏将更加流畅，并更容易兼容高版本设备系统"));


		changes.addButton( new ChangeButton(Icons.get(Icons.WARNING),
                "错误日志系统",
			"老版本一遇到崩溃，游戏就闪退了？\n\n" +
				"不用担心，新版本我们实装了自己的错误界面。\n\n" +
				"如游玩时遭遇游戏崩溃，请将错误报告复制后@管理员进行发送。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.AUDIO),
                "音乐系统实装",
			"老版本音乐千篇一律，打起来枯燥无味？\n\n" +
				"不用担心，新版本制作组精选了多首少前音乐，让你的游戏更加有乐趣\n\n" +
				"P.S. : 这样会导致安装包有一点大。\n\n" +
				"另外，如果你喜欢某首音乐，可在关于界面找到具体的歌曲名字哦。"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "新挑战：深入敌腹",
			"感觉Boss太简单，想挑战更高难度？\n\n" +
				"深入敌腹会满足你的需求，所有的Boss都会被加强。\n" +
				"你将会遭遇更加困难的作战，祝你好运！\n\n" +
				"此挑战在破碎原型为：绝命头目"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "新挑战：精英强敌",
			"感觉敌人太简单，想挑战更高难度？\n\n" +
				"精英强敌会满足你的需求，部分敌人都会被加强\n" +
				"你将会遭遇更加困难的作战，祝你好运！\n\n" +
				"此挑战在破碎原型为：精英强敌"));

		changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_ON),
                "特殊挑战：测试时间",
			"玩累了？想体验上帝模式？\n\n" +
				"测试时间让你深度探索游戏，拥有完整的测试工具，\n\n" +
				"但是此挑战不计入挑战数量，也无法在这里面获得成就和计入到排行榜。"));

		changes.addButton( new ChangeButton(new SnakeSprite(),
                "新敌人：靶机",
			"第一大区追加了新敌人：靶机。\n\n" +
				"其破碎怪物原型为：下水道巨蛇"));

		changes.addButton( new ChangeButton(new SpawnerSprite(),0.4F,
                "新敌人：侦查中枢",
			"第六大区追加了新敌人：侦查中枢。\n\n" +
				"其破碎怪物原型为：恶魔血巢"));

		changes.addButton( new ChangeButton(new RipperSprite(), 0.7F,
                "新敌人：尖兵",
			"第六大区追加了新敌人：尖兵。\n\n" +
				"其破碎怪物原型为：恶魔撕裂者\n\n" +
				"它通过_侦查中枢_来生成。"));

		changes.addButton( new ChangeButton(new HeroSprite(HeroClass.TYPE561, 5),
                0.8F,
                "新角色：56-1式",
			"增加了新角色战术人形56-1式。\n" +
				"\n" +
				"56-1自带强力专属遗物，并且会根据饱食度改变战斗力。\n" +
				"\n" +
				"56-1拥有两种转职。"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.GUN561, null),
                "新初始武器：56-1式",
			"“56-1式突击步枪，前来报到！我会坚决消灭每一个敌人！" +
				"\n\n以AK47为基础仿制的突击步枪，解决了一定的远距离作战的问题。" +
				"\n\n_需要手动装填枪榴弹，但填充后自身近战伤害大幅度降低，在发射完后，需要很长一段回合冷却。_"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
		changes.hardlight( CharSprite.WARNING );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new RatSprite(),
                "敌人生成",
			"众所周知，之前的老版本怪组生成极其不合理。\n\n" +
				"制作组在新版本调整了5区，6区的怪组生成，以尽量缓解每位指挥官的作战压力\n\n" +
				"祝各位能在新版本找到新的感觉，新的思路，新的玩法。"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.UMP45, null),
                "UMP-45",
			"UMP45初始防御从0调整为1，成长不变。\n\n" +
				"并且在描述中追加具体吸收伤害数值。"));

		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);

		changes.addButton( new ChangeButton(new TyphoonSprite(),0.3F,
                new TyphoonSprite().zap, true,
                "难度调整",
			"由于现在的少前地牢难度太低了，现决定对 _提丰/钢狮/九头蛇/木星_ 这四个敌人进行调整，以期平衡难度。" +
				"\n\n现在这四个敌人将会_ 见面送十炮 _。")
        );
    }
}
