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

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.TierOfTalent;
import com.shatteredpixel.shatteredpixeldungeon.custom.testmode.generator.debug;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfMetamorphosis;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentsPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class debugSelectTalent extends Window {

	private static final int WIDTH		= 130;
	private static final float GAP		= 2;

	public debugSelectTalent(final debug debugA, final ArrayList<ArrayList<Talent>> list){

		super();
        if (debugA == null)
            return;

		//crown can be null if hero is choosing from armor, pre-0.9.3 saves
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( debugA.image(), null ) );
		titlebar.label( debugA.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock body = PixelScene.renderTextBlock( 6 );
        body.text(Messages.get(this, "message"), WIDTH);
		body.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( body );

		float pos = body.bottom() + 3*GAP;
		for (ArrayList<Talent> listb :list) {

			RedButton SelectTalent = new RedButton(debug.shortDesc(listb), 6){
				@Override
				protected void onClick() {
                    GameScene.show(new debugTalent(listb));
				}
			};
			SelectTalent.leftJustify = true;
			SelectTalent.multiline = true;
			SelectTalent.setSize(WIDTH, SelectTalent.reqHeight()+2);
			SelectTalent.setRect(0, pos, WIDTH, SelectTalent.reqHeight()+2);
			add(SelectTalent);
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
    public static class debugTalent extends Window {

        //talents that can only be used by one hero class
        //TODO could some of these be made more generic?

        public static debugTalent INSTANCE;

        public Talent replacing;
        public int tier;
        LinkedHashMap<Talent, Integer> replaceOptions;

        //for window restoring
        public debugTalent(){
            super();

            if (INSTANCE != null){
                replacing = INSTANCE.replacing;
                tier = INSTANCE.tier;
                replaceOptions = INSTANCE.replaceOptions;
                INSTANCE = this;
                setup(tier, replaceOptions);
            } else {
                hide();
            }
        }

        public debugTalent(ArrayList<Talent> list){
            super();
            INSTANCE = this;

            for (Talent t:list){
                if (Dungeon.hero.hasTalentA(t)){
                    this.replacing = t;
                    this.tier = TierOfTalent.Tier(t);
                }
            }

            LinkedHashMap<Talent, Integer> options = new LinkedHashMap<>();
            for (Talent talent : list)
                options.put(talent, 0);
            replaceOptions = options;
            setup(TierOfTalent.Tier(list.get(0)), options);
        }
        private void setup(int tier, LinkedHashMap<Talent, Integer> replaceOptions){

            float top = 0;

            IconTitle title = new IconTitle(new debug());
            title.color( TITLE_COLOR );
            title.setRect(0, 0, 120, 0);
            add(title);

            top = title.bottom() + 2;

            RenderedTextBlock text = PixelScene.renderTextBlock(Messages.get(ScrollOfMetamorphosis.class, "replace_desc"), 6);
            text.maxWidth(120);
            text.setPos(0, top);
            add(text);

            top = text.bottom() + 2;

            TalentsPane.TalentTierPane optionsPane = new TalentsPane.TalentTierPane(replaceOptions, tier, TalentButton.Mode.debug_replace);
            add(optionsPane);
            optionsPane.title.text(" ");
            optionsPane.setPos(0, top);
            optionsPane.setSize(120, optionsPane.height());
            resize((int)optionsPane.width(), (int)optionsPane.bottom());

            resize(120, (int)optionsPane.bottom());
        }

        @Override
        public void hide() {
            super.hide();
            if (INSTANCE == this) {
                INSTANCE = null;
            }
        }

        @Override
        public void onBackPressed() {
            hide();
        }
    }
}
