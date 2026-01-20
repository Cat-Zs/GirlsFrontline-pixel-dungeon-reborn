package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.SPDAction;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;
import com.watabou.noosa.Camera;
import com.watabou.input.GameAction;
import com.watabou.input.KeyBindings;
import com.watabou.input.KeyEvent;
import com.watabou.utils.Signal;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SnakeScene extends PixelScene {
	{
		inGameScene = true;
	}

	//贪吃蛇所用到的变量(不要问为什么变量命名这么抽象,从自己n年前的C代码抄的,能用就行)
	public static final int WIDTH  = 13;
	public static final int HEIGHT = 13;
	public static final int UP    = 1;
	public static final int RIGHT = 2;
	public static final int DOWN  = 3;
	public static final int LEFT  = 4;
	public static final int EMPTY  = 0;
	public static final int FRUIT  = 5;
	public static final int BORDER = 6;
	private int _x_s;
	private int _x_s2;
	private int _x;
	private int _x2;
	private int _x3;
	private char[][] map;
	private int hY;
	private int hX;
	private int tY;
	private int tX;

	private boolean gameRunning = false;
	private int snakeLength;
	private int score; // 计分变量
	private RenderedTextBlock scoreText; // 分数显示文本
	private StyledButton restartButton; // 重新开始按钮
	private StyledButton returnButton; // 返回按钮
	private RenderedTextBlock gameOverText; // 游戏结束文本
	private RenderedTextBlock finalScoreText; // 最终分数文本

	//贪吃蛇初始化函数
	private void gameInit(){
		map = new char[HEIGHT][WIDTH];

		for(_x=0;_x<HEIGHT;_x++){
			for(_x2=0;_x2<WIDTH;_x2++){
				map[_x][_x2]=BORDER;
			}  
		}

		for(_x=1;_x<HEIGHT-1;_x++){
			for(_x2=1;_x2<WIDTH-1;_x2++){
				map[_x][_x2]=EMPTY;   
			}  
		}

		hY=rand()%(HEIGHT-4)+2;
		hX=rand()%(WIDTH-4)+2;
		map[hY][hX]=(char)(rand()%4+1);

		switch(map[hY][hX]){
			case UP:map[tY=(hY+1)][tX=hX]=UP;break;
			case RIGHT:map[tY=hY][tX=(hX-1)]=RIGHT;break;
			case DOWN:map[tY=(hY-1)][tX=hX]=DOWN;break;
			case LEFT:map[tY=hY][tX=(hX+1)]=LEFT;break;
		}

		while(map[_x=rand()%(HEIGHT-2)+1][_x2=rand()%(WIDTH-2)+1]!=EMPTY);
		map[_x][_x2]=FRUIT;

		// 初始化游戏状态
		gameRunning = true;
		snakeLength = 2;
		score = 0; // 先初始化分数为0
		timer = 1.0f; // 重置定时器，确保开局减速效果
		
		// 然后再渲染界面
		renderInit();
		inputInit();
	}

	//贪吃蛇(单次)运行
	private boolean gameTick(){
		switch(inputTick()){
			case 'w':if(map[hY][hX]!=DOWN){map[hY][hX]=UP;}break;
			case 'd':if(map[hY][hX]!=LEFT){map[hY][hX]=RIGHT;}break;
			case 's':if(map[hY][hX]!=UP){map[hY][hX]=DOWN;}break;
			case 'a':if(map[hY][hX]!=RIGHT){map[hY][hX]=LEFT;}break;
		}

		_x_s=map[hY][hX];
		if((_x_s&1)==1){
			hY+=_x_s-2;
		}else{
			hX-=_x_s-3;
		}

		if(map[hY][hX]!=EMPTY){
			if(map[hY][hX]==FRUIT){
				while(map[_x=rand()%(HEIGHT-2)+1][_x2=rand()%(WIDTH-2)+1]!=EMPTY);
				map[_x][_x2]=FRUIT;

				snakeLength+=1;
				score += 10; // 吃到水果，分数加10
				updateScoreDisplay(); // 更新分数显示
				if(snakeLength>=HEIGHT*WIDTH-1){
					return false;
				}
			}else{
				return false;
			}
		}else{
			_x_s2=map[tY][tX];
			map[tY][tX]=EMPTY;
			if((_x_s2&1)==1){
				tY+=_x_s2-2;
			}else{
				tX-=_x_s2-3;
			}
		}

		map[hY][hX]=(char)_x_s;

		renderTick();

		return true;
	}

	//贪吃蛇获取输入
	private BlockingQueue<Character> inputQueue;
	private Signal.Listener<KeyEvent> keyListener;
	private void inputInit(){
		inputQueue = new ArrayBlockingQueue<>(2);

		//按键控制
		keyListener = new Signal.Listener<KeyEvent>() {
			@Override
			public boolean onSignal(KeyEvent event) {
				GameAction action = KeyBindings.getActionForKey( event );
				if (event.pressed){
					if      (action == SPDAction.N){
						inputQueue.offer('w');
						return true;
					}else if(action == SPDAction.E){
						inputQueue.offer('d');
						return true;
					}else if(action == SPDAction.S){
						inputQueue.offer('s');
						return true;
					}else if(action == SPDAction.W){
						inputQueue.offer('a');
						return true;
					}
				}

				return false;
			}
		};

		KeyEvent.addKeyListener( keyListener );

		//控制上下左右的四个按钮
		float buttonSize = uiCamera.width<uiCamera.height ? uiCamera.width/6 : uiCamera.height/6;
		StyledButton btnRight  = new StyledButton(Chrome.Type.TOAST_TR, "", 9){
			@Override
			protected void onClick() {
				inputQueue.offer('d');
			}
		};
		StyledButton btnLeft   = new StyledButton(Chrome.Type.TOAST_TR, "", 9){
			@Override
			protected void onClick() {
				inputQueue.offer('a');
			}
		};
		StyledButton btnTop    = new StyledButton(Chrome.Type.TOAST_TR, "", 9){
			@Override
			protected void onClick() {
				inputQueue.offer('w');
			}
		};
		StyledButton btnBottom = new StyledButton(Chrome.Type.TOAST_TR, "", 9){
			@Override
			protected void onClick() {
				inputQueue.offer('s');
			}
		};
		btnRight .setSize(buttonSize, buttonSize);
		btnLeft  .setSize(buttonSize, buttonSize);
		btnTop   .setSize(buttonSize, buttonSize);
		btnBottom.setSize(buttonSize, buttonSize);
		btnRight .setPos (uiCamera.width -   buttonSize, uiCamera.height - 2*buttonSize);
		btnLeft  .setPos (uiCamera.width - 3*buttonSize, uiCamera.height - 2*buttonSize);
		btnTop   .setPos (uiCamera.width - 2*buttonSize, uiCamera.height - 3*buttonSize);
		btnBottom.setPos (uiCamera.width - 2*buttonSize, uiCamera.height -   buttonSize);
		btnRight .camera=uiCamera;
		btnLeft  .camera=uiCamera;
		btnTop   .camera=uiCamera;
		btnBottom.camera=uiCamera;
		add(btnRight );
		add(btnLeft  );
		add(btnTop   );
		add(btnBottom);
	}
	private char inputTick(){
		Character c = inputQueue.poll();
		return (c != null) ? c : '\0';
	}

	//贪吃蛇获取随机数
	private int rand(){
		return (int)(Random.Long()&0x7FFFFFFF);
	}

	//贪吃蛇渲染
	private int[] renderMap;
	private int snakeHead;
	private SnakeTileMap snakeTileMap;
	public class SnakeTileMap extends DungeonTilemap {
		public SnakeTileMap() {
			super(Assets.Environment.PLAY_SNAKE);
			map(renderMap, WIDTH);
		}

		@Override
		protected int getTileVisual(int pos, int tile, boolean flat) {
			if(pos==snakeHead){
				return tile;
			}else if(0<tile && tile<5){
				return 7;//蛇的身体
			}else{
				return tile;
			}
		}

	}
	private void renderInit(){
		renderMap = new int[HEIGHT*WIDTH];

		snakeTileMap = new SnakeTileMap();
		snakeTileMap.camera = uiCamera;
		float widthScale  = snakeTileMap.width /uiCamera.width;
		float heightScale = snakeTileMap.height/uiCamera.height;
		float scale = 1/(widthScale>heightScale?widthScale:heightScale);
		snakeTileMap.scale.x = scale;
		snakeTileMap.scale.y = scale;
		add(snakeTileMap);
		
		// 创建计分板
		createScoreDisplay();
	}
	private void renderTick(){
		for (int y=0; y<HEIGHT; y++) {
			for (int x=0; x<WIDTH; x++) {
				renderMap[y*WIDTH+x] = (int)map[y][x];
			}
		}

		snakeHead = hY*WIDTH+hX;
		snakeTileMap.updateMap();
	}

	{
		inGameScene = true;
	}

	@Override
	public void create() {
		super.create();

		gameInit();
		renderTick();

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}

	private float timer = 1f;
	@Override
	public void update() {
		super.update();
		
		if ((timer -= Game.elapsed) < 0) {
			timer = 0.25f;
			
			if(gameRunning){
				gameRunning = gameTick();
				if(!gameRunning){//不要把这个挪到外面,因为暂时不确定create和update的执行顺序(懒得看代码了,不过按常理来讲应该是create一定比update早)
					//游戏结束(可以根据长度(snakeLength)添加徽章之类的)
					showGameOverScreen(); // 显示游戏结束界面
				}
			}
		}
	}

	@Override
	public void destroy() {
		KeyEvent.removeKeyListener(keyListener);

		super.destroy();
	}
	
	// 创建分数显示
	private void createScoreDisplay() {
		// 如果已经存在旧的计分板，先移除它
		if (scoreText != null) {
			scoreText.killAndErase();
			scoreText = null;
		}
		
		// 创建计分文本
		scoreText = PixelScene.renderTextBlock("分数: " + score, 9);
		scoreText.camera = uiCamera;
		scoreText.hardlight(0xFFFFFF); // 白色文本
		scoreText.setPos(
			uiCamera.width - scoreText.width() - 10,
			10
		);
		add(scoreText);
	}
	
	// 更新分数显示
	private void updateScoreDisplay() {
		if (scoreText != null) {
			scoreText.text("分数: " + score);
			scoreText.setPos(
				uiCamera.width - scoreText.width() - 10,
				10
			);
		}
	}
	
	// 显示游戏结束界面
	private void showGameOverScreen() {
		// 创建游戏结束文本
		gameOverText = PixelScene.renderTextBlock("游戏结束!", 16);
		gameOverText.camera = uiCamera;
		gameOverText.hardlight(0xFFFFFF);
		gameOverText.setPos(
			(uiCamera.width - gameOverText.width()) / 2,
			uiCamera.height / 2 - 60
		);
		add(gameOverText);
		
		// 创建最终分数文本
		finalScoreText = PixelScene.renderTextBlock("最终分数: " + score, 12);
		finalScoreText.camera = uiCamera;
		finalScoreText.hardlight(0xFFFFFF);
		finalScoreText.setPos(
			(uiCamera.width - finalScoreText.width()) / 2,
			uiCamera.height / 2 - 30
		);
		add(finalScoreText);
		
		// 创建重新开始按钮
		restartButton = new StyledButton(Chrome.Type.TOAST_TR, "重新开始") {
			@Override
			protected void onClick() {
				restartGame();
			}
		};
		restartButton.camera = uiCamera;
		restartButton.setSize(100, 30);
		restartButton.setPos(
			(uiCamera.width - restartButton.width()) / 2,
			uiCamera.height / 2
		);
		add(restartButton);
		
		// 创建返回按钮
		returnButton = new StyledButton(Chrome.Type.TOAST_TR, "返回") {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		returnButton.camera = uiCamera;
		returnButton.setSize(100, 30);
		returnButton.setPos(
			(uiCamera.width - returnButton.width()) / 2,
			uiCamera.height / 2 + 40
		);
		add(returnButton);
	}
	
	// 重启游戏
	private void restartGame() {
		// 移除游戏结束界面的元素
		if (gameOverText != null) {
			gameOverText.killAndErase();
			gameOverText = null;
		}
		if (finalScoreText != null) {
			finalScoreText.killAndErase();
			finalScoreText = null;
		}
		if (restartButton != null) {
			restartButton.killAndErase();
			restartButton = null;
		}
		if (returnButton != null) {
			returnButton.killAndErase();
			returnButton = null;
		}
		
		// 重新初始化游戏
		gameInit();
		renderTick();
	}
}
