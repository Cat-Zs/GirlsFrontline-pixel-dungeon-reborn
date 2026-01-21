package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;
import com.watabou.noosa.NinePatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessAIScene extends PixelScene {
	{
		inGameScene = true;
	}

	// 游戏常量定义
	private static final int BOARD_WIDTH = 8;
	private static final int BOARD_HEIGHT = 8;
	private static final int EMPTY = 0;
	private static final int CELL_SIZE = 40; // 象棋棋盘单元格大小
	private static final int KING_COLUMN = 4;
	
	// 棋子类型定义
	private static final int WHITE_KING = 1;
	private static final int WHITE_QUEEN = 2;
	private static final int WHITE_ROOK = 3;
	private static final int WHITE_BISHOP = 4;
	private static final int WHITE_KNIGHT = 5;
	private static final int WHITE_PAWN = 6;
	private static final int BLACK_KING = 7;
	private static final int BLACK_QUEEN = 8;
	private static final int BLACK_ROOK = 9;
	private static final int BLACK_BISHOP = 10;
	private static final int BLACK_KNIGHT = 11;
	private static final int BLACK_PAWN = 12;
	
	// 游戏状态变量
	private int[][] gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
	private boolean gameRunning;
	private boolean whiteTurn;
	private boolean aiEnabled;
	private boolean playerIsWhite;
	// 选择状态
	private int selectedRow = -1;
	private int selectedCol = -1;
	private Image selectionIndicator;
	// 可走路径标记
	private List<Image> moveIndicators = new ArrayList<>();
	// 记录特殊规则
	private int passbyPawnRow;
	private int passbyPawnCol;
	private boolean canCastleRow0Col0;
	private boolean canCastleRow0Col7;
	private boolean canCastleRow7Col0;
	private boolean canCastleRow7Col7;
	
	// 渲染组件
	private Image background;
	private StyledButton[][] chessButtons;
	private StyledButton restartButton;
	private StyledButton exitButton;
	private StyledButton aiButton;
	// 资源图中每个格子大小为24*24
	private static final int TILE_SIZE = 24;
	// 棋子图像的实际大小
	private float adjustedCellSize;
	
	@Override
	public void create() {
		super.create();
		
		// 1. 设置背景
		setupBackground();
		
		// 2. 初始化游戏
		initGame();
		
		// 3. 创建游戏板视觉组件
		createBoardVisual();
		
		// 4. 添加控制按钮
		addControlButtons();
		
		// 5. 添加选择指示器
		createSelectionIndicator();
		
		// 6. 初始化游戏状态
		gameRunning = false;
		whiteTurn = true;
		aiEnabled = true;
		playerIsWhite = true;
		
		// 7. 显示棋子选择窗口
		showSideSelectionWindow();
		
		fadeIn();
	}
	
	// 设置背景
	private void setupBackground() {
		background = new Image(Assets.Interfaces.LOADING_PRISON);
		background.camera = uiCamera;
		
		// 计算缩放比例，使背景填满整个屏幕
		float scaleX = uiCamera.width / (float) background.width;
		float scaleY = uiCamera.height / (float) background.height;
		background.scale.set(scaleX, scaleY);
		
		add(background);
	}
	
	// 初始化游戏
	private void initGame() {
		// 初始化特殊规则
		passbyPawnRow = -1;
		passbyPawnCol = -1;
		canCastleRow0Col0 = true;
		canCastleRow0Col7 = true;
		canCastleRow7Col0 = true;
		canCastleRow7Col7 = true;
		
		// 初始化空棋盘
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				gameBoard[row][col] = EMPTY;
			}
		}
		
		// 放置白方棋子
		gameBoard[0][0] = WHITE_ROOK;
		gameBoard[0][1] = WHITE_KNIGHT;
		gameBoard[0][2] = WHITE_BISHOP;
		gameBoard[0][3] = WHITE_QUEEN;
		gameBoard[0][4] = WHITE_KING;
		gameBoard[0][5] = WHITE_BISHOP;
		gameBoard[0][6] = WHITE_KNIGHT;
		gameBoard[0][7] = WHITE_ROOK;
		
		for (int col = 0; col < BOARD_WIDTH; col++) {
			gameBoard[1][col] = WHITE_PAWN;
		}
		
		// 放置黑方棋子
		gameBoard[7][0] = BLACK_ROOK;
		gameBoard[7][1] = BLACK_KNIGHT;
		gameBoard[7][2] = BLACK_BISHOP;
		gameBoard[7][3] = BLACK_QUEEN;
		gameBoard[7][4] = BLACK_KING;
		gameBoard[7][5] = BLACK_BISHOP;
		gameBoard[7][6] = BLACK_KNIGHT;
		gameBoard[7][7] = BLACK_ROOK;
		
		for (int col = 0; col < BOARD_WIDTH; col++) {
			gameBoard[6][col] = BLACK_PAWN;
		}
	}
	
	// 创建游戏板视觉组件
	private void createBoardVisual() {
		// 计算合适的单元格大小，确保游戏板在屏幕内可见
		float maxPossibleCellSize = Math.min(
			uiCamera.width / (float) BOARD_WIDTH,
			uiCamera.height / (float) BOARD_HEIGHT
		);
		
		// 稍微缩小一点，留出边距
		adjustedCellSize = maxPossibleCellSize * 0.9f;
		
		// 创建棋子按钮数组
		chessButtons = new StyledButton[BOARD_HEIGHT][BOARD_WIDTH];
		
		// 创建每个棋子按钮
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				final int finalRow = row;
				final int finalCol = col;

				// 创建背景图
				NinePatch background = new NinePatch(Assets.Environment.CHESS, 0, ((row+col)%2)*TILE_SIZE, TILE_SIZE, TILE_SIZE, 0);
				// 创建棋子按钮
				chessButtons[row][col] = new StyledButton(background, "") {
					@Override
					protected void onClick() {
						handleChessPieceClick(finalRow, finalCol);
					}
				};
				
				// 设置按钮大小和位置并启用（使用调整后的单元格大小）
				chessButtons[row][col].setSize(adjustedCellSize, adjustedCellSize);
				chessButtons[row][col].setPos(col * adjustedCellSize, row * adjustedCellSize);
				chessButtons[row][col].camera = uiCamera;
				add(chessButtons[row][col]);
			}
		}
		
		// 更新按钮状态
		updateChessButtons();
	}
	
	// 处理棋子按钮点击
	private void handleChessPieceClick(int row, int col) {
		if (!gameRunning) return;
		
		// 如果是AI回合，不处理点击
		if ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite)) {
			return;
		}
		
		if (selectedRow == -1 && selectedCol == -1) {
			// 第一次选择，检查是否选择了自己的棋子
			if (isPlayerPiece(gameBoard[row][col])) {
				selectedRow = row;
				selectedCol = col;
				updateSelectionIndicator();
				showValidMoves(row, col);
			}
		} else {
			// 第二次选择
			if (row == selectedRow && col == selectedCol) {
				// 取消选择
				clearSelection();
			} else if (isPlayerPiece(gameBoard[row][col])) {
				// 选择了另一个自己的棋子
				selectedRow = row;
				selectedCol = col;
				updateSelectionIndicator();
				showValidMoves(row, col);
			} else {
				// 尝试移动棋子
				Runnable action = GetValidMove(selectedRow, selectedCol, row, col);
				if (action != null){
					// 执行移动
					action.run();
					clearMoveIndicators();
					
					// 检查是否轮到AI移动
					if (aiEnabled && ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite))) {
						aiMove();
					}
				}
			}
		}
	}
	
	// 检查是否是当前玩家的棋子
	private boolean isPlayerPiece(int piece) {
		if (piece == EMPTY) return false;
		
		if (playerIsWhite) {
			// 玩家是白方，检查是否是白方棋子
			return piece >= WHITE_KING && piece <= WHITE_PAWN;
		} else {
			// 玩家是黑方，检查是否是黑方棋子
			return piece >= BLACK_KING && piece <= BLACK_PAWN;
		}
	}

	// 检查是否是同阵营棋子
	private boolean isSameSide(int piece, int targetPiece){
		if(piece >= WHITE_KING && piece <= WHITE_PAWN && targetPiece >= WHITE_KING && targetPiece <= WHITE_PAWN){
			return true;
		}

		if(piece >= BLACK_KING && piece <= BLACK_PAWN && targetPiece >= BLACK_KING && targetPiece <= BLACK_PAWN){
			return true;
		}

		return false;
	}

	// 检查是否是不同阵营棋子
	private boolean isOppositeSize(int piece, int targetPiece){
		int a = 0;
		int b = 0;
		if (piece >= WHITE_KING && piece <= WHITE_PAWN){
			a =  1;
		}else if (piece >= BLACK_KING && piece <= BLACK_PAWN){
			a = -1;
		}
		if (targetPiece >= WHITE_KING && targetPiece <= WHITE_PAWN){
			b =  1;
		}else if (targetPiece >= BLACK_KING && targetPiece <= BLACK_PAWN){
			b = -1;
		}
		return a*b==-1;
	}
	
	// 获取有效移动
	private Runnable GetValidMove(int fromRow, int fromCol, int toRow, int toCol) {
		// 边界检查
		if (fromRow < 0 || fromRow >= BOARD_HEIGHT || fromCol < 0 || fromCol >= BOARD_WIDTH ||
			toRow < 0 || toRow >= BOARD_HEIGHT || toCol < 0 || toCol >= BOARD_WIDTH) {
			return null;
		}
		
		int piece = gameBoard[fromRow][fromCol];
		int targetPiece = gameBoard[toRow][toCol];
		
		// 不能移动到同阵营的棋子上
		if (isSameSide(piece, targetPiece)) {
			return null;
		}
		
		// 计算移动距离
		int rowDiff = Math.abs(toRow - fromRow);
		int colDiff = Math.abs(toCol - fromCol);
		
		switch (piece) {
			case WHITE_PAWN:
			case BLACK_PAWN:
				// 兵的移动规则
				int direction = (piece == WHITE_PAWN) ? 1 : -1;
				
				// 第一步可以向前移动两格
				if (colDiff == 0 && 
					((piece == WHITE_PAWN && fromRow == 1) || (piece == BLACK_PAWN && fromRow == 6)) && 
					toRow == fromRow + 2 * direction && 
					gameBoard[fromRow + direction][fromCol] == EMPTY && 
					targetPiece == EMPTY) {
					return ()-> PawnMove(fromRow,fromCol,toRow,toCol);
				}

				// 向前移动一格
				if (colDiff == 0 && toRow == fromRow + direction && targetPiece == EMPTY) {
					return ()-> PawnMove(fromRow,fromCol,toRow,toCol);
				}
				
				
				// 斜向移动
				if (colDiff == 1 && toRow == fromRow + direction) {
					// 吃子
					if (targetPiece != EMPTY) {
						return ()-> PawnMove(fromRow,fromCol,toRow,toCol);
					// 特殊规则 吃过路兵
					}else if (fromRow == passbyPawnRow && toCol == passbyPawnCol){
						return ()->{
							gameBoard[passbyPawnRow][passbyPawnCol] = EMPTY;
							PawnMove(fromRow,fromCol,toRow,toCol);
						};
					}
				}

				break;
			
			case WHITE_ROOK:
			case BLACK_ROOK:
				// 车的移动规则：直线移动，不能有棋子阻挡
				if ((rowDiff == 0 || colDiff == 0) && isPathClear(fromRow,fromCol,toRow,toCol)) {
					return ()->{
						// 特殊规则 标记王车易位失效
						if (fromRow == 0){
							if (fromCol == 0){
								canCastleRow0Col0 = false;
							}else if(fromCol == 7){
								canCastleRow0Col7 = false;
							}
						}else if (fromRow == 7){
							if (fromCol == 0){
								canCastleRow7Col0 = false;
							}else if(fromCol == 7){
								canCastleRow7Col7 = false;
							}
						}
						CommonMove(fromRow,fromCol,toRow,toCol);
					};
				}
				break;
			
			case WHITE_BISHOP:
			case BLACK_BISHOP:
				// 象的移动规则：对角线移动，不能有棋子阻挡
				if ((rowDiff == colDiff) && isPathClear(fromRow,fromCol,toRow,toCol)) {
					return ()-> CommonMove(fromRow,fromCol,toRow,toCol);
				}
				break;
			
			case WHITE_QUEEN:
			case BLACK_QUEEN:
				// 后的移动规则：直线或对角线移动，不能有棋子阻挡
				if ((rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) && isPathClear(fromRow,fromCol,toRow,toCol)){
					return ()-> CommonMove(fromRow,fromCol,toRow,toCol);
				}
				break;
			
			case WHITE_KNIGHT:
			case BLACK_KNIGHT:
				// 马的移动规则：日字形移动
				if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)){
					return ()-> CommonMove(fromRow,fromCol,toRow,toCol);
				}
				break;
			
			case WHITE_KING:
			case BLACK_KING:
				// 王的移动规则：一格范围内的任何方向 
				if ((rowDiff <= 1 && colDiff <= 1) && (rowDiff + colDiff != 0)) {
					return ()->{
						// 特殊规则 标记王车易位失效
						if (piece == WHITE_KING){
							canCastleRow0Col0 = false;
							canCastleRow0Col7 = false;
						}else{
							canCastleRow7Col0 = false;
							canCastleRow7Col7 = false;
						}
						CommonMove(fromRow,fromCol,toRow,toCol);
					};
				}
				
				// 特殊规则 王车易位
				boolean castle = false;
				int rookFromRow = -1;
				int rookFromCol = -1;
				int rookToRow   = -1;
				int rookToCol   = -1;
				if (piece == WHITE_KING){
					if       (canCastleRow0Col0 && toRow == 0 && toCol == KING_COLUMN-2){
						castle = true;
						rookFromRow = 0;
						rookFromCol = 0;
						rookToRow   = rookFromRow;
						rookToCol   = KING_COLUMN-1;
					}else if (canCastleRow0Col7 && toRow == 0 && toCol == KING_COLUMN+2){
						castle = true;
						rookFromRow = 0;
						rookFromCol = 7;
						rookToRow   = rookFromRow;
						rookToCol   = KING_COLUMN+1;
					}
				}else{
					if       (canCastleRow7Col0 && toRow == 7 && toCol == KING_COLUMN-2){
						castle = true;
						rookFromRow = 7;
						rookFromCol = 0;
						rookToRow   = rookFromRow;
						rookToCol   = KING_COLUMN-1;
					}else if (canCastleRow7Col7 && toRow == 7 && toCol == KING_COLUMN+2){
						castle = true;
						rookFromRow = 7;
						rookFromCol = 7;
						rookToRow   = rookFromRow;
						rookToCol   = KING_COLUMN+1;
					}
				}
				if (castle
					&& isPathClear(rookFromRow,rookFromCol,fromRow,fromCol)
					&& isCellSafe(piece, fromRow, fromCol)
					&& isCellSafe(piece, (fromRow+toRow)/2, (fromCol+toCol)/2)
					&& isCellSafe(piece, toRow, toCol)){
					// 可以进行王车易位
					int finalRookFromRow = rookFromRow;
					int finalRookFromCol = rookFromCol;
					int finalRookToRow   = rookToRow  ;
					int finalRookToCol   = rookToCol  ;
					return ()->{
						// 特殊规则 标记王车易位失效
						if (piece == WHITE_KING){
							canCastleRow0Col0 = false;
							canCastleRow0Col7 = false;
						}else{
							canCastleRow7Col0 = false;
							canCastleRow7Col7 = false;
						}
						// 特殊规则 王车易位:移动车
						gameBoard[finalRookToRow][finalRookToCol] = gameBoard[finalRookFromRow][finalRookFromCol];
						gameBoard[finalRookFromRow][finalRookFromCol] = EMPTY;
						// 特殊规则 王车易位:移动王
						CommonMove(fromRow,fromCol,toRow,toCol);
					};
				}

				break;
		}
		
		return null;
	}

	// 普通移动和吃子
	private void CommonMove(int fromRow, int fromCol, int toRow, int toCol){
		gameBoard[toRow][toCol] = gameBoard[fromRow][fromCol];
		gameBoard[fromRow][fromCol] = EMPTY;

		// 特殊规则 标记王车易位失效
		if (toRow == 0){
			if (toCol == 0){
				canCastleRow0Col0 = false;
			}else if(toCol == 7){
				canCastleRow0Col7 = false;
			}
		}else if (toRow == 7){
			if (toCol == 0){
				canCastleRow7Col0 = false;
			}else if(toCol == 7){
				canCastleRow7Col7 = false;
			}
		}

		// 特殊规则 清除过路兵标记
		passbyPawnCol = -1;
		passbyPawnRow = -1;

		// 结束回合
		whiteTurn = !whiteTurn;
		clearSelection();
		updateChessButtons();
	}

	// 兵普通移动和吃子
	private void PawnMove(int fromRow, int fromCol, int toRow, int toCol){
		gameBoard[toRow][toCol] = gameBoard[fromRow][fromCol];
		gameBoard[fromRow][fromCol] = EMPTY;

		// 特殊规则 标记王车易位失效
		if (toRow == 0){
			if (toCol == 0){
				canCastleRow0Col0 = false;
			}else if(toCol == 7){
				canCastleRow0Col7 = false;
			}
		}else if (toRow == 7){
			if (toCol == 0){
				canCastleRow7Col0 = false;
			}else if(toCol == 7){
				canCastleRow7Col7 = false;
			}
		}

		if (Math.abs(toRow - fromRow) == 2){
			// 特殊规则 标记过路兵
			passbyPawnCol = toCol;
			passbyPawnRow = toRow;
		}else{
			// 特殊规则 清除过路兵标记
			passbyPawnCol = -1;
			passbyPawnRow = -1;
		}

		if (toRow == 7 || toRow == 0){
			// 特殊规则 底线升变
			addToFront(new WndPromotion(toRow, toCol));
		}else{
			// 正常结束回合
			whiteTurn = !whiteTurn;
		}

		updateChessButtons();
		clearSelection();
	}

	// 检查移动路径是否畅通
	private boolean isPathClear(int fromRow, int fromCol, int toRow, int toCol) {
		int rowStep = (toRow > fromRow) ? 1 : (toRow < fromRow) ? -1 : 0;
		int colStep = (toCol > fromCol) ? 1 : (toCol < fromCol) ? -1 : 0;
		
		int currentRow = fromRow + rowStep;
		int currentCol = fromCol + colStep;
		
		while (currentRow != toRow || currentCol != toCol) {
			if (gameBoard[currentRow][currentCol] != EMPTY) {
				return false;
			}
			currentRow += rowStep;
			currentCol += colStep;
		}
		
		return true;
	}

	// 检查格子是否安全
	private boolean isCellSafe(int checkerPiece, int cellRow, int cellCol){
		for (int row=0; row<BOARD_HEIGHT; row++) {
		for (int col=0; col<BOARD_WIDTH ; col++) {
			if(!isOppositeSize(gameBoard[row][col], checkerPiece)){
				continue;
			}

			Runnable action = GetValidMove(row,col, cellRow,cellCol);
			if (action != null){
				return false;
			}
		}
		}

		return true;
	}

	// 兵升变选择窗口
	private class WndPromotion extends Window {
		
		private static final int WIDTH = 120;
		private static final int BTN_HEIGHT = 30;
		private static final float GAP = 2;
		private int row;
		private int col;
		
		public WndPromotion(int row, int col) {
			super();
			
			this.row = row;
			this.col = col;
			
			// 设置窗口大小
			resize(WIDTH, (int)(BTN_HEIGHT * 4 + GAP * 3));
			
			// 升变选择按钮
			int[] promotionPieces = whiteTurn ? 
				new int[]{WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT} :
				new int[]{BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT};
			
			String[] pieceNames = new String[]{"后", "车", "象", "马"};
			
			for (int i = 0; i < promotionPieces.length; i++) {
				final int finalPiece = promotionPieces[i];
				StyledButton btn = new StyledButton(Chrome.Type.TOAST_TR, pieceNames[i]) {
					@Override
					protected void onClick() {
						// 执行升变
						gameBoard[row][col] = finalPiece;
						whiteTurn = !whiteTurn;
						updateChessButtons();
						WndPromotion.this.hide();
						
						// 检查是否轮到AI移动
						if (aiEnabled && ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite))) {
							aiMove();
						}
					}
				};
				
				btn.setSize(WIDTH, BTN_HEIGHT);
				btn.setPos(0, i * (BTN_HEIGHT + GAP));
				add(btn);
			}
		}
		
		@Override
		public void onBackPressed() {
			// 执行升变
			gameBoard[row][col] = whiteTurn ? WHITE_QUEEN : BLACK_QUEEN;
			whiteTurn = !whiteTurn;
			updateChessButtons();
			hide();
			
			// 检查是否轮到AI移动
			if (aiEnabled && ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite))) {
				aiMove();
			}
		}
	}
	
	// 更新棋子按钮状态
	private void updateChessButtons() {
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				StyledButton button = chessButtons[row][col];
				int pieceType = gameBoard[row][col];
				
				// 设置按钮内容
				if (pieceType != EMPTY) {
					// 创建并设置按钮图标
					Image pieceImg = createPieceImage(pieceType);
					button.icon(pieceImg);
				} else {
					// 清除图标
					button.icon(null);
				}
			}
		}
	}
	
	// 创建棋子图像
	private Image createPieceImage(int pieceType) {
		// 定义不同棋子在贴图中的位置
		int row=0, col=0;
		switch (pieceType) {
			// 黑色棋子（第一排）
			case BLACK_PAWN:   // 黑色小兵
				row = 0; col = 1;
				break;
			case BLACK_ROOK:   // 黑色车
				row = 0; col = 2;
				break;
			case BLACK_KNIGHT: // 黑色马
				row = 0; col = 3;
				break;
			case BLACK_BISHOP: // 黑色象
				row = 0; col = 4;
				break;
			case BLACK_KING:   // 黑色国王
				row = 0; col = 5;
				break;
			case BLACK_QUEEN:  // 黑色后
				row = 0; col = 6;
				break;
			// 白色棋子（第二排）
			case WHITE_PAWN:   // 白色小兵
				row = 1; col = 1;
				break;
			case WHITE_ROOK:   // 白色车
				row = 1; col = 2;
				break;
			case WHITE_KNIGHT: // 白色马
				row = 1; col = 3;
				break;
			case WHITE_BISHOP: // 白色象
				row = 1; col = 4;
				break;
			case WHITE_KING:   // 白色国王
				row = 1; col = 5;
				break;
			case WHITE_QUEEN:  // 白色后
				row = 1; col = 6;
				break;
		}
		
		// 创建一个新图像，仅显示贴图中的指定部分
		Image pieceImg = new Image(Assets.Environment.CHESS);
		
		// 设置图像显示的区域（从贴图中裁剪出指定棋子）
		pieceImg.frame(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		
		// 设置图像大小（使用动态调整的单元格大小）
		float scale = (adjustedCellSize - 8) / pieceImg.width;
		pieceImg.scale.set(scale);
		
		return pieceImg;
	}
	
	// 添加控制按钮
	private void addControlButtons() {
		float buttonWidth = uiCamera.width * 0.2f;
		float buttonHeight = uiCamera.height * 0.05f;
		float margin = 20;
		
		// 重启按钮
		restartButton = new StyledButton(Chrome.Type.TOAST_TR, "重启") {
			@Override
			protected void onClick() {
				restartGame();
			}
		};
		restartButton.setSize(buttonWidth, buttonHeight);
		restartButton.setPos(
			(uiCamera.width - buttonWidth * 3 - margin * 2) / 2,
			uiCamera.height - buttonHeight - margin
		);
		restartButton.camera = uiCamera;
		add(restartButton);
		
		// AI按钮
		aiButton = new StyledButton(Chrome.Type.TOAST_TR, aiEnabled ? "AI: 开" : "AI: 关") {
			@Override
			protected void onClick() {
				aiEnabled = !aiEnabled;
				text(aiEnabled ? "AI: 开" : "AI: 关");
				
				// 如果AI被启用且现在轮到AI移动，执行AI移动
				if (aiEnabled && ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite))) {
					aiMove();
				}
			}
		};
		aiButton.setSize(buttonWidth, buttonHeight);
		aiButton.setPos(
			restartButton.right() + margin,
			uiCamera.height - buttonHeight - margin
		);
		aiButton.camera = uiCamera;
		add(aiButton);
		
		// 退出按钮
		exitButton = new StyledButton(Chrome.Type.TOAST_TR, "退出") {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		exitButton.setSize(buttonWidth, buttonHeight);
		exitButton.setPos(
			aiButton.right() + margin,
			uiCamera.height - buttonHeight - margin
		);
		exitButton.camera = uiCamera;
		add(exitButton);
	}
	
	// 创建选择指示器
	private void createSelectionIndicator() {
		// 使用一个简单的Image作为选择指示器
		selectionIndicator = new Image(Icons.get(Icons.TARGET));
		selectionIndicator.hardlight(0xFFFFFF); // 设置颜色为白色
		selectionIndicator.visible = false;
		selectionIndicator.camera = uiCamera;
		
		// 设置选择指示器的大小，使其适合单元格
		float scale = adjustedCellSize / selectionIndicator.width();
		selectionIndicator.scale.set(scale);
		
		add(selectionIndicator);
	}
	
	// 更新选择指示器位置
	private void updateSelectionIndicator() {
		if (selectedRow == -1 || selectedCol == -1) {
			selectionIndicator.visible = false;
			return;
		}
		
		// 计算选择指示器的位置（基于按钮位置）
		float cellX = selectedCol * adjustedCellSize;
		float cellY = selectedRow * adjustedCellSize;
		
		// 设置选择指示器的位置，使其居中于选中的单元格
		selectionIndicator.setPos(cellX,cellY);
		selectionIndicator.visible = true;
	}
	
	// 清除选择
	private void clearSelection() {
		selectedRow = -1;
		selectedCol = -1;
		selectionIndicator.visible = false;
		clearMoveIndicators();
	}

	// 显示可走路径标记
	private void showValidMoves(int fromRow, int fromCol) {
		// 先清除之前的标记
		clearMoveIndicators();
		
		// 遍历所有可能的目标位置
		for (int toRow = 0; toRow < BOARD_HEIGHT; toRow++) {
			for (int toCol = 0; toCol < BOARD_WIDTH; toCol++) {
				// 跳过自己的位置
				if (toRow == fromRow && toCol == fromCol) {
					continue;
				}
				
				// 检查是否是有效的移动
				Runnable action = GetValidMove(fromRow, fromCol, toRow, toCol);
				if (action != null) {
					// 添加可走路径标记
					addMoveIndicator(toRow, toCol);
				}
			}
		}
	}

	// 添加可走路径标记
	private void addMoveIndicator(int row, int col) {
		// 创建标记图像
		Image indicator = new Image(Icons.get(Icons.TARGET));
		indicator.hardlight(0x00FF00); // 设置颜色为绿色
		indicator.camera = uiCamera;
		
		// 设置标记大小
		float scale = adjustedCellSize / indicator.width();
		indicator.scale.set(scale);
		
		// 设置标记位置
		float cellX = col * adjustedCellSize;
		float cellY = row * adjustedCellSize;
		indicator.setPos(cellX, cellY);
		
		// 添加标记到容器和列表
		add(indicator);
		moveIndicators.add(indicator);
	}

	// 清除可走路径标记
	private void clearMoveIndicators() {
		for (Image indicator : moveIndicators) {
			if (indicator != null) {
				indicator.killAndErase();
			}
		}
		moveIndicators.clear();
	}

	// 重启游戏
	private void restartGame() {
		// 重置游戏状态
		clearSelection();
		clearMoveIndicators();
		
		// 重新初始化游戏
		initGame();
		
		// 更新游戏板显示
		updateChessButtons();
		
		// 重置游戏状态
		gameRunning = false;
		whiteTurn = true;
		aiEnabled = true;
		
		// 显示棋子选择窗口
		showSideSelectionWindow();
	}

	// 显示棋子选择窗口
	private void showSideSelectionWindow() {
		addToFront(new WndSideSelection());
	}

	// 棋子选择窗口
	private class WndSideSelection extends Window {
		private static final int WIDTH = 150;
		private static final int BTN_HEIGHT = 40;
		private static final float GAP = 2;
		
		public WndSideSelection() {
			super();
			
			// 设置窗口大小
			resize(WIDTH, (int)(BTN_HEIGHT * 2 + GAP));
			
			// 白棋选择按钮
			StyledButton whiteBtn = new StyledButton(Chrome.Type.TOAST_TR, "白棋 (先手)") {
				@Override
				protected void onClick() {
					// 选择白棋，开始游戏
					startGameAsWhite();
					hide();
				}
			};
			whiteBtn.setSize(WIDTH, BTN_HEIGHT);
			whiteBtn.setPos(0, 0);
			add(whiteBtn);
			
			// 黑棋选择按钮
			StyledButton blackBtn = new StyledButton(Chrome.Type.TOAST_TR, "黑棋 (后手)") {
				@Override
				protected void onClick() {
					// 选择黑棋，开始游戏
					startGameAsBlack();
					hide();
				}
			};
			blackBtn.setSize(WIDTH, BTN_HEIGHT);
			blackBtn.setPos(0, BTN_HEIGHT + GAP);
			add(blackBtn);
		}
	}

	// 以白棋开始游戏
	private void startGameAsWhite() {
		// 重置游戏状态
		initGame();
		updateChessButtons();
		gameRunning = true;
		whiteTurn = true;
		playerIsWhite = true;
	}

	// 以黑棋开始游戏
	private void startGameAsBlack() {
		// 重置游戏状态
		initGame();
		// 交换棋盘上的棋子
		swapBoardSides();
		updateChessButtons();
		gameRunning = true;
		whiteTurn = true;
		playerIsWhite = false;
		
		// 如果AI启用，执行AI移动
		if (aiEnabled) {
			aiMove();
		}
	}

	// 交换棋盘上的棋子
	private void swapBoardSides() {
		// 临时棋盘用于存储交换后的棋子
		int[][] tempBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
		
		// 交换棋子
		for (int row = 0; row < BOARD_HEIGHT; row++) {
			for (int col = 0; col < BOARD_WIDTH; col++) {
				int piece = gameBoard[row][col];
				if (piece != EMPTY) {
					// 计算交换后的位置
					int newRow = BOARD_HEIGHT - 1 - row;
					// 根据棋子类型转换为对方颜色
					switch (piece) {
						case WHITE_KING: tempBoard[newRow][col] = BLACK_KING; break;
						case WHITE_QUEEN: tempBoard[newRow][col] = BLACK_QUEEN; break;
						case WHITE_ROOK: tempBoard[newRow][col] = BLACK_ROOK; break;
						case WHITE_BISHOP: tempBoard[newRow][col] = BLACK_BISHOP; break;
						case WHITE_KNIGHT: tempBoard[newRow][col] = BLACK_KNIGHT; break;
						case WHITE_PAWN: tempBoard[newRow][col] = BLACK_PAWN; break;
						case BLACK_KING: tempBoard[newRow][col] = WHITE_KING; break;
						case BLACK_QUEEN: tempBoard[newRow][col] = WHITE_QUEEN; break;
						case BLACK_ROOK: tempBoard[newRow][col] = WHITE_ROOK; break;
						case BLACK_BISHOP: tempBoard[newRow][col] = WHITE_BISHOP; break;
						case BLACK_KNIGHT: tempBoard[newRow][col] = WHITE_KNIGHT; break;
						case BLACK_PAWN: tempBoard[newRow][col] = WHITE_PAWN; break;
					}
				}
			}
		}
		
		// 复制临时棋盘到游戏棋盘
		gameBoard = tempBoard;
		
		// 交换王车易位标记
		boolean tempCanCastleRow0Col0 = canCastleRow0Col0;
		boolean tempCanCastleRow0Col7 = canCastleRow0Col7;
		canCastleRow0Col0 = canCastleRow7Col0;
		canCastleRow0Col7 = canCastleRow7Col7;
		canCastleRow7Col0 = tempCanCastleRow0Col0;
		canCastleRow7Col7 = tempCanCastleRow0Col7;
	}

	// AI移动
	private void aiMove() {
		if (!gameRunning || !aiEnabled) return;
		
		// 收集所有有效的移动
		List<Move> validMoves = new ArrayList<>();
		
		for (int fromRow = 0; fromRow < BOARD_HEIGHT; fromRow++) {
			for (int fromCol = 0; fromCol < BOARD_WIDTH; fromCol++) {
				int piece = gameBoard[fromRow][fromCol];
				// 只考虑AI自己的棋子
				if ((whiteTurn && piece >= WHITE_KING && piece <= WHITE_PAWN) || 
				    (!whiteTurn && piece >= BLACK_KING && piece <= BLACK_PAWN)) {
					for (int toRow = 0; toRow < BOARD_HEIGHT; toRow++) {
						for (int toCol = 0; toCol < BOARD_WIDTH; toCol++) {
							Runnable action = GetValidMove(fromRow, fromCol, toRow, toCol);
							if (action != null) {
								validMoves.add(new Move(fromRow, fromCol, toRow, toCol, action));
							}
						}
					}
				}
			}
		}
		
		// 如果有有效的移动，随机选择一个执行
		if (!validMoves.isEmpty()) {
			final Move selectedMove = validMoves.get(Random.Int(validMoves.size()));
			selectedMove.action.run();
			
			// 检查是否轮到AI移动（例如，兵升变后）
			if (aiEnabled && ((whiteTurn && !playerIsWhite) || (!whiteTurn && playerIsWhite))) {
				aiMove();
			}
		}
	}

	// 移动类
	private class Move {
		int fromRow;
		int fromCol;
		int toRow;
		int toCol;
		Runnable action;
		
		Move(int fromRow, int fromCol, int toRow, int toCol, Runnable action) {
			this.fromRow = fromRow;
			this.fromCol = fromCol;
			this.toRow = toRow;
			this.toCol = toCol;
			this.action = action;
		}
	}

	// 处理返回键
	@Override
	protected void onBackPressed() {
		// 返回上一个场景
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene(InterlevelScene.class);
	}
}
