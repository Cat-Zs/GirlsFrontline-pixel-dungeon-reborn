package com.shatteredpixel.shatteredpixeldungeon.scenes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.StyledButton;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchThreeScene extends PixelScene {
    {
        inGameScene = true;
    }

    // 游戏常量定义
    private static final int BOARD_WIDTH = 12;
    private static final int BOARD_HEIGHT = 12;
    private static final int EMPTY = 0;
    private static final int BORDER = 1;
    private static final int CELL_SIZE = 24;
    private static final int PLANT_IMAGE_SIZE = 20;//CELL_SIZE - 4
    private static final float SIZE_BUTTON_TO_IMAGE = PLANT_IMAGE_SIZE / (float)CELL_SIZE;
    
    // 植物类型（使用ItemSpriteSheet中的SEEDS常量）
    private static final int[] PLANT_TYPES = {
        ItemSpriteSheet.SEED_ROTBERRY,
        ItemSpriteSheet.SEED_FIREBLOOM,
        ItemSpriteSheet.SEED_SWIFTTHISTLE,
        ItemSpriteSheet.SEED_SUNGRASS,
        ItemSpriteSheet.SEED_ICECAP
    };
    
    // 游戏状态变量
    private int[][] gameBoard;
    private boolean gameRunning;
    private int score; // 计分板变量
    private RenderedTextBlock scoreText; // 分数显示组件
    
    // 选择状态
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Image selectionIndicator;
    
    // 渲染组件
    private Image background;
    private float adjustedCellSize;
    private StyledButton[][] plantButtons;
    private StyledButton restartButton;
    private StyledButton exitButton;
    
    @Override
    public void create() {
        super.create();
        
        // 初始化游戏
        initGame();
        // 初始化游戏状态
        gameRunning = true;

        // 设置背景 - 确保最先添加，在最底层
        setupBackground();
        // 创建植物按钮组件
        createPlantButtons();
        // 添加选择指示器
        createSelectionIndicator();
        // 添加分数显示
        createScoreDisplay();
        // 添加控制按钮 - 确保最后添加，在最上层
        addControlButtons();

        // 更新按钮状态(初次渲染)
        updatePlantButtons();
        
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
        
        // 确保背景在最底层
        add(background);
    }
    
    // 初始化游戏
    private void initGame() {
        // 创建游戏板
        gameBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        
        // 初始化分数
        score = 0;
        updateScoreDisplay();
        
        // 填充游戏板
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                // 设置边框
                if (row == 0 || row == BOARD_HEIGHT - 1 || col == 0 || col == BOARD_WIDTH - 1) {
                    gameBoard[row][col] = BORDER;
                } else {
                    // 填充随机植物，确保没有初始匹配
                    do {
                        gameBoard[row][col] = getRandomPlant();
                    } while (hasInitialMatch(row, col));
                }
            }
        }
    }
    
    // 检查初始匹配
    private boolean hasInitialMatch(int row, int col) {
        int plant = gameBoard[row][col];
        
        // 检查水平匹配
        if (col >= 2 && gameBoard[row][col-1] == plant && gameBoard[row][col-2] == plant) {
            return true;
        }
        
        // 检查垂直匹配
        if (row >= 2 && gameBoard[row-1][col] == plant && gameBoard[row-2][col] == plant) {
            return true;
        }
        
        return false;
    }
    
    // 获取随机植物类型
    private int getRandomPlant() {
        return PLANT_TYPES[Random.Int(PLANT_TYPES.length)];
    }
    
    // 创建游戏板视觉组件
    private void createPlantButtons() {
        // 计算合适的单元格大小，使游戏板能够适应屏幕大小
        float availableWidth = uiCamera.width * 0.9f; // 留出边距
        float availableHeight = uiCamera.height * 0.9f; // 留出边距
        
        // 计算基于宽度和高度的最大单元格大小
        float maxCellSizeWidth = availableWidth / BOARD_WIDTH;
        float maxCellSizeHeight = availableHeight / BOARD_HEIGHT;
        
        // 选择较小的那个作为实际的单元格大小
        adjustedCellSize = Math.min(maxCellSizeWidth, maxCellSizeHeight);
        
        // 创建植物按钮数组
        plantButtons = new StyledButton[BOARD_HEIGHT][BOARD_WIDTH];
        
        // 创建每个植物按钮
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                final int finalRow = row;
                final int finalCol = col;
                
                // 创建植物按钮
                plantButtons[row][col] = new StyledButton(Chrome.Type.GREY_BUTTON, "") {
                    @Override
                    protected void onClick() {
                        handlePlantClick(finalRow, finalCol);
                    }
                };
                
                // 设置按钮大小和位置
                plantButtons[row][col].setSize(adjustedCellSize, adjustedCellSize);
                plantButtons[row][col].setPos(col * adjustedCellSize, row * adjustedCellSize);
                plantButtons[row][col].camera = uiCamera;
                
                // 添加按钮到容器
                add(plantButtons[row][col]);
                
                // 设置按钮背景为透明
                plantButtons[row][col].text("");
            }
        }
    }
    
    // 处理植物按钮点击
    private void handlePlantClick(int row, int col) {
        if (!gameRunning) return;
        
        // 检查点击位置是否在有效区域内（排除边框）
        if (row < 1 || row >= BOARD_HEIGHT - 1 || col < 1 || col >= BOARD_WIDTH - 1) {
            return;
        }
        
        if (selectedRow == -1 && selectedCol == -1) {
            // 第一次选择
            selectedRow = row;
            selectedCol = col;
            updateSelectionIndicator();
        } else {
            // 第二次选择，检查是否相邻
            if (isAdjacent(selectedRow, selectedCol, row, col)) {
                // 交换植物
                swapPlants(selectedRow, selectedCol, row, col);
                
                // 检查是否有匹配
                List<int[]> matches = findMatches();
                if (!matches.isEmpty()) {
                    // 处理匹配
                    processMatches(matches);
                } else {
                    // 没有匹配，交换回来
                    swapPlants(row, col, selectedRow, selectedCol);
                }
                
                // 清除选择
                clearSelection();
            } else {
                // 选择新的单元格
                selectedRow = row;
                selectedCol = col;
                updateSelectionIndicator();
            }
        }
    }
    
    // 检查两个单元格是否相邻
    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        int rowDiff = Math.abs(row1 - row2);
        int colDiff = Math.abs(col1 - col2);
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
    
    // 更新植物按钮状态
    private void updatePlantButtons() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                StyledButton button = plantButtons[row][col];
                int plantType = gameBoard[row][col];
                
                // 设置按钮内容
                if (plantType != EMPTY && plantType != BORDER) {
                    // 创建植物图像
                    Image plantImg = new Image(Assets.Sprites.ITEMS);
                    plantImg.frame(ItemSpriteSheet.film.get(plantType));
                    
                    // 设置图像大小
                    float scale = SIZE_BUTTON_TO_IMAGE*adjustedCellSize / plantImg.width;
                    plantImg.scale.set(scale);
                    
                    // 设置按钮图标
                    button.icon(plantImg);
                    
                    // 启用按钮
                    button.enable(true);
                } else {
                    // 清除图标
                    button.icon(null);
                    // 禁用按钮
                    button.enable(false);
                }
            }
        }
    }
    
    // 交换两个植物
    private void swapPlants(int row1, int col1, int row2, int col2) {
        int temp = gameBoard[row1][col1];
        gameBoard[row1][col1] = gameBoard[row2][col2];
        gameBoard[row2][col2] = temp;
        updatePlantButtons();
    }
    
    // 查找所有匹配
    private List<int[]> findMatches() {
        List<int[]> matches = new ArrayList<>();
        boolean[][] visited = new boolean[BOARD_HEIGHT][BOARD_WIDTH];
        
        // 检查水平匹配
        for (int row = 1; row < BOARD_HEIGHT - 1; row++) {
            int count = 1;
            for (int col = 2; col < BOARD_WIDTH - 1; col++) {
                if (gameBoard[row][col] == gameBoard[row][col-1] && gameBoard[row][col] != EMPTY) {
                    count++;
                } else {
                    if (count >= 3) {
                        for (int c = col - count; c < col; c++) {
                            if (!visited[row][c]) {
                                visited[row][c] = true;
                                matches.add(new int[]{row, c});
                            }
                        }
                    }
                    count = 1;
                }
            }
            // 检查行尾的匹配
            if (count >= 3) {
                for (int c = BOARD_WIDTH - 1 - count; c < BOARD_WIDTH - 1; c++) {
                    if (!visited[row][c]) {
                        visited[row][c] = true;
                        matches.add(new int[]{row, c});
                    }
                }
            }
        }
        
        // 检查垂直匹配
        for (int col = 1; col < BOARD_WIDTH - 1; col++) {
            int count = 1;
            for (int row = 2; row < BOARD_HEIGHT - 1; row++) {
                if (gameBoard[row][col] == gameBoard[row-1][col] && gameBoard[row][col] != EMPTY) {
                    count++;
                } else {
                    if (count >= 3) {
                        for (int r = row - count; r < row; r++) {
                            if (!visited[r][col]) {
                                visited[r][col] = true;
                                matches.add(new int[]{r, col});
                            }
                        }
                    }
                    count = 1;
                }
            }
            // 检查列尾的匹配
            if (count >= 3) {
                for (int r = BOARD_HEIGHT - 1 - count; r < BOARD_HEIGHT - 1; r++) {
                    if (!visited[r][col]) {
                        visited[r][col] = true;
                        matches.add(new int[]{r, col});
                    }
                }
            }
        }
        
        return matches;
    }
    
    // 处理匹配
    private void processMatches(List<int[]> matches) {
        // 为每个匹配的植物增加分数
        score += matches.size() * 1;
        updateScoreDisplay();
        
        // 移除匹配的植物
        for (int[] match : matches) {
            gameBoard[match[0]][match[1]] = EMPTY;
        }
        
        // 植物下落
        dropPlants();
        
        // 生成新植物
        generateNewPlants();
        
        // 更新显示
        updatePlantButtons();
        
        // 检查是否有新的匹配
        List<int[]> newMatches = findMatches();
        if (!newMatches.isEmpty()) {
            processMatches(newMatches);
        }
    }
    
    // 植物下落
    private void dropPlants() {
        for (int col = 1; col < BOARD_WIDTH - 1; col++) {
            int emptyRow = BOARD_HEIGHT - 2;
            
            // 从下往上处理每一列
            for (int row = BOARD_HEIGHT - 2; row >= 1; row--) {
                if (gameBoard[row][col] != EMPTY) {
                    if (emptyRow != row) {
                        gameBoard[emptyRow][col] = gameBoard[row][col];
                        gameBoard[row][col] = EMPTY;
                    }
                    emptyRow--;
                }
            }
        }
    }
    
    // 生成新植物
    private void generateNewPlants() {
        for (int col = 1; col < BOARD_WIDTH - 1; col++) {
            for (int row = 1; row < BOARD_HEIGHT - 1; row++) {
                if (gameBoard[row][col] == EMPTY) {
                    gameBoard[row][col] = getRandomPlant();
                }
            }
        }
    }
    
    // 创建分数显示
    private void createScoreDisplay() {
        scoreText = PixelScene.renderTextBlock(9);
        scoreText.text("分数: 0");
        scoreText.setPos(PixelScene.align((uiCamera.width - scoreText.width()) / 2), 20);
        scoreText.hardlight(0xFFFFFF);
        scoreText.camera = uiCamera;
        add(scoreText);
    }
    
    // 更新分数显示
    private void updateScoreDisplay() {
        if (scoreText != null) {
            scoreText.text("分数: " + score);
            scoreText.setPos(PixelScene.align((uiCamera.width - scoreText.width()) / 2), 20);
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
        // 使用一个简单的Image作为选择指示器，而不是临时的TargetedCell
        selectionIndicator = new Image(Icons.get(Icons.TARGET));
        selectionIndicator.hardlight(0xFFFFFF); // 设置颜色为白色
        selectionIndicator.visible = false;
        selectionIndicator.camera = uiCamera;
        
        // 设置选择指示器的大小，使其完全覆盖单元格
        float scale = adjustedCellSize / selectionIndicator.width;
        selectionIndicator.scale.set(scale);
        
        add(selectionIndicator);
    }
    
    // 更新选择指示器位置
    private void updateSelectionIndicator() {
        if (selectedRow == -1 || selectedCol == -1) {
            selectionIndicator.visible = false;
            return;
        }
        
        // 设置选择指示器的位置，使其与选中的单元格精确对齐
        float x = selectedCol * adjustedCellSize;
        float y = selectedRow * adjustedCellSize;
        
        // 使用PixelScene.align确保像素对齐
        selectionIndicator.setPos(PixelScene.align(x), PixelScene.align(y));
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
        
        // 更新显示
        updatePlantButtons();
        
        gameRunning = true;
    }
    
    // 处理返回键
    @Override
    protected void onBackPressed() {
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene(InterlevelScene.class);
    }
}