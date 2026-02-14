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

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.PointerArea;

import java.util.ArrayList;

public class ChangesWindowWithPages extends ChangesWindow {
    public ChangesWindowWithPages(ArrayList<Image> icon, ArrayList<Float> size,
                                  ArrayList<MovieClip.Animation> action, ArrayList<Boolean> cycle,
                                  ArrayList<Integer> color, ArrayList<String> title,
                                  ArrayList<String> pageMessage, int page) {
        super(icon.get(page), size.get(page),
                action!=null?action.get(page):null,
                cycle!=null?cycle.get(page):null,
                color.get(page),
                title.get(page), pageMessage.get(page));
        layout(page, pageMessage);
        //翻页相关图标

        // 为翻页按钮创建更大的点击区域（3倍大小）
        // 为上一页按钮创建更大的点击区域（再扩大2倍）
        PointerArea prevHotArea = new PointerArea(-55, height - 65, 120, 120) {
            @Override
            protected void onClick(PointerEvent event) {
                if (page > 0) {
                    onBackPressed();
                    GirlsFrontlinePixelDungeon.scene().add(new ChangesWindowWithPages(icon, size, action, cycle, color, title, pageMessage, page - 1));
                }
            }
        };
        add(prevHotArea);

        // 为下一页按钮创建更大的点击区域
        PointerArea nextHotArea = new PointerArea(width - 65, height - 65, 120, 120) {
            @Override
            protected void onClick(PointerEvent event) {
                if (page < pageMessage.size() - 1) {
                    onBackPressed();
                    GirlsFrontlinePixelDungeon.scene().add(new ChangesWindowWithPages(icon, size, action, cycle, color, title, pageMessage, page + 1));
                }
            }
        };
        add(nextHotArea);
    }
    private void layout(int page, ArrayList<String> pageMessage){
        //给窗口增加高度以放置按钮而不遮挡原来的文本
        height += 10;
        resize(width, height);
        RenderedTextBlock pageNumberText; // 页面数字显示
        // 创建页面数字显示
        pageNumberText = PixelScene.renderTextBlock(page + 1 + "/" + pageMessage.size(), 8);
        pageNumberText.hardlight(Window.TITLE_COLOR);
        // 设置页码文本位置（与翻页按钮在同一水平线上）
        pageNumberText.setPos((float) width / 2, // 页码显示在翻页按钮右侧
                height - 10 // 与翻页按钮在同一水平线上
        );
        add(pageNumberText);

        // 创建翻页按钮（保持视觉大小不变）
        RedButton btnPrev = new RedButton("<");
        // 设置按钮视觉大小
        btnPrev.setSize(10, 10);
        // 设置按钮位置
        btnPrev.setPos(0, height - 10);
        add(btnPrev);

        // 创建翻页按钮（保持视觉大小不变）
        RedButton btnNext = new RedButton(">");
        // 设置按钮视觉大小
        btnNext.setSize(10, 10);
        // 设置按钮位置
        btnNext.setPos(width - 10, height - 10);
        add(btnNext);

    }
}