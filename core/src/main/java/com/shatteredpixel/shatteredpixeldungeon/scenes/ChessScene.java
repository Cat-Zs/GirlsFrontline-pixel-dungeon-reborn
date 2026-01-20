package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessScene extends PixelScene {
    {
        inGameScene = true;
    }

    // 游戏常量定义
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;
    private static final int EMPTY = 0;
    private static final int CELL_SIZE = 40; // 象棋棋盘单元格大小
    
    // 动态调整的单元格大小
    private float adjustedCellSize = CELL_SIZE;
    
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
    private int[][] gameBoard;
    private boolean gameRunning;
    private boolean whiteTurn; // 白方回合
    
    // 棋子移动状态
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteKingSideRookMoved = false;
    private boolean whiteQueenSideRookMoved = false;
    private boolean blackKingSideRookMoved = false;
    private boolean blackQueenSideRookMoved = false;
    private boolean[] pawnTwoStepped = new boolean[BOARD_HEIGHT * BOARD_WIDTH]; // 记录兵是否刚刚走了两步
    
    // AI相关变量
    private boolean aiEnabled = false;
    private boolean aiThinking = false;
    
    // 选择状态
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Image selectionIndicator;
    
    // 渲染组件
    private Image background;
    private Component boardContainer;
    private StyledButton[][] chessButtons;
    private StyledButton restartButton;
    private StyledButton exitButton;
    private StyledButton aiButton;
    
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
        gameRunning = true;
        whiteTurn = true;
        
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
        // 创建游戏板
        gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        
        // 初始化棋子移动状态
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteKingSideRookMoved = false;
        whiteQueenSideRookMoved = false;
        blackKingSideRookMoved = false;
        blackQueenSideRookMoved = false;
        pawnTwoStepped = new boolean[BOARD_HEIGHT * BOARD_WIDTH];
        
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
    
    // 检查目标格子是否可以移动
    private boolean isValidSquare(int targetPiece) {
        return targetPiece == EMPTY || isOpponentPiece(targetPiece);
    }
    
    // 检查是否被将军
    private boolean isInCheck(boolean whitePlayer) {
        // 找到国王位置
        int kingRow = -1;
        int kingCol = -1;
        int kingPiece = whitePlayer ? WHITE_KING : BLACK_KING;
        
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (gameBoard[row][col] == kingPiece) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
            if (kingRow != -1) break;
        }
        
        // 如果没有找到国王，说明已经被将杀（理论上不应该发生）
        if (kingRow == -1 || kingCol == -1) {
            return false;
        }
        
        // 检查是否有对方的棋子可以攻击到国王
        // 直接实现攻击检查，避免调用isValidMove导致无限递归
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                int piece = gameBoard[row][col];
                if (piece == EMPTY) {
                    continue;
                }
                
                // 检查是否是对方的棋子
                boolean isOpponent = false;
                if (whitePlayer) {
                    isOpponent = (piece == BLACK_PAWN) || (piece == BLACK_ROOK) || (piece == BLACK_KNIGHT) ||
                               (piece == BLACK_BISHOP) || (piece == BLACK_QUEEN) || (piece == BLACK_KING);
                } else {
                    isOpponent = (piece == WHITE_PAWN) || (piece == WHITE_ROOK) || (piece == WHITE_KNIGHT) ||
                               (piece == WHITE_BISHOP) || (piece == WHITE_QUEEN) || (piece == WHITE_KING);
                }
                
                if (!isOpponent) {
                    continue;
                }
                
                int rowDiff = Math.abs(kingRow - row);
                int colDiff = Math.abs(kingCol - col);
                
                switch (piece) {
                    case WHITE_PAWN:
                    case BLACK_PAWN:
                        // 兵的攻击规则：斜向一格
                        int pawnDirection = (piece == WHITE_PAWN) ? 1 : -1;
                        if (rowDiff == 1 && colDiff == 1 && (kingRow - row == pawnDirection)) {
                            return true;
                        }
                        break;
                        
                    case WHITE_ROOK:
                    case BLACK_ROOK:
                        // 车的攻击规则：直线移动，不能有棋子阻挡
                        if ((rowDiff == 0 || colDiff == 0) && isPathClear(row, col, kingRow, kingCol)) {
                            return true;
                        }
                        break;
                        
                    case WHITE_BISHOP:
                    case BLACK_BISHOP:
                        // 象的攻击规则：对角线移动，不能有棋子阻挡
                        if (rowDiff == colDiff && isPathClear(row, col, kingRow, kingCol)) {
                            return true;
                        }
                        break;
                        
                    case WHITE_QUEEN:
                    case BLACK_QUEEN:
                        // 后的攻击规则：直线或对角线移动，不能有棋子阻挡
                        if ((rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) && isPathClear(row, col, kingRow, kingCol)) {
                            return true;
                        }
                        break;
                        
                    case WHITE_KING:
                    case BLACK_KING:
                        // 王的攻击规则：一格范围内的任何方向
                        if (rowDiff <= 1 && colDiff <= 1 && (rowDiff != 0 || colDiff != 0)) {
                            return true;
                        }
                        break;
                        
                    case WHITE_KNIGHT:
                    case BLACK_KNIGHT:
                        // 马的攻击规则：日字形移动
                        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
                            return true;
                        }
                        break;
                }
            }
        }
        
        return false;
    }
    
    // 检查是否被将杀
    private boolean isCheckmate(boolean whitePlayer) {
        if (!isInCheck(whitePlayer)) {
            return false;
        }
        
        // 检查是否有任何合法的移动可以解除将军
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                int piece = gameBoard[row][col];
                if (isPlayerPiece(piece)) {
                    for (int toRow = 0; toRow < BOARD_HEIGHT; toRow++) {
                        for (int toCol = 0; toCol < BOARD_WIDTH; toCol++) {
                            if (isValidMove(row, col, toRow, toCol)) {
                                // 模拟移动
                                int originalPiece = gameBoard[toRow][toCol];
                                gameBoard[toRow][toCol] = piece;
                                gameBoard[row][col] = EMPTY;
                                
                                // 检查是否还在将军
                                boolean stillInCheck = isInCheck(whitePlayer);
                                
                                // 恢复移动
                                gameBoard[row][col] = piece;
                                gameBoard[toRow][toCol] = originalPiece;
                                
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
    
    // 创建游戏板视觉组件
    private void createBoardVisual() {
        // 计算合适的单元格大小，确保游戏板在屏幕内可见
        float maxPossibleCellSize = Math.min(
            uiCamera.width / (float) BOARD_WIDTH,
            uiCamera.height / (float) BOARD_HEIGHT
        );
        
        // 稍微缩小一点，留出边距
        float adjustedCellSize = maxPossibleCellSize * 0.9f;
        
        // 创建游戏板容器
        boardContainer = new Component();
        boardContainer.camera = uiCamera;
        
        // 设置游戏板大小（8*8）
        float totalWidth = adjustedCellSize * BOARD_WIDTH;
        float totalHeight = adjustedCellSize * BOARD_HEIGHT;
        
        boardContainer.setSize(totalWidth, totalHeight);
        
        // 设置游戏板位置（居中）
        boardContainer.setPos(
            (uiCamera.width - boardContainer.width()) / 2,
            (uiCamera.height - boardContainer.height()) / 2
        );
        
        add(boardContainer);
        
        // 创建棋子按钮数组
        chessButtons = new StyledButton[BOARD_HEIGHT][BOARD_WIDTH];
        
        // 创建每个棋子按钮
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                final int finalRow = row;
                final int finalCol = col;
                
                // 创建棋子按钮
                chessButtons[row][col] = new StyledButton(Chrome.Type.GREY_BUTTON, "") {
                    @Override
                    protected void onClick() {
                        handleChessPieceClick(finalRow, finalCol);
                    }
                };
                
                // 设置按钮大小和位置（使用调整后的单元格大小）
                chessButtons[row][col].setSize(adjustedCellSize, adjustedCellSize);
                chessButtons[row][col].setPos(col * adjustedCellSize, row * adjustedCellSize);
                chessButtons[row][col].camera = uiCamera;
                
                // 添加按钮到容器
                boardContainer.add(chessButtons[row][col]);
                
                // 设置棋盘格子颜色（国际象棋棋盘样式）
                if ((row + col) % 2 == 0) {
                    // 白色格子
                    chessButtons[row][col].text("");
                } else {
                    // 黑色格子
                    chessButtons[row][col].text("");
                }
            }
        }
        
        // 更新按钮状态
        updateChessButtons();
        
        // 更新棋子图像大小（使用调整后的单元格大小）
        this.adjustedCellSize = adjustedCellSize;
    }
    
    // 处理棋子按钮点击
    private void handleChessPieceClick(int row, int col) {
        if (!gameRunning) return;
        
        if (selectedRow == -1 && selectedCol == -1) {
            // 第一次选择，检查是否选择了自己的棋子
            if (isPlayerPiece(gameBoard[row][col])) {
                selectedRow = row;
                selectedCol = col;
                updateSelectionIndicator();
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
            } else {
                // 尝试移动棋子
                if (isValidMove(selectedRow, selectedCol, row, col)) {
                    // 执行移动
                    movePiece(selectedRow, selectedCol, row, col);
                    
                    // 注意：回合切换和AI移动的逻辑现在在movePiece和WndPromotion中处理
                }
                
                // 清除选择
                clearSelection();
            }
        }
    }
    
    // 检查是否是当前玩家的棋子
    private boolean isPlayerPiece(int piece) {
        if (piece == EMPTY) return false;
        
        if (whiteTurn) {
            // 白方回合，检查是否是白方棋子
            return piece >= WHITE_KING && piece <= WHITE_PAWN;
        } else {
            // 黑方回合，检查是否是黑方棋子
            return piece >= BLACK_KING && piece <= BLACK_PAWN;
        }
    }
    
    // 检查是否是对方玩家的棋子
    private boolean isOpponentPiece(int piece) {
        if (piece == EMPTY) return false;
        return !isPlayerPiece(piece);
    }
    
    // 检查移动是否有效（实现完整的国际象棋规则）
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // 边界检查
        if (fromRow < 0 || fromRow >= BOARD_HEIGHT || fromCol < 0 || fromCol >= BOARD_WIDTH ||
            toRow < 0 || toRow >= BOARD_HEIGHT || toCol < 0 || toCol >= BOARD_WIDTH) {
            return false;
        }
        
        int piece = gameBoard[fromRow][fromCol];
        int targetPiece = gameBoard[toRow][toCol];
        
        // 不能移动到自己的棋子上
        if (isPlayerPiece(targetPiece)) {
            return false;
        }
        
        // 计算移动距离
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        boolean validMove = false;
        
        switch (piece) {
            case WHITE_PAWN:
            case BLACK_PAWN:
                // 兵的移动规则
                int direction = (piece == WHITE_PAWN) ? 1 : -1;
                
                // 向前移动一格
                if (colDiff == 0 && toRow == fromRow + direction && targetPiece == EMPTY) {
                    validMove = true;
                }
                
                // 第一步可以向前移动两格
                if (colDiff == 0 && 
                    ((piece == WHITE_PAWN && fromRow == 1) || (piece == BLACK_PAWN && fromRow == 6)) && 
                    toRow == fromRow + 2 * direction && 
                    gameBoard[fromRow + direction][fromCol] == EMPTY && 
                    targetPiece == EMPTY) {
                    validMove = true;
                }
                
                // 吃子（斜向移动）
                if (rowDiff == 1 && colDiff == 1 && isOpponentPiece(targetPiece)) {
                    validMove = true;
                }
                
                // 吃过路兵
                if (rowDiff == 1 && colDiff == 1 && targetPiece == EMPTY) {
                    int pawnRow = fromRow;
                    int pawnCol = toCol;
                    int opponentPiece = (piece == WHITE_PAWN) ? BLACK_PAWN : WHITE_PAWN;
                    
                    if (gameBoard[pawnRow][pawnCol] == opponentPiece) {
                        // 检查对方的兵是否刚刚走了两步
                        if (pawnTwoStepped[pawnRow * BOARD_WIDTH + pawnCol]) {
                            validMove = true;
                        }
                    }
                }
                break;
                
            case WHITE_ROOK:
            case BLACK_ROOK:
                // 车的移动规则：直线移动，不能有棋子阻挡
                if (rowDiff == 0 || colDiff == 0) {
                    validMove = isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
                
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                // 象的移动规则：对角线移动，不能有棋子阻挡
                if (rowDiff == colDiff) {
                    validMove = isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
                
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                // 后的移动规则：直线或对角线移动，不能有棋子阻挡
                if (rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) {
                    validMove = isPathClear(fromRow, fromCol, toRow, toCol);
                }
                break;
                
            case WHITE_KING:
            case BLACK_KING:
                // 王的移动规则：一格范围内的任何方向
                if ((rowDiff <= 1 && colDiff <= 1) && (rowDiff != 0 || colDiff != 0)) {
                    validMove = isValidSquare(targetPiece);
                }
                
                // 王车异位
                // 为了避免无限递归，直接检查国王是否被攻击，而不是调用isInCheck
                boolean kingInCheck = false;
                int kingPiece = piece;
                int opponentKingPiece = (kingPiece == WHITE_KING) ? BLACK_KING : WHITE_KING;
                
                // 检查国王是否被攻击（简化版，避免递归）
                for (int row = 0; row < BOARD_HEIGHT && !kingInCheck; row++) {
                    for (int col = 0; col < BOARD_WIDTH && !kingInCheck; col++) {
                        int opponentPiece = gameBoard[row][col];
                        if (opponentPiece != EMPTY && opponentPiece != opponentKingPiece) {
                            // 检查对方棋子是否能直接攻击国王
                            int tempFromRow = row;
                            int tempFromCol = col;
                            int tempToRow = fromRow;
                            int tempToCol = fromCol;
                            
                            // 简化的攻击检查，只考虑基本移动规则，不考虑王车异位
                            int tempRowDiff = Math.abs(tempToRow - tempFromRow);
                            int tempColDiff = Math.abs(tempToCol - tempFromCol);
                            
                            switch (opponentPiece) {
                                case WHITE_PAWN:
                                case BLACK_PAWN:
                                    int pawnDirection = (opponentPiece == WHITE_PAWN) ? 1 : -1;
                                    if (tempRowDiff == 1 && tempColDiff == 1) {
                                        kingInCheck = true;
                                    }
                                    break;
                                case WHITE_ROOK:
                                case BLACK_ROOK:
                                    if ((tempRowDiff == 0 || tempColDiff == 0) && isPathClear(tempFromRow, tempFromCol, tempToRow, tempToCol)) {
                                        kingInCheck = true;
                                    }
                                    break;
                                case WHITE_BISHOP:
                                case BLACK_BISHOP:
                                    if (tempRowDiff == tempColDiff && isPathClear(tempFromRow, tempFromCol, tempToRow, tempToCol)) {
                                        kingInCheck = true;
                                    }
                                    break;
                                case WHITE_QUEEN:
                                case BLACK_QUEEN:
                                    if ((tempRowDiff == 0 || tempColDiff == 0 || tempRowDiff == tempColDiff) && isPathClear(tempFromRow, tempFromCol, tempToRow, tempToCol)) {
                                        kingInCheck = true;
                                    }
                                    break;
                                case WHITE_KING:
                                case BLACK_KING:
                                    if (tempRowDiff <= 1 && tempColDiff <= 1) {
                                        kingInCheck = true;
                                    }
                                    break;
                                case WHITE_KNIGHT:
                                case BLACK_KNIGHT:
                                    if ((tempRowDiff == 2 && tempColDiff == 1) || (tempRowDiff == 1 && tempColDiff == 2)) {
                                        kingInCheck = true;
                                    }
                                    break;
                            }
                        }
                    }
                }
                
                if (!kingInCheck && !hasPieceMoved(piece, fromRow, fromCol)) {
                    // 短易位（王翼）
                    if (colDiff == 2 && toRow == fromRow && targetPiece == EMPTY) {
                        // 检查路径是否清空
                        if (isPathClear(fromRow, fromCol, toRow, toCol - 1) && 
                            gameBoard[fromRow][toCol + 1] != EMPTY && 
                            !hasPieceMoved(gameBoard[fromRow][toCol + 1], fromRow, toCol + 1)) {
                            validMove = true;
                        }
                    }
                    // 长易位（后翼）
                    if (colDiff == 2 && toRow == fromRow && targetPiece == EMPTY) {
                        // 检查路径是否清空
                        if (isPathClear(fromRow, fromCol, toRow, toCol + 1) && 
                            gameBoard[fromRow][toCol - 2] != EMPTY && 
                            !hasPieceMoved(gameBoard[fromRow][toCol - 2], fromRow, toCol - 2)) {
                            validMove = true;
                        }
                    }
                }
                break;
                
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                // 马的移动规则：日字形移动
                validMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
                break;
                
            default:
                return false;
        }
        
        // 如果移动是合法的，检查是否处于被将军状态
        // 如果处于被将军状态，必须确保移动能够解除将军
        if (validMove) {
            boolean isWhite = (piece == WHITE_KING) || (piece == WHITE_QUEEN) || (piece == WHITE_ROOK) || 
                             (piece == WHITE_BISHOP) || (piece == WHITE_KNIGHT) || (piece == WHITE_PAWN);
            boolean currentPlayer = (isWhite && whiteTurn) || (!isWhite && !whiteTurn);
            boolean isInCheck = this.isInCheck(currentPlayer);
            
            // 如果当前处于被将军状态，需要检查移动是否能解除将军
            if (isInCheck) {
                // 模拟移动
                int originalPiece = gameBoard[toRow][toCol];
                gameBoard[toRow][toCol] = piece;
                gameBoard[fromRow][fromCol] = EMPTY;
                
                // 保存原始移动状态
                boolean originalWhiteKingMoved = whiteKingMoved;
                boolean originalBlackKingMoved = blackKingMoved;
                boolean originalWhiteKingSideRookMoved = whiteKingSideRookMoved;
                boolean originalWhiteQueenSideRookMoved = whiteQueenSideRookMoved;
                boolean originalBlackKingSideRookMoved = blackKingSideRookMoved;
                boolean originalBlackQueenSideRookMoved = blackQueenSideRookMoved;
                boolean[] originalPawnTwoStepped = pawnTwoStepped.clone();
                
                // 更新移动状态
                if (piece == WHITE_KING) {
                    whiteKingMoved = true;
                } else if (piece == BLACK_KING) {
                    blackKingMoved = true;
                } else if (piece == WHITE_ROOK) {
                    if (fromCol == 0 && fromRow == 0) {
                        whiteQueenSideRookMoved = true;
                    } else if (fromCol == 7 && fromRow == 0) {
                        whiteKingSideRookMoved = true;
                    }
                } else if (piece == BLACK_ROOK) {
                    if (fromCol == 0 && fromRow == 7) {
                        blackQueenSideRookMoved = true;
                    } else if (fromCol == 7 && fromRow == 7) {
                        blackKingSideRookMoved = true;
                    }
                } else if ((piece == WHITE_PAWN || piece == BLACK_PAWN) && Math.abs(toRow - fromRow) == 2) {
                    pawnTwoStepped[toRow * BOARD_WIDTH + toCol] = true;
                }
                
                // 检查是否还在将军
                boolean stillInCheck = this.isInCheck(currentPlayer);
                
                // 恢复移动
                gameBoard[fromRow][fromCol] = piece;
                gameBoard[toRow][toCol] = originalPiece;
                
                // 恢复移动状态
                whiteKingMoved = originalWhiteKingMoved;
                blackKingMoved = originalBlackKingMoved;
                whiteKingSideRookMoved = originalWhiteKingSideRookMoved;
                whiteQueenSideRookMoved = originalWhiteQueenSideRookMoved;
                blackKingSideRookMoved = originalBlackKingSideRookMoved;
                blackQueenSideRookMoved = originalBlackQueenSideRookMoved;
                pawnTwoStepped = originalPawnTwoStepped;
                
                // 如果移动后仍然在将军状态，那么这个移动是不合法的
                return !stillInCheck;
            }
        }
        
        return validMove;
    }
    
    // 检查棋子是否已经移动过
    private boolean hasPieceMoved(int piece, int row, int col) {
        switch (piece) {
            case WHITE_KING:
                return whiteKingMoved;
            case BLACK_KING:
                return blackKingMoved;
            case WHITE_ROOK:
                if (col == 0 && row == 0) {
                    return whiteQueenSideRookMoved;
                } else if (col == 7 && row == 0) {
                    return whiteKingSideRookMoved;
                }
                break;
            case BLACK_ROOK:
                if (col == 0 && row == 7) {
                    return blackQueenSideRookMoved;
                } else if (col == 7 && row == 7) {
                    return blackKingSideRookMoved;
                }
                break;
        }
        return false;
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
    
    // 移动棋子
    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        // 边界检查
        if (fromRow < 0 || fromRow >= BOARD_HEIGHT || fromCol < 0 || fromCol >= BOARD_WIDTH ||
            toRow < 0 || toRow >= BOARD_HEIGHT || toCol < 0 || toCol >= BOARD_WIDTH) {
            return;
        }
        
        int piece = gameBoard[fromRow][fromCol];
        
        // 记录王和车的移动状态
        if (piece == WHITE_KING) {
            whiteKingMoved = true;
        } else if (piece == BLACK_KING) {
            blackKingMoved = true;
        } else if (piece == WHITE_ROOK) {
            if (fromCol == 0 && fromRow == 0) {
                whiteQueenSideRookMoved = true;
            } else if (fromCol == 7 && fromRow == 0) {
                whiteKingSideRookMoved = true;
            }
        } else if (piece == BLACK_ROOK) {
            if (fromCol == 0 && fromRow == 7) {
                blackQueenSideRookMoved = true;
            } else if (fromCol == 7 && fromRow == 7) {
                blackKingSideRookMoved = true;
            }
        }
        
        // 王车异位
        if ((piece == WHITE_KING || piece == BLACK_KING) && Math.abs(toCol - fromCol) == 2) {
            if (toCol > fromCol) {
                // 短易位
                gameBoard[fromRow][toCol - 1] = gameBoard[fromRow][toCol + 1];
                gameBoard[fromRow][toCol + 1] = EMPTY;
            } else {
                // 长易位
                gameBoard[fromRow][toCol + 1] = gameBoard[fromRow][toCol - 2];
                gameBoard[fromRow][toCol - 2] = EMPTY;
            }
        }
        
        // 吃过路兵
        if ((piece == WHITE_PAWN || piece == BLACK_PAWN) && Math.abs(toCol - fromCol) == 1 && gameBoard[toRow][toCol] == EMPTY) {
            int direction = (piece == WHITE_PAWN) ? 1 : -1;
            int capturedPawnRow = toRow - direction;
            gameBoard[capturedPawnRow][toCol] = EMPTY;
        }
        
        // 移动棋子
        gameBoard[toRow][toCol] = piece;
        gameBoard[fromRow][fromCol] = EMPTY;
        
        // 记录兵的两步移动
        if ((piece == WHITE_PAWN || piece == BLACK_PAWN) && Math.abs(toRow - fromRow) == 2) {
            pawnTwoStepped[toRow * BOARD_WIDTH + toCol] = true;
        }
        
        // 兵升变
        if ((piece == WHITE_PAWN && toRow == BOARD_HEIGHT - 1) || (piece == BLACK_PAWN && toRow == 0)) {
            // 显示升变选择窗口
            WndPromotion promotionWindow = new WndPromotion(piece, toRow, toCol);
            addToFront(promotionWindow);
        } else {
            // 不是兵升变情况，直接完成移动
            completeMove();
            
            // 切换回合
            whiteTurn = !whiteTurn;
            
            // AI移动
            if (!whiteTurn && aiEnabled) {
                aiMove();
            }
        }
    }
    
    // 完成移动后的处理
    private void completeMove() {
        // 清除所有吃过路兵的标记
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            pawnTwoStepped[i] = false;
        }
        
        updateChessButtons();
        
        // 检查是否将军或将杀
        if (isInCheck(!whiteTurn)) {
            if (isCheckmate(!whiteTurn)) {
                GLog.p(whiteTurn ? "黑方被将杀！白方获胜！" : "白方被将杀！黑方获胜！");
            } else {
                GLog.p(whiteTurn ? "黑方被将军！" : "白方被将军！");
            }
        }
    }
    
    // 兵升变选择窗口
    private class WndPromotion extends Window {
        
        private static final int WIDTH = 120;
        private static final int BTN_HEIGHT = 30;
        private static final float GAP = 2;
        private int pawnType;
        private int row;
        private int col;
        
        public WndPromotion(int pawnType, int row, int col) {
            super();
            
            this.pawnType = pawnType;
            this.row = row;
            this.col = col;
            
            boolean isWhite = (pawnType == WHITE_PAWN);
            
            // 设置窗口大小
            resize(WIDTH, (int)(BTN_HEIGHT * 4 + GAP * 3));
            
            // 升变选择按钮
            int[] promotionPieces = isWhite ? 
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
                        completeMove();
                        hide();
                        
                        // 切换回合
                        whiteTurn = !whiteTurn;
                        
                        // AI移动
                        if (!whiteTurn && aiEnabled) {
                            aiMove();
                        }
                    }
                };
                
                btn.setSize(WIDTH, BTN_HEIGHT);
                btn.setPos(0, i * (BTN_HEIGHT + GAP));
                add(btn);
            }
            
            // 窗口会自动居中显示，不需要手动设置位置
        }
        
        @Override
        public void onBackPressed() {
            // 当用户点击窗口外或按返回键时，默认升变为后
            boolean isWhite = (pawnType == WHITE_PAWN);
            int queenPiece = isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            
            // 执行升变
            gameBoard[row][col] = queenPiece;
            completeMove();
            hide();
            
            // 切换回合
            whiteTurn = !whiteTurn;
            
            // AI移动
            if (!whiteTurn && aiEnabled) {
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
                    // 创建棋子图像（使用简单的图标表示）
                    Image pieceImg = createPieceImage(pieceType);
                    
                    // 设置按钮图标
                    button.icon(pieceImg);
                    
                    // 启用按钮
                    button.enable(true);
                } else {
                    // 清除图标
                    button.icon(null);
                    // 启用按钮
                    button.enable(true);
                }
            }
        }
    }
    
    // AI移动方法
    private void aiMove() {
        if (!aiEnabled || whiteTurn) {
            return;
        }
        
        aiThinking = true;
        
        // 简单的AI实现：随机选择一个合法移动
        ArrayList<int[]> validMoves = new ArrayList<>();
        
        // 收集所有合法移动
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                int piece = gameBoard[row][col];
                if (!isPlayerPiece(piece)) {
                    continue;
                }
                
                for (int toRow = 0; toRow < BOARD_HEIGHT; toRow++) {
                    for (int toCol = 0; toCol < BOARD_WIDTH; toCol++) {
                        if (isValidMove(row, col, toRow, toCol)) {
                            validMoves.add(new int[]{row, col, toRow, toCol});
                        }
                    }
                }
            }
        }
        
        // 随机选择一个移动
        if (!validMoves.isEmpty()) {
            int[] move = validMoves.get(Random.Int(validMoves.size()));
            movePiece(move[0], move[1], move[2], move[3]);
            whiteTurn = true;
        }
        
        aiThinking = false;
    }
    
    // 创建棋子图像
    private Image createPieceImage(int pieceType) {
        // 加载国际象棋贴图
        Image chessSheet = new Image(Assets.Environment.CHESS);
        
        // 每个格子大小为24*24
        final int TILE_SIZE = 24;
        
        // 定义不同棋子在贴图中的位置
        int row, col;
        
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
            
            default:
                return new Image(Icons.get(Icons.TARGET));
        }
        
        // 创建一个新图像，仅显示贴图中的指定部分
        Image pieceImg = new Image(chessSheet);
        
        // 设置图像显示的区域（从贴图中裁剪出指定棋子）
        pieceImg.frame(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        
        // 设置图像大小（使用动态调整的单元格大小）
        float scale = (adjustedCellSize - 8) / pieceImg.width;
        pieceImg.scale.set(scale);
        
        return pieceImg;
    }
    
    // 更新游戏板显示
    private void updateBoardVisual() {
        if (boardContainer != null) {
            updateChessButtons();
        }
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
            (uiCamera.width - buttonWidth * 2 - margin) / 2,
            uiCamera.height - buttonHeight - margin
        );
        restartButton.camera = uiCamera;
        add(restartButton);
        
        // 退出按钮
        exitButton = new StyledButton(Chrome.Type.TOAST_TR, "退出") {
            @Override
            protected void onClick() {
                onBackPressed();
            }
        };
        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.setPos(
            restartButton.right() + margin,
            uiCamera.height - buttonHeight - margin
        );
        exitButton.camera = uiCamera;
        add(exitButton);
        
        // AI开关按钮
        aiButton = new StyledButton(Chrome.Type.TOAST_TR, aiEnabled ? "AI开启" : "AI关闭") {
            @Override
            protected void onClick() {
                aiEnabled = !aiEnabled;
                this.text(aiEnabled ? "AI开启" : "AI关闭");
            }
        };
        aiButton.setSize(buttonWidth, buttonHeight);
        aiButton.setPos(
            exitButton.right() + margin,
            uiCamera.height - buttonHeight - margin
        );
        aiButton.camera = uiCamera;
        add(aiButton);
    }
    
    // 创建选择指示器
    private void createSelectionIndicator() {
        // 使用一个简单的Image作为选择指示器
        selectionIndicator = new Image(Icons.get(Icons.TARGET));
        selectionIndicator.hardlight(0xFFFFFF); // 设置颜色为白色
        selectionIndicator.visible = false;
        selectionIndicator.camera = uiCamera;
        
        // 设置选择指示器的大小，使其适合单元格
        float scale = CELL_SIZE / selectionIndicator.width();
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
        float cellX = boardContainer.x + selectedCol * adjustedCellSize;
        float cellY = boardContainer.y + selectedRow * adjustedCellSize;
        
        // 计算缩放后的选择指示器尺寸
        float scaledWidth = selectionIndicator.width() * selectionIndicator.scale.x;
        float scaledHeight = selectionIndicator.height() * selectionIndicator.scale.y;
        
        // 设置选择指示器的位置，使其居中于选中的单元格
        selectionIndicator.setPos(
            cellX + (adjustedCellSize - scaledWidth) / 2,
            cellY + (adjustedCellSize - scaledHeight) / 2
        );
        selectionIndicator.visible = true;
    }
    
    // 清除选择
    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        selectionIndicator.visible = false;
    }
    
    // 重启游戏
    private void restartGame() {
        // 重置游戏状态
        clearSelection();
        
        // 重新初始化游戏
        initGame();
        
        // 更新游戏板显示
        updateBoardVisual();
        
        gameRunning = true;
        whiteTurn = true;
    }
    
    // 处理返回键
    @Override
    protected void onBackPressed() {
        // 返回上一个场景
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene(InterlevelScene.class);
    }
}