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

package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LostBackpack;
import com.shatteredpixel.shatteredpixeldungeon.levels.DeadEndLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RabbitBossLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Chasm;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.services.updates.Updates;
import com.shatteredpixel.shatteredpixeldungeon.ui.GameLog;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.BArray;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndError;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.utils.DeviceCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class InterlevelScene extends PixelScene {
	
	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total
	
	public static float fadeTime;
	
	public enum Mode {
		ACCESS,DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, NONE, RESET
	}
	public static Mode mode=Mode.NONE;

	public static String seedCode=null;

	public static int returnDepth;
	public static int returnPos;

	public static int accessPos;
	public static int accessLevelDepth;
    public static int SUBId = 0;

	public static boolean fallIntoPit;

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}
	private Phase phase;
	private float timeLeft;

	private RenderedTextBlock message;

	private static Thread thread;
	private static Exception error = null;
	private float waitingTime;

	public static int lastRegion = -1;

	{
		inGameScene = true;
	}

	@SuppressWarnings("SuspiciousIndentation")
    @Override
	public void create() {
		super.create();

		String loadingAsset;
		int loadingDepth;
		final float scrollSpeed;
		fadeTime = NORM_FADE;
		switch (mode){
			default:
				loadingDepth = Dungeon.depth;
				scrollSpeed = 1;
				break;
			case CONTINUE:
				loadingDepth = GamesInProgress.check(GamesInProgress.curSlot).depth;
				scrollSpeed = 5;
				break;
			case DESCEND:
				if (Dungeon.hero == null){
					loadingDepth = 1;
					fadeTime = SLOW_FADE;
				} else {
					loadingDepth = Dungeon.depth+1;
					if (!(Statistics.deepestFloor < loadingDepth)) {
						fadeTime = FAST_FADE;
					} else if (loadingDepth == 6 || loadingDepth == 11
							|| loadingDepth == 16 || loadingDepth == 21) {
						fadeTime = SLOW_FADE;
					}
				}
				scrollSpeed = 5;
				break;
			case FALL:
				loadingDepth = Dungeon.depth+1;
				scrollSpeed = 50;
				break;
			case ASCEND:
				fadeTime = FAST_FADE;
				loadingDepth = Dungeon.depth-1;
				scrollSpeed = -5;
				break;
			case RETURN:
				loadingDepth = returnDepth;
				scrollSpeed = returnDepth > Dungeon.depth ? 15 : -15;
				break;
		}

		//flush the texture cache whenever moving between regions, helps reduce memory load
		int region = (int)Math.ceil(loadingDepth / 5f);
		if (region != lastRegion){
			TextureCache.clear();
			lastRegion = region;
		}

		if      (lastRegion == 1)    loadingAsset = Assets.Interfaces.LOADING_SEWERS;
		else if (lastRegion == 2)    loadingAsset = Assets.Interfaces.LOADING_PRISON;
		else if (lastRegion == 3)    loadingAsset = Assets.Interfaces.LOADING_CAVES;
		else if (lastRegion == 4)    loadingAsset = Assets.Interfaces.LOADING_CITY;
		else if (lastRegion == 5)    loadingAsset = Assets.Interfaces.LOADING_HALLS;
		else                         loadingAsset = Assets.Interfaces.SHADOW;

        boolean isTest = false;
        GamesInProgress.Info gameInfo = GamesInProgress.check(GamesInProgress.curSlot);
        if (gameInfo!=null) {
            isTest = GamesInProgress.isChallenged(gameInfo.challenges,Challenges.TEST_MODE);
        }
        resetFadeTime(DeviceCompat.isDebug()|| Dungeon.isChallenged(Challenges.TEST_MODE)||isTest);

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, loadingAsset ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		bg.autoAdjust = true;
		add(bg);

		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000)){
			@Override
			public void update() {
				super.update();
				if (phase == Phase.FADE_IN)         aa = Math.max( 0, (timeLeft - (fadeTime - 0.333f)));
				else if (phase == Phase.FADE_OUT)   aa = Math.max( 0, (0.333f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		String text = Messages.get(Mode.class, mode.name());

		message = PixelScene.renderTextBlock( text, 9 );
		message.setPos(
				(Camera.main.width - message.width()) / 2,
				(Camera.main.height - message.height()) / 2
		);
		align(message);
		add( message );

		if (Updates.isInstallable()){
			StyledButton install = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "install")){
				@Override
				public void update() {
					super.update();
					float p = timeLeft / fadeTime;
					if (phase == Phase.FADE_IN)         alpha(1 - p);
					else if (phase == Phase.FADE_OUT)   alpha(p);
					else                                alpha(1);
				}

				@Override
				protected void onClick() {
					super.onClick();
					Updates.launchInstall();
				}
			};
			install.icon(Icons.get(Icons.CHANGES));
			install.textColor(Window.SHPX_COLOR);
			install.setSize(install.reqWidth()+5, 20);
			install.setPos((Camera.main.width - install.width())/2, (Camera.main.height - message.bottom())/3 + message.bottom());
			add(install);
		}

		phase = Phase.FADE_IN;
		timeLeft = fadeTime;

		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {

					try {

						if (Dungeon.hero != null){
							Dungeon.hero.spendToWhole();
						}
						Actor.fixTime();

						switch (mode) {
							case ACCESS:
								access();
								break;
							case DESCEND:
								descend();
								break;
							case ASCEND:
								ascend();
								break;
							case CONTINUE:
								restore();
								break;
							case RESURRECT:
								resurrect();
								break;
							case RETURN:
								returnTo();
								break;
							case FALL:
								fall();
								break;
                            case RESET:
                                reset();
                                break;
						}

					} catch (Exception e) {

						error = e;

					}

					if (phase == Phase.STATIC && error == null) {
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					}
				}
			};
			thread.start();
		}
		waitingTime = 0f;
	}

    public static void resetFadeTime(boolean isTest){
        //slow down transition when displaying an install prompt
        if (Updates.isInstallable()){
            fadeTime += 0.5f; //adds 1 second total
            //speed up transition when debugging
        }else if (isTest){
            fadeTime = 0f;
            //debug版本或者测试模式将楼层切换的时间fadeTime设置为0
        }
        if(!isTest){
            //非debug版本进入过测试模式后进入普通对局时，重新对fadeTime赋值
            fadeTime = NORM_FADE;
            switch (mode){
                case DESCEND:
                    if (Dungeon.hero == null){
                        fadeTime = SLOW_FADE;
                    } else {
                        int loadingDepth = Dungeon.depth + 1;
                        if (!(Statistics.deepestFloor < loadingDepth)) {
                            fadeTime = FAST_FADE;
                        } else if (loadingDepth == 6 || loadingDepth == 11
                                || loadingDepth == 16 || loadingDepth == 21) {
                            fadeTime = SLOW_FADE;
                        }
                    }
                    break;
                case ASCEND:
                    fadeTime = FAST_FADE;
                    break;
                case RETURN:
                case FALL:
                case CONTINUE:
                default:
                    break;
            }
            if (Updates.isInstallable()){
                fadeTime += 0.5f; //adds 1 second total
                //speed up transition when debugging
            }
        }
    }
	@Override
	public void update() {
		super.update();

		waitingTime += Game.elapsed;

		float p = timeLeft / fadeTime;

		switch (phase) {

		case FADE_IN:
			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = fadeTime;
				} else {
					phase = Phase.STATIC;
				}
			}
			break;

		case FADE_OUT:
			message.alpha( p );

			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
				thread = null;
				error = null;
			}
			break;

		case STATIC:
			if (error != null) {
				String errorMsg;
				if (error instanceof FileNotFoundException)     errorMsg = Messages.get(this, "file_not_found");
				else if (error instanceof IOException)          errorMsg = Messages.get(this, "io_error");
				else if (error.getMessage() != null &&
						error.getMessage().equals("old save")) errorMsg = Messages.get(this, "io_error");

				else throw new RuntimeException("fatal error occured while moving between floors. " +
							"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth, error);

				add( new WndError( errorMsg ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( TitleScene.class );
					}
				} );
				thread = null;
				error = null;
			} else if (thread != null && (int)waitingTime == 10){
				waitingTime = 11f;
				String s = "";
				for (StackTraceElement t : thread.getStackTrace()){
					s += "\n";
					s += t.toString();
				}
				GirlsFrontlinePixelDungeon.reportException(
						new RuntimeException("waited more than 10 seconds on levelgen. " +
								"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " trace:" +
								s)
				);
			}
			break;
		}
	}

	public static void start(){
		Mob.clearHeldAllies();
		Dungeon.init("ANEWWORLD",0);
		GameLog.wipe();
		Dungeon.depth=Statistics.deepestFloor=-1;
		Level level = Dungeon.newLevel(0,0);
		Dungeon.switchLevel(level,level.entrance);
	}

	private void access(){
		Mob.holdAllies(Dungeon.level);
		try{Dungeon.saveAll();}
		catch(IOException e){Game.reportException(e);}

		Level level;
        if (SUBId!=0) {
            //先尝试新子层
            level = Dungeon.tryLoadLevel(accessLevelDepth, SUBId, 0);
            if (null == level) {
                level = Dungeon.tryLoadLevel(accessLevelDepth + 1000 * SUBId, 0, 0);
                //没有新子层尝试旧子层
            }
            if (null == level) {
                //没有旧子层则新建
                level = Dungeon.newLevel(accessLevelDepth, SUBId);
            }
        }else {
            level = Dungeon.tryLoadLevel(accessLevelDepth, 0, 0);
            if (null == level) {
                level = Dungeon.newLevel(accessLevelDepth, 0);
            }
        }

		Dungeon.depth=level.levelDepth;
        Dungeon.SUBId=level.SUBId;
        SUBId = 0;
        //特殊前进时使用子层标签读取或生成，结束后清零
		Dungeon.switchLevel(level,accessPos);
	}

	private static void descend() throws IOException {
		if (Dungeon.hero == null) {
			Mob.clearHeldAllies();
			Dungeon.init(seedCode);
			GameLog.wipe();
		} else {
			Mob.holdAllies( Dungeon.level );
			Dungeon.saveAll();
		}

		Level level;
        int depth = Dungeon.depth;
        int SUB = Dungeon.SUBId;
        Level TryToNew = null;
        if (depth!=0
                &&SUB>=0
                &&SUB<Dungeon.ConnectLevel) {
            //非0层（以免开始游戏被0层触发）
            // 且当前子层序号不是任务层(负数)
            // 且当前子层序号未达到衔接层上限
            int SUBA=SUB;

            do {
                SUBA++;
                //遍历最大子层数量
                TryToNew = Dungeon.tryLoadLevel(depth, SUBA ,0);
                if (TryToNew == null || TryToNew instanceof RabbitBossLevel) {
                    //尝试读取当前序号子层，没有则创建,读取结果为兔子层则要覆盖
                    TryToNew = Dungeon.newLevel(depth, SUBA);
                }
            }
            while (SUBA<Dungeon.ConnectLevel && (TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel));
                //尝试次数小于最大衔接层且尝试子层结果是默认层时，重新尝试

            if (TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel) {
                //遍历最大子层数量均不是衔接层时，进入主层的下一层
                depth++;
                SUB = 0;
                TryToNew =  Dungeon.tryLoadLevel(depth, SUB ,0);
                if (TryToNew == null) {
                    //尝试读取当前序号子层，没有则创建
                    TryToNew = Dungeon.newLevel(depth, SUB);
                }
            }
        }else {
            depth++;
            SUB = 0;
            //0层开局到1层主层
            // 任务层默认进入下一层主层
            // 当前子层已经是衔接层下限进入下一层主层
        }

        if (TryToNew!=null
                // 当前楼层允许有衔接层子层
                &&!(TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel)
                // 且以此法得到的子层不是默认层
        ) {
            //TryToNew在上面已经tryLoadLevel再newLevel了，为避免影响物品生成，将其赋值给level
            level = TryToNew;
        }else {
            //将要进入主层时的操作，等效旧版
            if (Dungeon.depth >= Statistics.deepestFloor) {
                //进入更深楼层，会新增楼层
                level = Dungeon.newLevel(depth, SUB);
            } else {
                level = Dungeon.tryLoadLevel(depth, SUB, 0);
            }
        }
        if (level != null) {
            Dungeon.depth = depth;
            Dungeon.switchLevel( level, level.entrance );
        }else {
            GLog.n("该楼层已损坏，请节哀");
        }
    }

	private void fall() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Buff.affect( Dungeon.hero, Chasm.Falling.class );
		Dungeon.saveAll();

        Level level;
        int depth = Dungeon.depth;
        int SUB = Dungeon.SUBId;
        Level TryToNew = null;
        if(fallIntoPit){
            Dungeon.SUBId=0;
            depth++;
            Dungeon.depth = depth;
            if (depth >= Statistics.deepestFloor) {
                level = Dungeon.newLevel(depth, 0);
            } else {
                level = Dungeon.tryLoadLevel(depth,0,0);
                //坠落回主层
            }
        }
        else {
            if (SUB >= 0
                    && SUB < Dungeon.ConnectLevel) {
                // 且当前子层序号不是任务层(负数)
                // 且当前子层序号未达到衔接层上限
                int SUBA = SUB;

                do {
                    SUBA++;
                    //遍历最大子层数量
                    TryToNew = Dungeon.tryLoadLevel(depth, SUBA, 0);
                    if (TryToNew == null || TryToNew instanceof RabbitBossLevel) {
                        //尝试读取当前序号子层，没有则创建,读取结果为兔子层则要覆盖
                        TryToNew = Dungeon.newLevel(depth, SUBA);
                    }
                }
                while (SUBA < Dungeon.ConnectLevel && (TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel));
                //尝试次数小于最大衔接层且尝试子层结果是默认层时，重新尝试

                if (TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel) {
                    //遍历最大子层数量均不是衔接层时，进入主层的下一层
                    depth++;
                    SUB = 0;
                    TryToNew = Dungeon.tryLoadLevel(depth, SUB, 0);
                    if (TryToNew == null) {
                        //尝试读取当前序号子层，没有则创建
                        TryToNew = Dungeon.newLevel(depth, SUB);
                    }
                }
            } else {
                depth++;
                SUB = 0;
                //0层开局到1层主层
                // 任务层默认进入下一层主层
                // 当前子层已经是衔接层下限进入下一层主层
            }


            if (TryToNew != null
                    // 当前楼层允许有衔接层子层
                    && !(TryToNew instanceof DeadEndLevel || TryToNew instanceof RabbitBossLevel)
                // 且以此法得到的子层不是默认层
            ) {
                //TryToNew在上面已经tryLoadLevel再newLevel了，为避免影响物品生成，将其赋值给level
                level = TryToNew;
            } else {
                //将要进入主层时的操作，等效旧版
                if (Dungeon.depth >= Statistics.deepestFloor) {
                    //进入更深楼层，会新增楼层
                    level = Dungeon.newLevel(depth, SUB);
                } else {
                    Dungeon.depth = depth;
                    level = Dungeon.tryLoadLevel(depth, SUB, 0);
                }
            }
        }
        if (level != null) {
            Dungeon.depth = depth;
            Dungeon.switchLevel( level, level.fallCell( fallIntoPit ) );
        }else {
            GLog.n("该楼层已损坏，请节哀");
        }
	}
    private void reset() throws IOException {
        Actor.fixTime();
        Level copyLevel = Dungeon.tryLoadLevel(Dungeon.depth,Dungeon.SUBId,1);
        //重置楼层，所以以当前楼层子标签读取
        if (copyLevel == null) {
            GLog.n("未读取到复制存档，即将重新保存");
            Dungeon.level.FirstSave = true;
            Dungeon.saveAll();
        } else
            Dungeon.switchLevel(copyLevel,copyLevel.entrance);
    }
	
	private void ascend() throws IOException {
		
		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
        int depth = Dungeon.depth;
        int SUB = Dungeon.SUBId;
        int SUBA;
        Level level;
        Level TryToLoad = null;
        if (SUB<0){
            SUB=0;
            //任务层默认上楼到当前主层
        }else if (SUB>0){
            //衔接子层返回
            SUBA = SUB;
            do {
                SUBA--;
                TryToLoad = Dungeon.tryLoadLevel(depth,SUBA,0);
                //尝试读取子层序号-1的楼层
                if (TryToLoad==null || TryToLoad instanceof RabbitBossLevel){
                    TryToLoad = Dungeon.newLevel(depth, SUBA);
                    //读取结果为空则为新增的子层衔接层，读取结果为兔子层则要覆盖，新建一个
                }
            }while (SUBA>0 &&(TryToLoad instanceof DeadEndLevel || TryToLoad instanceof RabbitBossLevel));
            //在当前尝试楼层序号大于0（处于子层中）且读取结果是默认层的时候重新尝试

            if (TryToLoad instanceof DeadEndLevel || TryToLoad instanceof RabbitBossLevel) {
                //从当前子层遍历到子层1，均不是衔接层时，返回主层
                SUB = 0;
                depth--;
            }

        }else {
            //主层向上返回
            depth--;
            Dungeon.depth=depth;
            SUBA = Dungeon.ConnectLevel;
            do {
                TryToLoad = Dungeon.tryLoadLevel(depth,SUBA,0);
                //从最大衔接层往下遍历
                if (TryToLoad==null){
                    TryToLoad = Dungeon.newLevel(depth, SUBA);
                    //读取结果为空则为新增的子层衔接层，新建一个
                }
                SUBA--;
            }while (SUBA>0 && (TryToLoad instanceof DeadEndLevel || TryToLoad instanceof RabbitBossLevel));
            //在当前尝试楼层序号大于0（处于子层中）且读取结果是默认层的时候重新尝试
            if (TryToLoad instanceof DeadEndLevel || TryToLoad instanceof RabbitBossLevel) {
                //遍历最大子层数量均不是衔接层时，返回主层
                TryToLoad = Dungeon.tryLoadLevel(depth,SUBA,0);
            }
        }
        if (TryToLoad!=null
                //往上楼层允许有衔接层子层
                &&!(TryToLoad instanceof DeadEndLevel || TryToLoad instanceof RabbitBossLevel)
                //且以此法得到的子层不是默认层
        ) {
            //TryToLoad在上面已经tryLoadLevel再newLevel了，为避免影响物品生成，将其赋值给level
            level = TryToLoad;
        }else {
            //没有衔接层时的操作，等效旧版无子层
            level = Dungeon.tryLoadLevel(depth,SUB,0);
        }
        if (level != null) {
            Dungeon.depth=depth;
            Dungeon.switchLevel( level, level.exit );
        }else
            GLog.n("该楼层已损坏，节哀");
    }
	
	private void returnTo() throws IOException {
		
		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
		int depth = returnDepth;
		Level level;
        int SUBA = SUBId;
        level = Dungeon.tryLoadLevel(depth, SUBA, 0);
        if(level == null) {
            level = Dungeon.tryLoadLevel(depth+1000*SUBId, 0, 0);
            //对旧版本子层兼容
        }
        if (level==null||level instanceof RabbitBossLevel) {
            do {
                level = Dungeon.tryLoadLevel(depth, SUBA, 0);
                if (level == null)
                    level = Dungeon.newLevel(depth, SUBA);
                SUBA--;
            } while (SUBA > 0 && level instanceof DeadEndLevel);
        }
        SUBId = 0;
        if (!(level instanceof DeadEndLevel)) {
            Dungeon.depth = depth;
            Dungeon.SUBId = SUBA;
            //大传、大传送的用处，先赋予子层标签再进入此处，返回之后子层临时参数清零
            Dungeon.switchLevel(level, returnPos);
        }else {
            GLog.n("该楼层已损坏，请节哀");
        }
	}
	
	public static void restore() throws IOException {
		Mob.clearHeldAllies();

		GameLog.wipe();

		Level level;
        Dungeon.loadGame( GamesInProgress.curSlot );
        int SUB = 0;
        GamesInProgress.Info gameInfo = GamesInProgress.check(GamesInProgress.curSlot);
        if (gameInfo!=null) {
            SUB = gameInfo.SUBId;
        }
        Dungeon.SUBId=SUB;
        try{
            level = Dungeon.loadLevel(GamesInProgress.curSlot, Dungeon.depth, SUB, 0);
            //尝试新子层
        } catch (IOException e) {
            level = Dungeon.loadLevel(GamesInProgress.curSlot, Dungeon.depth + 1000*SUB, 0, 0);
            //未建立则尝试旧子层
        }
        //读档
		Dungeon.switchLevel(level,Dungeon.hero.pos);
	}
	
	private void resurrect() {
		
		Mob.holdAllies( Dungeon.level );

		Level level;
		if (Dungeon.level.locked) {
			ArrayList<Item> preservedItems = Dungeon.level.getItemsToPreserveFromSealedResurrect();

			Dungeon.hero.resurrect();
			level = Dungeon.newLevel(Dungeon.depth, Dungeon.SUBId);
            //以当前楼层深度、子层序号重建
			Dungeon.hero.pos = level.randomRespawnCell(Dungeon.hero);

			for (Item i : preservedItems){
				level.drop(i, level.randomRespawnCell(null));
			}
			level.drop(new LostBackpack(), level.randomRespawnCell(null));

		} else {
			level = Dungeon.level;
			BArray.setFalse(level.heroFOV);
			BArray.setFalse(level.visited);
			BArray.setFalse(level.mapped);
			int invPos = Dungeon.hero.pos;
			int tries = 0;
			do {
				Dungeon.hero.pos = level.randomRespawnCell(Dungeon.hero);
				tries++;

			//prevents spawning on traps or plants, prefers farther locations first
			} while (level.traps.get(Dungeon.hero.pos) != null
					|| (level.plants.get(Dungeon.hero.pos) != null && tries < 500)
					|| level.trueDistance(invPos, Dungeon.hero.pos) <= 30 - (tries/10));

			//directly trample grass
			if (level.map[Dungeon.hero.pos] == Terrain.HIGH_GRASS || level.map[Dungeon.hero.pos] == Terrain.FURROWED_GRASS){
				level.map[Dungeon.hero.pos] = Terrain.GRASS;
			}
			Dungeon.hero.resurrect();
			level.drop(new LostBackpack(), invPos);
		}

		Dungeon.switchLevel( level, Dungeon.hero.pos );
	}
	
	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}
