package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
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
                    
                    // 切换回合
                    whiteTurn = !whiteTurn;
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
    
    // 检查移动是否有效（简化版，仅实现基本移动规则）
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        int piece = gameBoard[fromRow][fromCol];
        int targetPiece = gameBoard[toRow][toCol];
        
        // 不能移动到自己的棋子上
        if (isPlayerPiece(targetPiece)) {
            return false;
        }
        
        // 计算移动距离
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        
        switch (piece) {
            case WHITE_PAWN:
            case BLACK_PAWN:
                // 简化的兵移动规则
                int direction = (piece == WHITE_PAWN) ? 1 : -1;
                
                // 向前移动一格
                if (colDiff == 0 && toRow == fromRow + direction && targetPiece == EMPTY) {
                    return true;
                }
                
                // 第一步可以向前移动两格
                if (colDiff == 0 && 
                    ((piece == WHITE_PAWN && fromRow == 1) || (piece == BLACK_PAWN && fromRow == 6)) && 
                    toRow == fromRow + 2 * direction && 
                    gameBoard[fromRow + direction][fromCol] == EMPTY && 
                    targetPiece == EMPTY) {
                    return true;
                }
                
                // 吃子（斜向移动）
                if (rowDiff == 1 && colDiff == 1 && isOpponentPiece(targetPiece)) {
                    return true;
                }
                
                return false;
                
            case WHITE_ROOK:
            case BLACK_ROOK:
                // 车的移动规则：直线移动，不能有棋子阻挡
                if (rowDiff == 0 || colDiff == 0) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                return false;
                
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                // 象的移动规则：对角线移动，不能有棋子阻挡
                if (rowDiff == colDiff) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                return false;
                
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                // 后的移动规则：直线或对角线移动，不能有棋子阻挡
                if (rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) {
                    return isPathClear(fromRow, fromCol, toRow, toCol);
                }
                return false;
                
            case WHITE_KING:
            case BLACK_KING:
                // 王的移动规则：一格范围内的任何方向
                return rowDiff <= 1 && colDiff <= 1;
                
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                // 马的移动规则：日字形移动
                return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
                
            default:
                return false;
        }
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
        gameBoard[toRow][toCol] = gameBoard[fromRow][fromCol];
        gameBoard[fromRow][fromCol] = EMPTY;
        updateChessButtons();
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
    
    // 创建棋子图像
    private Image createPieceImage(int pieceType) {
        Image img = new Image(Icons.get(Icons.TARGET));
        
        switch (pieceType) {
            case WHITE_KING:
            case BLACK_KING:
                img.hardlight(0xFF0000); // 红色表示王
                break;
            case WHITE_QUEEN:
            case BLACK_QUEEN:
                img.hardlight(0x0000FF); // 蓝色表示后
                break;
            case WHITE_ROOK:
            case BLACK_ROOK:
                img.hardlight(0x00FF00); // 绿色表示车
                break;
            case WHITE_BISHOP:
            case BLACK_BISHOP:
                img.hardlight(0xFFFF00); // 黄色表示象
                break;
            case WHITE_KNIGHT:
            case BLACK_KNIGHT:
                img.hardlight(0xFF00FF); // 紫色表示马
                break;
            case WHITE_PAWN:
            case BLACK_PAWN:
                img.hardlight(0xFFFFFF); // 白色表示兵
                break;
        }
        
        // 设置图像大小（使用动态调整的单元格大小）
        float scale = (adjustedCellSize - 8) / img.width;
        img.scale.set(scale);
        
        return img;
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
        // 保存游戏进度
        try {
            Dungeon.saveAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 返回上一个场景
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene(InterlevelScene.class);
    }
    
    @Override
    public void destroy() {
        // 清理资源
        if (selectionIndicator != null) {
            selectionIndicator.killAndErase();
        }
        
        if (boardContainer != null) {
            boardContainer.killAndErase();
        }
        
        if (restartButton != null) {
            restartButton.killAndErase();
        }
        
        if (exitButton != null) {
            exitButton.killAndErase();
        }
        
        if (background != null) {
            background.killAndErase();
        }
        
        super.destroy();
    }
}