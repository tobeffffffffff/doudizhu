
package doudizhu;


import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static doudizhu.Card.BLACK_JOKER;
import static doudizhu.Card.RED_JOKER;


public class Doudizhu {

    private Card[] cards;

    private Random random = new Random();

    private Player[] players = new Player[3];

    private int[] usedCardCount = new int[15];

    private List<ChupaiRecord> chupaiRecords = new ArrayList<>();

    private int dizhu = 0;

    private int curPlayerIndex = dizhu;

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        Doudizhu game = new Doudizhu();
        game.init();
        // 玩家轮流出牌
        while (game.isNotEnd()) {
            game.printCard(-1);
            try {
                ChupaiRecord record = game.getCurPlayer().chupai(game.getLastRecord());
                game.recordChupai(record);
            } catch (Exception e) {
                System.out.println("玩家出牌错误, 重新出牌, because: " + e.getMessage());
                game.curPlayerIndex--;
                continue;
            }
        }
        System.out.println("获胜的玩家是： " + game.getCurPlayer().getName());
    }

    private Player getCurPlayer() {
        return players[curPlayerIndex++ % 3];
    }

    private ChupaiRecord getLastRecord() {
        if (chupaiRecords.size() == 0) {
            return null;
        }
        return this.chupaiRecords.get(this.chupaiRecords.size() - 1);
    }

    private void recordChupai(ChupaiRecord record) {
        if (record == null) {
            return;
        }
        chupaiRecords.add(record);
        final List<Card> cards = record.getCards();
        for (Card card : cards) {
            Integer value = card.getValue();
            if (value == -1) {
                value = 14;
            }
            usedCardCount[value]++;
        }
    }

    private void init() {
        Card[] cards = new Card[54];
        // 初始化54牌
        int count = 0;
        for (int i = 1; i < 14; i++) {
            for (int j = 0; j < TypeEnum.values().length; j++) {
                TypeEnum type = TypeEnum.values()[j];
                cards[count++] = new Card(i, type);
            }
        }
        cards[count++] = BLACK_JOKER;
        cards[count] = RED_JOKER;
        // 洗牌
        shuffleRandomPoker(cards);
        this.cards = cards;
        // 初始化玩家
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(String.valueOf((char) ('A' + i)));
        }
        // 发牌
        for (int i = 0; i < this.cards.length - 3; i++) {
            players[i % players.length].napai(cards[i]);
        }
        // 选一个地主并将剩下的三张牌都给地主
        dizhu = random.nextInt(3);
        curPlayerIndex = dizhu;
        for (int i = 0; i < 3; i++) {
            players[dizhu].napai(cards[cards.length - 1 - i]);
        }
        // 每个玩家给牌按照大小排序
        for (int i = 0; i < players.length; i++) {
            players[i].paixu();
        }
    }

    private void shuffleRandomPoker(Card[] poker) {
        Card[] b = new Card[54];
        for (int l = 0; l < 3; l++) {
            Card[] front = Arrays.copyOfRange(poker, 0, 27);
            Card[] back = Arrays.copyOfRange(poker, 27, 54);
            int i = 0, j = 0, k = 0;
            while (i < 27 && j < 27) {
                int n = random.nextInt(2);
                if (n == 0) {
                    b[k] = front[i];
                    k++;
                    i++;
                }
                if (n == 1) {
                    b[k] = back[j];
                    k++;
                    j++;
                }
                if (i - j > 3) {
                    b[k] = back[j];
                    k++;
                    j++;
                }
                if (j - i > 3) {
                    b[k] = front[i];
                    k++;
                    i++;
                }
            }
            if (i < 27) {
                for (; i < 27; i++, k++) {
                    b[k] = front[i];
                }
            }
            if (j < 27) {
                for (; j < 27; j++, k++) {
                    b[k] = back[j];
                }
            }
            for (i = 0; i < 54; i++) {
                poker[i] = b[i];
            }
        }
    }

    /**
     * 是否结束
     * @return
     */
    private boolean isEnd() {
        if (Arrays.stream(this.players).anyMatch(p -> CollUtil.isEmpty(p.getCards()))) {
            curPlayerIndex--;
            return true;
        } else {
            return false;
        }
    }

    private boolean isNotEnd() {
        return !isEnd();
    }

    public void printCard(int player) {
        if (player == -1) {
            for (int i = 0; i < players.length; i++) {
                players[i].zhanshishoupai();
            }
        } else {
            players[player].zhanshishoupai();
        }
    }

}
