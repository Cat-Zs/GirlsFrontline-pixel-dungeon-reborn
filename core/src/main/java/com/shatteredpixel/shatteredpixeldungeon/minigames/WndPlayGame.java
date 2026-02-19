//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.minigames;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NoelShopkeeper;
import com.shatteredpixel.shatteredpixeldungeon.items.Grass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.NoelShopKeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;

public class WndPlayGame extends Window {
    public WndPlayGame(boolean endGame) {
        float pos = 0.0F;
        CardFront cardImage0 = new CardFront(PlayGame.PCcards.get(0));
        cardImage0.setRect(pos, 50.0F, 20.0F, 32.0F);
        this.add(cardImage0);
        pos += 22.0F;
        CardFront cardImage1 = new CardFront(PlayGame.PCcards.get(1));
        cardImage1.setRect(pos, 50.0F, 20.0F, 32.0F);
        this.add(cardImage1);
        pos += 22.0F;
        if (PlayGame.PCcards.size() >= 3) {
            CardFront cardImage2 = new CardFront(PlayGame.PCcards.get(2));
            cardImage2.setRect(pos, 50.0F, 20.0F, 32.0F);
            this.add(cardImage2);
            pos += 22.0F;
        }

        if (PlayGame.PCcards.size() >= 4) {
            CardFront cardImage3 = new CardFront(PlayGame.PCcards.get(3));
            cardImage3.setRect(pos, 50.0F, 20.0F, 32.0F);
            this.add(cardImage3);
            pos += 22.0F;
        }

        if (PlayGame.PCcards.size() >= 5) {
            CardFront cardImage4 = new CardFront(PlayGame.PCcards.get(4));
            cardImage4.setRect(pos, 50.0F, 20.0F, 32.0F);
            this.add(cardImage4);
            pos += 22.0F;
        }

        pos = 88.0F;
        if (!endGame) {
            CardBack cardImage5 = new CardBack();
            cardImage5.setRect(pos, 0.0F, 20.0F, 32.0F);
            this.add(cardImage5);
            pos -= 22.0F;
            CardBack cardImage6 = new CardBack();
            cardImage6.setRect(pos, 0.0F, 20.0F, 32.0F);
            this.add(cardImage6);
            pos -= 22.0F;
            if (PlayGame.AIcards.size() >= 3) {
                CardBack cardImage7 = new CardBack();
                cardImage7.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage7);
                pos -= 22.0F;
            }

            if (PlayGame.AIcards.size() >= 4) {
                CardBack cardImage8 = new CardBack();
                cardImage8.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage8);
                pos -= 22.0F;
            }

            if (PlayGame.AIcards.size() >= 5) {
                CardBack cardImage9 = new CardBack();
                cardImage9.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage9);
                pos -= 22.0F;
            }

            String PCaction = Messages.get(this, "pcdraw");
            RenderedTextBlock PCact = PixelScene.renderTextBlock(PCaction, 7);
            PCact.setRect(0.0F, 40.0F, PCact.width(), 10.0F);
            if (PlayGame.PCdraw) {
                this.add(PCact);
            }

            String AIaction = Messages.get(this, "aidraw");
            RenderedTextBlock AIact = PixelScene.renderTextBlock(AIaction, 7);
            AIact.setRect(110.0F - AIact.width(), 40.0F, AIact.width(), 10.0F);
            if (PlayGame.AIdraw) {
                this.add(AIact);
            }

            String yes = PlayGame.PCnum == 0 ? Messages.get(this, "toomany") : Messages.get(this, "drawmore");
            RedButton btnDraw = new RedButton(yes) {
                protected void onClick() {
                    PlayGame.PCdraw = true;
                    WndPlayGame.this.hide();
                    PlayGame.nextTurn();
                }
            };
            btnDraw.enable(PlayGame.PCcards.size() != 5 && PlayGame.PCnum != 0);
            btnDraw.setRect(0.0F, 90.0F, 55.0F, 20.0F);
            this.add(btnDraw);
            String no = PlayGame.PCnum >= 21 ? Messages.get(this, "enough") : Messages.get(this, "nomore");
            RedButton btnPass = new RedButton(no) {
                protected void onClick() {
                    PlayGame.PCdraw = false;
                    WndPlayGame.this.hide();
                    PlayGame.nextTurn();
                }
            };
            btnPass.setRect(55.0F, 90.0F, 55.0F, 20.0F);
            this.add(btnPass);
        } else {
            CardFront cardImage5f = new CardFront(PlayGame.AIcards.get(0));
            cardImage5f.setRect(pos, 0.0F, 20.0F, 32.0F);
            this.add(cardImage5f);
            pos -= 22.0F;
            CardFront cardImage6f = new CardFront(PlayGame.AIcards.get(1));
            cardImage6f.setRect(pos, 0.0F, 20.0F, 32.0F);
            this.add(cardImage6f);
            pos -= 22.0F;
            if (PlayGame.AIcards.size() >= 3) {
                CardFront cardImage7f = new CardFront(PlayGame.AIcards.get(2));
                cardImage7f.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage7f);
                pos -= 22.0F;
            }

            if (PlayGame.AIcards.size() >= 4) {
                CardFront cardImage8f = new CardFront(PlayGame.AIcards.get(3));
                cardImage8f.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage8f);
                pos -= 22.0F;
            }

            if (PlayGame.AIcards.size() >= 5) {
                CardFront cardImage9f = new CardFront(PlayGame.AIcards.get(4));
                cardImage9f.setRect(pos, 0.0F, 20.0F, 32.0F);
                this.add(cardImage9f);
                pos -= 22.0F;
            }

            String msg;
            if (PlayGame.PCnum >=21){
                msg = Messages.get(this, "price");
                price();
            }
            else if (PlayGame.PCnum > PlayGame.AInum) {
                msg = Messages.get(this, "win");
                win();
            }
            else if (PlayGame.PCnum < PlayGame.AInum) {
                msg = Messages.get(this, "lose");
                lose();
            }
            else {
                msg = Messages.get(this, "tie");
                tie();
            }

            RenderedTextBlock result = PixelScene.renderTextBlock(msg, 7);
            result.setRect(0.0F, 40.0F, result.width(), 10.0F);
            result.setHightlighting(true);
            this.add(result);
            RedButton btnAgain = new RedButton(Messages.get(this, "onemoregame")) {
                protected void onClick() {
                    WndPlayGame.this.hide();
                    PlayGame.gameStart();
                    Dungeon.gold-= NoelShopkeeper.PlayNeed;
                    GameScene.show(new WndPlayGame(false));
                }
            };
            btnAgain.setRect(0.0F, 90.0F, 55.0F, 20.0F);
            btnAgain.enable(Dungeon.gold>= NoelShopkeeper.PlayNeed);
            this.add(btnAgain);
            RedButton btnClose = new RedButton(Messages.get(this, "nomoregame")) {
                protected void onClick() {
                    WndPlayGame.this.hide();
                }
            };
            btnClose.setRect(55.0F, 90.0F, 55.0F, 20.0F);
            this.add(btnClose);
        }

        this.resize(110, 110);
    }
    private void price(){
        Dungeon.gold+=3* NoelShopkeeper.PlayNeed;
    }
    private void win(){
        Dungeon.gold += 2* NoelShopkeeper.PlayNeed;
    }
    private void lose(){
        if (Random.Int(10)==0) {
            Grass grass = new Grass();
            if (!grass.doPickUp(Dungeon.hero))
                Dungeon.level.drop(grass, Dungeon.hero.pos);
        }
    }
    private void tie(){
        Dungeon.gold+= NoelShopkeeper.PlayNeed;
    }
    @Override
    public void onBackPressed() {
        GameScene.show(
                new WndOptions(new NoelShopKeeperSprite(),
                        Messages.titleCase(Messages.get(this, "stop")),
                        Messages.get(this, "lose_warn"),
                        Messages.get(this, "yes"),
                        Messages.get(this, "no")) {
                    @Override
                    protected void onSelect(int index) {
                        if (index == 0){
                            hide();
                            WndPlayGame.this.hide();
                        }
                    }
                }
        );
    }

    private class CardFront extends Component {
        Image icon = new Image("interfaces/card_front.png");
        RenderedTextBlock txt;

        public CardFront(int i) {
            this.icon.frame(0, 0, 20, 32);
            String num;
            if (i == 1) {
                num = "A";
            } else if (i == 11) {
                num = "J";
            } else if (i == 12) {
                num = "Q";
            } else if (i == 13) {
                num = "K";
            } else {
                num = Integer.toString(i);
            }

            this.txt = PixelScene.renderTextBlock(num, 7);
            this.txt.setHightlighting(true);
        }

        protected void layout() {
            super.layout();
            this.icon.x = this.x;
            this.icon.y = this.y;
            this.add(this.icon);
            this.txt.setPos(this.icon.x + 3.0F, this.icon.y + (this.icon.height - this.txt.height()) / 4.0F);
            PixelScene.align(this.txt);
            this.add(this.txt);
        }
    }

    private class CardBack extends Component {
        Image icon = new Image("interfaces/card_back.png");

        public CardBack() {
            this.icon.frame(0, 0, 20, 32);
        }

        protected void layout() {
            super.layout();
            this.icon.x = this.x;
            this.icon.y = this.y;
            this.add(this.icon);
        }
    }
}
