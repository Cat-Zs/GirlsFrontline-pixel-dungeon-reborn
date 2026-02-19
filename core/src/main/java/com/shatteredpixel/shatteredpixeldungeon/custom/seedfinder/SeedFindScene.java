package com.shatteredpixel.shatteredpixeldungeon.custom.seedfinder;

import com.shatteredpixel.shatteredpixeldungeon.GirlsFrontlinePixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.SecondTitleScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.Archs;
import com.shatteredpixel.shatteredpixeldungeon.ui.ExitButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.ScrollPane;
import com.shatteredpixel.shatteredpixeldungeon.ui.WndTextInput;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndSettings;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Arrays;

public class SeedFindScene extends PixelScene {
    public static String seedCode = "";
    private static String want = "";
    private RenderedTextBlock currentSeedText;
    private Thread findSeedThread;
    private volatile boolean stopThread = false; // 仅用于停止线程，无间隔

    public void create() {
        super.create();
        final float fullWidth = 120.0F * (float) (landscape() ? 2 : 1);
        final int w = Camera.main.width;
        final int h = Camera.main.height;
        Archs archs = new Archs();
        archs.setSize((float) w, (float) h);
        this.add(archs);
        this.add(new ColorBlock((float) w, (float) h, -2013265920));

        // 初始化实时种子显示文本（无间隔刷新）
        currentSeedText = PixelScene.renderTextBlock("", 6);
        currentSeedText.setRect(((float) Camera.main.width - 90.0F) / 2.0F, 12.0F, 120.0F, 0.0F);
        currentSeedText.hardlight(0xFFFFFF);
        this.add(currentSeedText);

        final ScrollPane list = new ScrollPane(new Component());
        this.add(list);
        SeedFindScene.seedCode = "";
        Component content = list.content();
        content.clear();

        GirlsFrontlinePixelDungeon.scene().addToFront(new WndTextInput(
                Messages.get(this, "title"),
                Messages.get(this, "body"),
                Messages.get(this, "initial_value"),
                1000,
                true,
                Messages.get(this, "find"),
                Messages.get(this, "check")) {
            public void onSelect(boolean positive, String text) {
                want = "";
                if (positive && text != "") {
                    want+="\n"+text;
                    ArrayList<String> itemList = new ArrayList<>(Arrays.asList(text.split("\n")));
                    int floor = 31;
                    String strFloor = "层";
                    if (itemList.get(0).contains(strFloor)) {
                        String floorA = itemList.remove(0);
                        String fl = floorA.split(strFloor)[0].trim();
                        try {
                            floor = Integer.parseInt(fl);
                            if (floor>31||floor<1)
                                floor=31;
                        } catch (NumberFormatException ignored) {
                        }
                    }

                    HeroClass heroclass = HeroClass.WARRIOR;
                    String strHero = "角色";
                    if (itemList.get(0).contains(strHero)) {
                        String HeroSelect = itemList.remove(0);
                        String hero = HeroSelect.split(strHero)[0].trim();
                        try {
                            switch (hero) {
                                case "UMP45":
                                default:
                                    heroclass = HeroClass.WARRIOR;
                                    break;
                                case "G11":
                                    heroclass = HeroClass.MAGE;
                                    break;
                                case "UMP9":
                                    heroclass = HeroClass.ROGUE;
                                    break;
                                case "HK416":
                                    heroclass = HeroClass.HUNTRESS;
                                    break;
                                case "56-1式":
                                    heroclass = HeroClass.TYPE561;
                                    break;
                                case "GSh-18":
                                    heroclass = HeroClass.GSH18;
                                    break;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                    if (itemList.isEmpty()) {
                        GirlsFrontlinePixelDungeon.switchNoFade(SecondTitleScene.class);
                        return;
                    }
                    // 重置停止标记，启动无间隔后台线程
                    stopThread = false;
                    int finalFloor = floor;
                    HeroClass finalHeroclass = heroclass;
                    findSeedThread = new Thread(() -> {
                        try {
                            SeedFinder finder = new SeedFinder();
                            // 传入当前Scene引用，用于无间隔刷新UI
                            String result = finder.findSeed(itemList, finalFloor, SPDSettings.challenges(), finalHeroclass, SeedFindScene.this);

                            // 查找完成后主线程更新结果
                            if (!stopThread) {
                                GirlsFrontlinePixelDungeon.runOnRenderThread(() -> {
                                    content.clear();
                                    CreditsBlock txt = new CreditsBlock(true, 16777028, result);
                                    txt.setRect(((float) Camera.main.width - 120.0F) / 2.0F, 12.0F, 120.0F, 0.0F);
                                    content.add(txt);
                                    content.setSize(fullWidth, txt.bottom() + 10.0F);
                                    list.setRect(0.0F, 0.0F, (float) w, (float) h);
                                    list.scrollTo(0.0F, 0.0F);
                                    currentSeedText.text(" ");
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            GirlsFrontlinePixelDungeon.runOnRenderThread(() -> {
                                currentSeedText.text("查找失败：" + e.getMessage());
                            });
                        } finally {
                            stopThread = false;
                        }
                    });
                    findSeedThread.start(); // 无间隔启动线程

                }else if (!positive&&text!=""){
                    content.clear();
                    CreditsBlock txt = new CreditsBlock(true, 16777028, (new SeedFinder()).checkSeed(text, HeroClass.WARRIOR));
                    txt.setRect(((float)Camera.main.width - 120.0F) / 2.0F, 12.0F, 120.0F, 0.0F);
                    content.add(txt);
                    content.setSize(fullWidth, txt.bottom() + 10.0F);
                    list.setRect(0.0F, 0.0F, (float)w, (float)h);
                    list.scrollTo(0.0F, 0.0F);
                } else {
                    stopThread = true;
                    if (findSeedThread != null && findSeedThread.isAlive()) {
                        findSeedThread.interrupt();
                    }
                    GirlsFrontlinePixelDungeon.switchNoFade(SecondTitleScene.class);
                    System.gc();
                }
            }
        });
        setButton();
        IconButton notice = new IconButton(Icons.WARNING.get()){
            @Override
            public void onClick() {
                Game.scene().add(new SeedFinderNotice());
            }
        };
        notice.setSize(20,20);
        notice.setPos( (float) Camera.main.width - notice.width()-5, (float) Camera.main.height - notice.height()-5 );
        add(notice);

        ExitButton exitBtn = new ExitButton() {
            @Override
            public void onClick() {
                stopThread = true;
                if (findSeedThread != null && findSeedThread.isAlive()) {
                    findSeedThread.interrupt();
                }
                SeedFinder.SeedFinding = false;
                GirlsFrontlinePixelDungeon.switchNoFade(SecondTitleScene.class);
            }
        };
        exitBtn.setPos((float) Camera.main.width - exitBtn.width(), 0);
        this.add(exitBtn);
    }

    // 无间隔更新种子显示（直接主线程回调，无sleep）
    public void updateCurrentSeed(long seed) {
        GirlsFrontlinePixelDungeon.runOnRenderThread(() -> {
            if (currentSeedText != null && !stopThread) {
                currentSeedText.text("当前遍历种子：" + seed+want);
            }
        });
    }

    protected void onBackPressed() {
        stopThread = true;
        if (findSeedThread != null && findSeedThread.isAlive()) {
            findSeedThread.interrupt();
        }
        GirlsFrontlinePixelDungeon.switchScene(SecondTitleScene.class);
    }

    private void setButton() {
        RedButton btnCopy = new RedButton("") {
            @Override
            protected void onPointerDown() {
                super.onPointerDown();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onPointerUp() {
                super.onPointerUp();
                PointerEvent.clearKeyboardThisPress = false;
            }

            @Override
            protected void onClick() {
                super.onClick();
                if (SeedFindScene.seedCode != null && !SeedFindScene.seedCode.isEmpty()) {
                    GirlsFrontlinePixelDungeon.scene().addToFront(
                            new WndOptions(new Image(new ItemSprite(ItemSpriteSheet.SEED_HOLDER)),
                                    Messages.get(WndSettings.class, "copy_title"),
                                    Messages.get(WndSettings.class, "copy_body"),
                                    Messages.get(WndSettings.class, "copy_yes"),
                                    Messages.get(WndSettings.class, "copy_no")) {

                                protected void onSelect(int index) {
                                    super.onSelect(index);
                                    if (index == 0) {
                                        SPDSettings.seedCode(SeedFindScene.seedCode);
                                    }
                                }
                            }
                    );
                }
            }
        };
        btnCopy.icon(Icons.RENAME_ON.get());
        btnCopy.enable(true);
        add(btnCopy);
        btnCopy.setRect(0, 0, 20, 20);
    }

    @Override
    public void destroy() {
        super.destroy();
        stopThread = true;
        if (findSeedThread != null && findSeedThread.isAlive()) {
            findSeedThread.interrupt();
        }
    }

    public static class CreditsBlock extends Component {
        boolean large;
        RenderedTextBlock body;

        public CreditsBlock(boolean large, int highlight, String body) {
            this.large = large;
            this.body = PixelScene.renderTextBlock(body, 6);
            if (highlight != -1) {
                this.body.setHightlighting(true, highlight);
            }

            if (large) {
                this.body.align(2);
            }

            this.add(this.body);
        }

        protected void layout() {
            super.layout();
            float topY = this.top();
            if (this.large) {
                this.body.maxWidth((int) this.width());
                this.body.setPos(this.x + (this.width() - this.body.width()) / 2.0F, topY);
            } else {
                ++topY;
                this.body.maxWidth((int) this.width());
                this.body.setPos(this.x, topY);
            }

            topY += this.body.height();
            this.height = Math.max(this.height, topY - this.top());
        }
    }
}