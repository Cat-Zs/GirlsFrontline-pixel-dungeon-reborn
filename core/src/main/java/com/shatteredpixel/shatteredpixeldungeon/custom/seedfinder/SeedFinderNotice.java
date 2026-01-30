package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.ui.Component;

public class SeedFinderNotice extends Window {

    public SeedFinderNotice() {
        int width = 120;
        String message = Messages.get(SeedFindScene.class, "notice");

        ScrollPane pane = new ScrollPane(new Component());
        Component content = pane.content();
        this.add(pane);
        IconTitle titlebar = new IconTitle(new ItemSprite(ItemSpriteSheet.SEED_SUNGRASS), "注意事项");
        titlebar.color( TITLE_COLOR );
        content.add(titlebar);
        titlebar.setRect(0.0F, 0.0F, (float)width, 0.0F);
        RenderedTextBlock text = PixelScene.renderTextBlock(6);
        text.text(message, width);
        content.add(text);
        text.setPos(titlebar.left(), titlebar.bottom() + 4.0F);
        content.setSize((float)width, text.bottom() + 2.0F);
        this.resize(width, Math.min(153, (int)content.height() + 1));
        content.setPos(0.0F, 0.0F);
        pane.setRect(0.0F, 0.0F, (float)width, (float)(153));
    }
}
