package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

import java.util.ArrayList;

public class TargetSpell extends BookSpell {
    protected RedBook bookA;
    protected Hero heroA;
    public void onCast(RedBook book, Hero hero) {
        bookA=book;
        heroA=hero;
        GameScene.selectCell(targeter);
    }

    protected void onSelectA(Integer cell){}
    //A作为正常操作
    protected void onSelectB(Integer cell){}
    //B为补救措施
    protected boolean Stop = false;
    protected CellSelector.Listener targeter = new CellSelector.Listener(){
        @Override
        public void onSelect(Integer cell) {
            if (cell==null || !Dungeon.level.heroFOV[cell]){
                return;
            }
            onSelectA(cell);
            if (Stop){
                return;
            }else {
                onSelectB(cell);
            }
            if (Stop){
                return;
            }
            TargetSpell.super.onCast(bookA, heroA);
        }

        @Override
        public String prompt() {
            return Messages.get(RedBook.class, "prompt");
        }
    };
}
