//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Chrome.Type;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBook;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.RedBookSpell.BookSpell;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.NinePatch;
import com.watabou.utils.DeviceCompat;

import java.util.ArrayList;

public class WndRedBook extends Window {
    protected static final int WIDTH = 120;
    public static int BTN_SIZE = 20;

    public WndRedBook(final RedBook book, final boolean info) {
        IconTitle title;
        if (!info) {
            title = new IconTitle(new ItemSprite(book), Messages.titleCase(Messages.get(this, "cast_title")));
        } else {
            title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
        }

        title.setRect(0.0F, 0.0F, 120.0F, 0.0F);
        this.add(title);
        IconButton btnInfo = new IconButton(info ? new ItemSprite(book) : Icons.INFO.get()) {
            protected void onClick() {
                GameScene.show(new WndRedBook(book, !info));
                WndRedBook.this.hide();
            }
        };
        btnInfo.setRect(104.0F, 0.0F, 16.0F, 16.0F);
        this.add(btnInfo);
        RenderedTextBlock msg;
        if (info) {
            msg = PixelScene.renderTextBlock(Messages.get(this, "info_desc"), 6);
        } else if (DeviceCompat.isDesktop()) {
            msg = PixelScene.renderTextBlock(Messages.get(this, "cast_desc_desktop"), 6);
        } else {
            msg = PixelScene.renderTextBlock(Messages.get(this, "cast_desc_mobile"), 6);
        }

        msg.maxWidth(120);
        msg.setPos(0.0F, title.bottom() + 4.0F);
        this.add(msg);
        int top = (int) msg.bottom() + 4;

        for(int i = 1; i <= 4; ++i) {
            ArrayList<BookSpell> spells = BookSpell.getSpellList(book, i);
            if (!spells.isEmpty() && i != 1) {
                top += BTN_SIZE + 2;
                ColorBlock sep = new ColorBlock(120.0F, 1.0F, -16777216);
                sep.y = (float)top;
                this.add(sep);
                top += 3;
            }

            ArrayList<IconButton> spellBtns = new ArrayList();

            for(BookSpell spell : spells) {
                IconButton spellBtn = new SpellButton(spell, book, info);
                this.add(spellBtn);
                spellBtns.add(spellBtn);
            }

            int left = 2 + (120 - spellBtns.size() * (BTN_SIZE + 4)) / 2;

            for(IconButton btn : spellBtns) {
                btn.setRect((float)left, (float)top, (float)BTN_SIZE, (float)BTN_SIZE);
                left = (int)((float)left + btn.width() + 4.0F);
            }
        }

        this.resize(120, top + BTN_SIZE);
        if (SPDSettings.interfaceSize() != 2) {
            this.offset(0, GameScene.uiCamera.height / 2 - 30 - this.height / 2);
        }

    }

    public class SpellButton extends IconButton {
        BookSpell spell;
        RedBook book;
        boolean info;
        NinePatch bg;

        public SpellButton(BookSpell spell, RedBook book, boolean info) {
            super(new HeroIcon(spell));
            this.spell = spell;
            this.book = book;
            this.info = info;
            if (!book.canCast(spell)) {
                this.icon.alpha(0.3F);
            }

            this.bg = Chrome.get(Type.TOAST);
            if (this.bg != null) {
                this.addToBack(this.bg);
            }
        }


        protected void onPointerUp() {
            super.onPointerUp();
            if (!this.book.canCast(this.spell)) {
                this.icon.alpha(0.3F);
            }
        }

        protected void layout() {
            super.layout();
            if (this.bg != null) {
                this.bg.size(this.width, this.height);
                this.bg.x = this.x;
                this.bg.y = this.y;
            }

        }

        protected void onClick() {
            if (this.info) {
                GameScene.show(new WndTitledMessage(new HeroIcon(this.spell), Messages.titleCase(this.spell.name()), this.spell.desc()));
            } else {
                WndRedBook.this.hide();
                if (!this.book.canCast(this.spell)) {
                    GLog.w(Messages.get(RedBook.class, "no_spell"));
                } else {
                    this.spell.onCast(this.book, Dungeon.hero);
                }
            }

        }

        protected String hoverText() {
            String var10000 = Messages.titleCase(this.spell.name());
            return "_" + var10000 + "_\n" + this.spell.shortDesc();
        }
    }
}
