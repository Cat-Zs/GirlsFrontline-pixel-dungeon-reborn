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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elphelt;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.debug;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ElpheltSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class debugSelectCodeB extends Window {

	private static final int WIDTH		= 130;
	private static final float GAP		= 2;
    private ArrayList<RedButton> buttons = new ArrayList<>();

	public debugSelectCodeB(final debug debugA, final ArrayList<Integer> list){

		super();
        if (debugA == null)
            return;

		//crown can be null if hero is choosing from armor, pre-0.9.3 saves
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( debugA.image(), null ) );
		titlebar.label( debugA.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock body = PixelScene.renderTextBlock( 7 );
        body.text(debug.getInfo(list)+"\n\n"+Messages.get(this, "message"), WIDTH);
		body.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( body );

		float pos = body.bottom() + 3*GAP;
		for (int i :list) {
			RedButton SelectTalent = new RedButton(debug.shortDesc(list, i), 7){
				@Override
				protected void onClick() {
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(
                                    new WndOptions(
                                            Messages.titleCase(debug.shortDesc(list)),
                                            debug.LongDesc(list, i),
                                            Messages.get(this, "yes"),
                                            Messages.get(this, "no"))
                                    {
                                        @Override
                                        protected void onSelect(int index) {
                                            if (index == 0){
                                                debug.setCode(list, i);
                                                hide();
                                            }
                                        }
                                    }
                            );
                        }
                    });
				}
			};
			SelectTalent.leftJustify = true;
			SelectTalent.multiline = true;
			SelectTalent.setSize(WIDTH, SelectTalent.reqHeight()+2);
			SelectTalent.setRect(0, pos, WIDTH, SelectTalent.reqHeight()+2);
			add(SelectTalent);
            buttons.add(SelectTalent);
            pos = SelectTalent.bottom() + GAP;
		}

		RedButton cancelButton = new RedButton(Messages.get(this, "cancel")){
			@Override
			protected void onClick() {
				hide();
			}
		};
		cancelButton.setRect(0, pos, WIDTH, 18);
		add(cancelButton);
		pos = cancelButton.bottom() + GAP;

		resize(WIDTH, (int)pos);

	}
    @Override
    public void hide(){
        for (RedButton b: buttons){
            b.destroy();
        }
        super.hide();
    }
}
