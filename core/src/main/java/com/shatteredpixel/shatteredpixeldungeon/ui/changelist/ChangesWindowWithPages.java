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

package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class ChangesWindowWithPages extends Window {

    private ArrayList<Page> pages = new ArrayList<>();
    private int currentPage = 0;
    
    private Image icon;
    private String title;
    private RenderedTextBlock contentText;
    private RenderedTextBlock titleText;
    private RenderedTextBlock pageNumberText; // 页面数字显示
    
    private ArrayList<ColorBlock> pageIndicators = new ArrayList<>();
    
    public ChangesWindowWithPages(Image icon, String title, ArrayList<String> pageContents) {
        super();
        
        this.icon = icon;
        this.title = title;
        
        // 创建页面
        for (int i = 0; i < pageContents.size(); i++) {
            pages.add(new Page(i + 1, pageContents.get(i)));
        }
        
        // 创建标题文本
        titleText = PixelScene.renderTextBlock(title, 9);
        titleText.hardlight(Window.TITLE_COLOR);
        add(titleText);
        
        // 创建内容文本
        contentText = PixelScene.renderTextBlock(pageContents.get(0), 6);
        add(contentText);
        
        // 创建页面数字显示
        pageNumberText = PixelScene.renderTextBlock("1/" + pages.size(), 8);
        pageNumberText.hardlight(Window.TITLE_COLOR);
        add(pageNumberText);
        
        // 如果有图标，添加到窗口
        if (icon != null) {
            add(icon);
        }
        
        // 创建页面指示器
        for (int i = 0; i < pages.size(); i++) {
            ColorBlock indicator = new ColorBlock(6, 6, Window.TITLE_COLOR);
            indicator.alpha(i == currentPage ? 1f : 0.5f);
            pageIndicators.add(indicator);
            add(indicator);
        }
        
        // 创建翻页按钮（缩小尺寸）
        RedButton btnPrev = new RedButton("<") {
            @Override
            protected void onClick() {
                previousPage();
            }
        };
        btnPrev.setSize(10, 10); // 调整界面尺寸的位置
        add(btnPrev);
        
        RedButton btnNext = new RedButton(">") {
            @Override
            protected void onClick() {
                nextPage();
            }
        };
        btnNext.setSize(10, 10); // 缩小按钮尺寸
        add(btnNext);
        
        layout();
    }
    
    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateContent();
        }
    }
    
    private void nextPage() {
        if (currentPage < pages.size() - 1) {
            currentPage++;
            updateContent();
        }
    }
    
    private void updateContent() {
        contentText.text(pages.get(currentPage).content);
        
        // 更新页面数字显示
        pageNumberText.text((currentPage + 1) + "/" + pages.size());
        
        // 更新页面指示器
        for (int i = 0; i < pageIndicators.size(); i++) {
            pageIndicators.get(i).alpha(i == currentPage ? 1f : 0.5f);
        }
        
        layout();
    }
    
    public void onBackPressed() {
        hide(); // 使用父类的hide()方法来正确关闭窗口
    }
    
    // 不再需要@Override注解和super.layout()调用
    protected void layout() {
        // 设置内容文本最大宽度
        contentText.maxWidth((int)(PixelScene.uiCamera.width - 120));
        
        // 计算窗口大小
        float windowWidth = contentText.width() + 40;
        float windowHeight = 100; // 基础高度
        
        // 计算内容总高度
        float contentHeight = 0;
        contentHeight += titleText.height() + 10; // 标题
        if (icon != null) {
            contentHeight += icon.height + 10; // 图标
        }
        contentHeight += contentText.height() + 15; // 内容
        contentHeight += 20; // 页面指示器和页码
        
        windowHeight = Math.max(windowHeight, contentHeight + 20); // 加上边距
        
        // 调整窗口大小
        resize((int)windowWidth, (int)windowHeight);
        
        // 设置图标位置（左上角）
        if (icon != null) {
            icon.setPos(10, 10);
        }
        
        // 设置标题位置（图标右侧或居中）
        if (icon != null) {
            titleText.setPos(
                icon.x + icon.width + 10,
                10
            );
        } else {
            titleText.setPos(
                (width - titleText.width()) / 2f,
                10
            );
        }
        
        // 设置内容文本位置
        float contentY = icon != null ? (icon.y + icon.height) + 10 : titleText.bottom() + 15;
        contentText.setPos(
            (width - contentText.width()) / 2f,
            contentY
        );
        
        // 计算底部控制区域的位置（固定居中）
        float controlY = windowHeight - 20; // 固定在底部上方20px处
        
        // 计算各个底部元素的总宽度
        float indicatorsWidth = (pageIndicators.size() * 12) - 6; // 每个指示器间距12px
        float totalControlsWidth = 20 + indicatorsWidth + 10 + pageNumberText.width() + 20; // 左右边距 + 指示器 + 间距 + 页码 + 右边距
        
        // 计算控制区域的起始X坐标（居中）
        float controlsStartX = (windowWidth - totalControlsWidth) / 2f;
        
        // 设置翻页按钮位置（固定）
        RedButton btnPrev = (RedButton)members.get(members.size() - 2);
        RedButton btnNext = (RedButton)members.get(members.size() - 1);
        
        btnPrev.setPos(
            controlsStartX,
            controlY
        );
        
        btnNext.setPos(
            controlsStartX + totalControlsWidth - 10,
            controlY
        );
        
        // 设置页面指示器位置（固定）
        float indicatorX = controlsStartX + 20; // 左边距后开始
        for (int i = 0; i < pageIndicators.size(); i++) {
            ColorBlock indicator = pageIndicators.get(i);
            indicator.setPos(indicatorX + (i * 12), controlY + 1); // 调整垂直位置使其与文字对齐
        }
        
        // 设置页码文本位置（固定）
        pageNumberText.setPos(
            indicatorX + indicatorsWidth + 10,
            controlY
        );
    }
    
    // 页面类
    private class Page {
        int number;
        String content;
        
        public Page(int number, String content) {
            this.number = number;
            this.content = content;
        }
    }
}