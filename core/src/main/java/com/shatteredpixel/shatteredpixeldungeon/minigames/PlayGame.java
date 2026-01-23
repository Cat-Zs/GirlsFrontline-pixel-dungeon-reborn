//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.shatteredpixel.shatteredpixeldungeon.minigames;

import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayGame {
    private static final ArrayList<Integer> defaultCards = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
    private static final ArrayList<Integer> cards = new ArrayList<>();
    public static ArrayList<Integer> PCcards = new ArrayList<>();
    public static ArrayList<Integer> AIcards = new ArrayList<>();
    public static int PCscore;
    public static int AIscore;
    public static int PCnum = 0;
    public static int AInum = 0;
    public static boolean PCdraw;
    public static boolean AIdraw;
    public static int turn;

    public static void shuffle() {
        for(int card : defaultCards) {
            cards.add(card);
        }

    }

    public static void resetHand() {
        if (PCcards.size() > 0) {
            PCcards.subList(0, PCcards.size()).clear();
        }

        PCnum = 0;
        if (AIcards.size() > 0) {
            AIcards.subList(0, AIcards.size()).clear();
        }

        AInum = 0;
    }

    public static int randomCard() {
        if (cards.size() == 0) {
            shuffle();
        }

        int a = Random.Int(cards.size());
        int b = (Integer)cards.get(a);
        cards.remove(a);
        if (cards.size() == 0) {
            shuffle();
        }

        return b;
    }

    public static int count(ArrayList<Integer> arrayList) {
        int totalNum = 0;
        boolean haveA = false;

        for(int card : arrayList) {
            totalNum += Math.min(card, 10);
            if (card == 1) {
                haveA = true;
            }
        }

        if (totalNum > 21) {
            return 0;
        } else {
            if (totalNum <= 11 && haveA) {
                totalNum += 10;
            }

            if (totalNum == 21 && haveA) {
                ++totalNum;
            }

            return totalNum;
        }
    }

    public static void drawCard(ArrayList<Integer> arrayList) {
        int card = randomCard();
        arrayList.add(card);
    }

    public static void gameStart() {
        resetHand();
        turn = 1;
        PCdraw = false;
        AIdraw = false;
        drawCard(PCcards);
        drawCard(PCcards);
        drawCard(AIcards);
        drawCard(AIcards);
        PCnum = count(PCcards);
        AInum = count(AIcards);
    }

    public static void nextTurn() {
        ++turn;
        AI();
        if (!PCdraw && !AIdraw) {
            endGame();
        } else {
            if (PCdraw) {
                drawCard(PCcards);
            }

            if (AIdraw) {
                drawCard(AIcards);
            }

            PCnum = count(PCcards);
            AInum = count(AIcards);
            GameScene.show(new WndPlayGame(false));
        }
    }

    public static void endGame() {
        GameScene.show(new WndPlayGame(true));
    }

    public static void AI() {
        float num = 0.0F;

        for(int card : cards) {
            num += (float)Math.min(card, 10);
        }

        num /= (float)cards.size();
        if (AInum <= 20 && AInum != 0 && AIcards.size() != 5) {
            if (AInum < 12) {
                AIdraw = true;
            } else {
                if (num + (float)AInum <= 21.0F) {
                    AIdraw = !((double)Random.Float() < Math.pow((double)0.5F, Math.pow((double)((float)(21 - AInum) / num), (double)3.0F)) * (double)AInum / (double)(21.0F - num));
                }

                if (num + (float)AInum > 21.0F) {
                    AIdraw = (double)Random.Float() < Math.pow((double)((float)(21 - AInum) / num), (double)2.0F);
                }

            }
        } else {
            AIdraw = false;
        }
    }
}
