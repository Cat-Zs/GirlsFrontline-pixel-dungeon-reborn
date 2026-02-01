package com.shatteredpixel.shatteredpixeldungeon.scenes;

import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON_TR;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder.SeedFindScene;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ErrorButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;

import java.io.IOException;

public class SecondTitleScene extends PixelScene {
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.play(Assets.Music.THEME_1,true);

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		placeTorch(title.x + 22, title.y + 46);
		placeTorch(title.x + title.width - 22, title.y + 46);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		StyledButton btnZeroLevel;
        btnZeroLevel = new StyledButton(GREY_BUTTON, "返回地表") {
            @Override
            protected void onClick() {
                enterMainGame();
            }
        };
        btnZeroLevel.icon(Icons.get(Icons.ENTER));
        add(btnZeroLevel);

        StyledButton SeedFinder = new StyledButton(GREY_BUTTON, "种子查询器") {
            @Override
            protected void onClick() {
                GirlsFrontlinePixelDungeon.switchNoFade(SeedFindScene.class);
            }
        };
        SeedFinder.icon(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS));
        add(SeedFinder);

        StyledButton LastTitle;
        LastTitle = new StyledButton(GREY_BUTTON, "上一页") {
            @Override
            protected void onClick() {
                GirlsFrontlinePixelDungeon.switchNoFade(TitleScene.class);
            }
        };
        LastTitle.icon(Icons.get(Icons.ENTER));
        add(LastTitle);

        StyledButton NextTitle;
        NextTitle = new StyledButton(GREY_BUTTON, "下一页") {
            @Override
            protected void onClick() {
                GirlsFrontlinePixelDungeon.switchNoFade(SecondTitleScene.class);
            }
        };
        NextTitle = new StyledButton(GREY_BUTTON_TR,"下一页(未制作)");
        NextTitle.icon(Icons.get(Icons.ENTER));
        add(NextTitle);

		final int BTN_HEIGHT = 20;
		final int GAP = 2;

		if (landscape()) {
            btnZeroLevel.setRect(title.x - 50, topRegion + GAP, title.width() + 100 - 1, BTN_HEIGHT);
			align(btnZeroLevel);
			LastTitle.setRect(btnZeroLevel.left(),btnZeroLevel.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
			SeedFinder.setRect(btnZeroLevel.left(),LastTitle.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
            NextTitle.setRect(btnZeroLevel.left(),SeedFinder.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
		} else {
            btnZeroLevel.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnZeroLevel);
			LastTitle.setRect(btnZeroLevel.left(),btnZeroLevel.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
			SeedFinder.setRect(btnZeroLevel.left(),LastTitle.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
            NextTitle.setRect(btnZeroLevel.left(),SeedFinder.bottom()+GAP,btnZeroLevel.width()  ,BTN_HEIGHT);
		}

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	@Override
	protected void onBackPressed() {
		//Do nothing
	}

    private static void enterMainGame(){
        Dungeon.hero=null;
        ActionIndicator.action  = null;
        GamesInProgress.curSlot = 0;
        GamesInProgress.selectedClass = HeroClass.TYPE561;
        GamesInProgress.Info gameInfo = GamesInProgress.check(GamesInProgress.curSlot);
        if(gameInfo == null){
            InterlevelScene.start();
        }else if(gameInfo.version < Game.versionCode){
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
            InterlevelScene.start();
        }else{
            try{InterlevelScene.restore();}
            catch(IOException e){Game.reportException(e);}
        }
        Game.switchScene(GameScene.class);
    }
}