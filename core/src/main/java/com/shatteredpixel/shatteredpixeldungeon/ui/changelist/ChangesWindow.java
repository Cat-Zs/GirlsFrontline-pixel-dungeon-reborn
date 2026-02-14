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

import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Image;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;

import java.util.Objects;

public class ChangesWindow extends WndTitledMessage {
    public ChangesWindow(Image icon, float size, MovieClip.Animation action, Boolean cycle, int color , String title, String message) {
        this(titleBar(icon, size, action, cycle, color, title), message);
    }
    public ChangesWindow(Component title, String message ) {
        super( title, message==null?"":message);

        PointerArea blocker = new PointerArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
            @Override
            protected void onClick( PointerEvent event ) {
                onBackPressed();
            }
        };
        blocker.camera = PixelScene.uiCamera;
        add(blocker);

    }
    private static Component titleBar(Image icon, float size, MovieClip.Animation action, Boolean cycle, int color, String title ){
        if (title == null)
            title = "";
        if (icon instanceof CharSprite)
            return new ChangeButton.MobTitle((CharSprite) icon, title, size, action, color, cycle);
        else
            return new IconTitle(new Image(icon), title);
    }
}
