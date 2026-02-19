package com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

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
    //B为补救措施
    protected boolean Sudden = false;
    protected CellSelector.Listener targeter = new CellSelector.Listener(){
        @Override
        public void onSelect(Integer cell) {
            if (cell==null || !Dungeon.level.heroFOV[cell]){
                return;
            }
            onSelectA(cell);
            if (Stop){
                if (Sudden) {
                    //在onSelectA中，选中可选择格子的情况下仍进入了Stop，将会对是否处在突发环境进行幅值，如果确认处在突发环境将会执行应对措施onSelectB
                    onSelectB(cell);
                }
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
