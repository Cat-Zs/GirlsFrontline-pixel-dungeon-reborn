package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.GamesInProgress;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.ErrorButton;
import com.watabou.noosa.Game;
import java.io.IOException;

import static com.shatteredpixel.shatteredpixeldungeon.Dungeon.version;

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.GREY_BUTTON_TR;
import static com.shatteredpixel.shatteredpixeldungeon.Chrome.Type.TOAST_TR;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.BannerSprites;
import com.shatteredpixel.shatteredpixeldungeon.effects.Fireball;
import com.shatteredpixel.shatteredpixeldungeon.messages.Languages;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.update.GDChangesButton;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndStartGame;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSelectGameInProgress;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {
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

		StyledButton btnZeroLevel=null;
		if (Badges.isUnlocked(Badges.Badge.HAPPY_END) || DeviceCompat.isDebug()){
			btnZeroLevel = new StyledButton(GREY_BUTTON,"返回地表"){
				@Override
				protected void onClick() {
					enterMainGame();
				}
			};
		}else{
			btnZeroLevel = new StyledButton(GREY_BUTTON_TR,"返回地表(未解锁)");
		}
		btnZeroLevel.icon(Icons.get(Icons.ENTER));
		add(btnZeroLevel);

		StyledButton btnPlay = new StyledButton(GREY_BUTTON,"进入游戏"){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().isEmpty()){
					TitleScene.this.add( new WndStartGame(1) );
				} else {
					TitleScene.this.add( new WndSelectGameInProgress() );
				}
			}
		};
		btnPlay.icon(Icons.get(Icons.ENTER));
		add(btnPlay);

		StyledButton btnRankings = new StyledButton(GREY_BUTTON,"排行榜"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(Icons.get(Icons.RANKINGS));
		add(btnRankings);

		StyledButton btnBadges = new StyledButton(GREY_BUTTON,"徽章"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(Icons.get(Icons.BADGES));
		add(btnBadges);

		StyledButton btnSettings = new SettingsButton(GREY_BUTTON,"设置");
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_BUTTON,"关于"){
			@Override
			protected void onClick() {
				GirlsFrontlinePixelDungeon.switchNoFade( AboutScene.class );
			}
		};
		Image xs = Icons.get(Icons.GIRLPDS);
		xs.scale.set(PixelScene.align(0.6f));
		btnAbout.icon(xs);
		add(btnAbout);

		StyledButton btnChanges = new GDChangesButton(TOAST_TR,"更改");
		btnChanges.icon(new Image(Icons.get(Icons.CHANGESLOG)));
		btnChanges.setRect(0, h - 20, 50, 20);
		add(btnChanges);

		final int BTN_HEIGHT = 20;
		final int GAP = 2;

		if (landscape()) {
			btnPlay    .setRect(title.x - 50, topRegion + GAP, title.width() + 100 - 1, BTN_HEIGHT);
			align(btnPlay);
			btnZeroLevel.setRect(btnPlay.left(),btnPlay.bottom()+GAP,btnPlay.width()  ,BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left()     ,btnZeroLevel.bottom()+GAP     ,btnPlay.width()/2f    ,BTN_HEIGHT);
			btnBadges  .setRect(btnRankings.left() ,btnRankings.bottom()+GAP ,btnPlay.width()/2f    ,BTN_HEIGHT);
			btnSettings.setRect(btnBadges.right()+2,btnRankings.top()        ,btnPlay.width()/2f-GAP,BTN_HEIGHT);
			btnAbout   .setRect(btnSettings.left() ,btnSettings.bottom()+GAP ,btnPlay.width()/2f-GAP,BTN_HEIGHT);
		} else {
			btnPlay    .setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnZeroLevel.setRect(btnPlay.left(),btnPlay.bottom()+GAP,btnPlay.width()  ,BTN_HEIGHT);
			btnRankings.setRect(btnZeroLevel.left(),btnZeroLevel    .bottom()+GAP,btnPlay.width(),BTN_HEIGHT);
			btnBadges  .setRect(btnZeroLevel.left(),btnRankings.bottom()+GAP,btnPlay.width(),BTN_HEIGHT);
			btnSettings.setRect(btnZeroLevel.left(),btnBadges  .bottom()+GAP,btnPlay.width(),BTN_HEIGHT);
			btnAbout   .setRect(btnZeroLevel.left(),btnSettings.bottom()+GAP,btnPlay.width(),BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

        ErrorButton btnError = new ErrorButton();
        btnError.setPos( w-25, version.y-20 );
        add(btnError);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(w - 25, 0);
        add(btnExit);

		fadeIn();
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				icon(Icons.get(Icons.LANGS));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(Icons.get(Icons.SETTINGS));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.INCOMPLETE){
				WndSettings.last_index = 4;
			}
			GirlsFrontlinePixelDungeon.scene().add(new WndSettings());
		}
	}

	private static void enterMainGame(){
		Dungeon.hero=null;
		ActionIndicator.action  = null;
		GamesInProgress.curSlot = 0;
		GamesInProgress.selectedClass=HeroClass.TYPE561;
		boolean	newGame = null == GamesInProgress.check(GamesInProgress.curSlot);
		if(newGame){
			InterlevelScene.start();
		}else{
			if(version<=643){
            //643版本在生成器中添加了生成后又移除，导致在添加生成的情况下进入过的存档，在移除生成之后会崩档。
            //由于只在内测版本出现这种情况，所以不对正常存档做改动，正常存档崩了就删档吧
            //这里对643及以前版本生成的返回地表存档进行删档以处理崩档情况。
			//因为地表的存档还可能会有错误所以我先加回来。
            Dungeon.deleteGame(GamesInProgress.curSlot, true);
			}else {
            try{InterlevelScene.restore();}
            catch(IOException e){Game.reportException(e);}
			}
		}
		Game.switchScene(GameScene.class);
	}

	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}