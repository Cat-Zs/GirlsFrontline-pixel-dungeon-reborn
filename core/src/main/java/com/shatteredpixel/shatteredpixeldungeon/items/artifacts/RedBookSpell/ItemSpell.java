package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;

public class ItemSpell extends BookSpell {
    protected RedBook bookA;
    protected Hero heroA;
    public void onCast(RedBook book, Hero hero) {
        bookA=book;
        heroA=hero;
        GameScene.selectItem( itemSelector );
    }

    protected boolean usableOnItem( Item item ){
        return true;
    }

    protected void onItemAct( Item item ){}
    protected boolean Stop = false;

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return "";
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
        public void onSelect( Item item ) {

            if (!itemSelectable(item)){
                return;
            }

            if (item != null) {
                onItemAct(item);
            }
            if (Stop){
                return;
            }
            ItemSpell.super.onCast(bookA,heroA);
        }
    };
}
