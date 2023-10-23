

package doudizhu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ChupaiRecord {

    public Player getPlayer() {
        return player;
    }

    public List<Card> getCards() {
        return cards;
    }

    public ChupaiType getType() {
        return type;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getStartValue() {
        return startValue;
    }

    private final Player player;

    private List<Card> cards;

    private final ChupaiType type;

    private Integer size;

    private Integer startValue;

    public void paixu() {
        cards = cards.stream().sorted().collect(Collectors.toList());
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public ChupaiRecord(List<Card> cards, Player player) {
        this.cards = cards;
        this.player = player;
        this.type = checkoutRule();
    }


    public ChupaiType checkoutRule() {
        // 单张
        if (cards.size() == 1) {
            size = 1;
            startValue = cards.get(0).getValue();
            return ChupaiType.DAN_ZHANG;
        }
        // 对子
        if (cards.size() == 2) {
            // 判断是否为王炸
            final Card card1 = cards.get(0);
            final Card card2 = cards.get(1);
            size = 1;
            if ((card1.equals(Card.BLACK_JOKER) || card1.equals(Card.RED_JOKER)) && (card2.equals(Card.BLACK_JOKER) || card2.equals(Card.RED_JOKER))) {
                this.startValue = Card.BLACK_JOKER.getValue();
                return ChupaiType.WANG_ZHA;
            }
            if (card1.getValue().equals(card2.getValue())) {
                this.startValue = card1.getValue();
                return ChupaiType.DUI_ZI;
            } else {
                return null;
            }
        }
        // 三带
        if (cards.size() == 3) {
            size = 1;
            startValue = cards.get(0).getValue();
            return cards.get(0).equals(cards.get(1)) && cards.get(1).equals(cards.get(2)) ? ChupaiType.SAN_ZHANG : null;
        }
        // 判断连对和顺子
        // 先排序
        paixu();
        // 判断同一种牌出现的次数
        int[] count = new int[16];
        int max = 0;
        for (Card card : cards) {
            Integer value = card.getValue();
            if (value == -1 || value == 0) {
                System.out.println("大王或小王只能单出或者炸弹!");
                return null;
            }
            if (value == 1 || value == 2) {
                value += 13;
            }
            count[value]++;
            max = Math.max(count[value], max);
        }
        // 构建牌出现频率的大根堆
//        CountNode[] countNodes = new CountNode[16];
//        for (Card card : cards) {
//            Integer value = card.getValue();
//            if (value == -1 || value == 0) {
//                System.out.println("大王或小王只能单出或者炸弹!");
//                return null;
//            }
//            if (value == 1 || value == 2) {
//                value += 13;
//            }
//            if (countNodes[value] == null) {
//                countNodes[value] = new CountNode(value, 0);
//            }
//            countNodes[value].increaseFreq();
//        }
        this.startValue = getStartValueByCount(count, max);
        // 如果最大值为2 则说明是对子， 如果最大值为3则是三带， 如果是四张则是炸弹
        if (max == 1) {
            // 顺子
            if (cards.size() < 5) {
                System.out.println("顺子的长度不够5啊");
                return null;
            }
            if (!lianxuxing(cards)) {
                System.out.println("这货根本不是顺子, 因为不连续");
                return null;
            }
            this.size = cards.size();
            return ChupaiType.SHUN_ZI;
        } else if (max == 2) {
            // 连对
            if (cards.size() % 2 != 0 || cards.size() < 6) {
                System.out.println("这货根本不是连对, 因为你长度不对");
                return null;
            }
            // 判断组成连对的顺序
            int elementCount = cards.size() / 2;
            // 去重后判断连续性
            for (int i = 0; i < count.length; i++) {
                if (count[i] != 0 && count[i] != max) {
                    System.out.println("这货不是连对，因为不是每张牌都是对子");
                    return null;
                }
            }
            for (int i = (startValue == 1 || startValue == 2) ? startValue + 13 : startValue; i < startValue + elementCount - 1; i++) {
                if (count[i] != max) {
                    System.out.println("这货根本不是连对，因为对子不连续");
                    return null;
                }
            }
            this.size = elementCount;
            return ChupaiType.LIAN_DUI;
        } else if (max == 3) {
            // 三带N / 飞机
            if (cards.size() == 4) {
                return ChupaiType.SAN_ZHANG;
            }
            if (cards.size() == 5) {
                // 判断剩下的两张牌是不是对子
                for (int i = 0; i < count.length; i++) {
                    if (count[i] == 1) {
                        System.out.println("这货根本不是三带二， 因为带的不是对子");
                        return null;
                    }
                }
            }
            // size 大于 6 只可能是飞机
            int maxCount = 0;
            for (int i = 0; i < count.length; i++) {
                if (count[i] == max) maxCount++;
            }
            if(maxCount < 2) {
                System.out.println("这货根本不是飞机， 起码要是两组三张吧");
                return null;
            }
            // 判断三张是不是都是连续的
            for (int i = startValue; i < startValue + maxCount; i++) {
                if(count[i] != max) {
                    System.out.println("这货根本是不飞机，你的三张都不是连续的");
                    return null;
                }
            }
            // 判断出牌的张数是否符合要求
            if (cards.size() == (3 * maxCount + maxCount)) {
                size = maxCount;
                return ChupaiType.FEI_JI;
            } else if (cards.size() == (3 * maxCount + 2 * maxCount)) {
                //  判断除了三张的牌以外是否都是对子
                int doubleCount = 0;
                for (int i = 0; i < count.length; i++) {
                    if (count[i] == 2) doubleCount++;
                }
                if (maxCount == doubleCount) {
                    size = maxCount;
                    return ChupaiType.FEI_JI;
                } else {
                    System.out.println("这货根本不是飞机， 因为带的牌不全是对子");
                    return null;
                }
            } else {
                System.out.println("这货根本不是飞机， 出牌张数不对啊");
                return null;
            }
        } else if (max == 4) {
            // 炸弹
            if (size == 4) {
                return ChupaiType.ZHA_DAN;
            } else {
                System.out.println("炸弹不准夹带私货!");
                return null;
            }
        }
        return null;
    }

    /**
     * 连续性判断
     * @param cards
     * @return
     */
    private boolean lianxuxing(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            final Card card = cards.get(i);
            Integer curValue = card.getValue();
            final Card nextCard = cards.get(i + 1);
            Integer nextValue = nextCard.getValue();
            if (curValue == 1 || curValue == 2) {
                curValue += 13;
            }
            if (nextValue == 1 || nextValue == 2) {
                nextValue += 13;
            }
            if (nextValue - curValue != 1) {
                return false;
            }
        }
        return true;
    }

    private Integer getStartValueByCount(int[] count, int elementCount) {
        for (int i = 3; i < count.length; i++) {
            if (count[i] == elementCount) {
                return i <= 13 ? i : i - 13;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(3, null));
        cards.add(new Card(3, null));
        cards.add(new Card(3, null));
        cards.add(new Card(4, null));
        cards.add(new Card(4, null));
        cards.add(new Card(4, null));
        cards.add(new Card(7, null));
        cards.add(new Card(7, null));
        cards.add(new Card(5, null));
        cards.add(new Card(9, null));
        final Player a1 = new Player("a");
        final ChupaiRecord a = new ChupaiRecord(cards, a1);
        System.out.println(a.getType().getName());
        System.out.println(a.getStartValue());
        System.out.println(a.getSize());
        a1.chupai(a);

    }


}
