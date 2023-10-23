
package doudizhu;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Data
public class Player {

    private List<Card> cards;

    private String name;

    public Player(String name) {
        this.name = name;
        cards = new ArrayList<>(18);
    }

    public ChupaiRecord chupai(ChupaiRecord lastRecord) {
        // 选择要出的牌
        List<Integer> cardIndex = getInput();
        if (CollUtil.isEmpty(cardIndex) || cardIndex.stream().anyMatch(e -> e == -1)) {
            if (lastRecord.getPlayer().equals(this)) {
                throw new RuntimeException("你必须出牌！");
            }
            System.out.println("玩家该轮跳过出牌回合");
            return null;
        }
        List<Card> chupai = new ArrayList<>(cardIndex.size());
        for (Integer index : cardIndex) {
            chupai.add(cards.get(index));
        }
        // 创建出牌记录 && 判断要出的牌是否符合出牌规则
        final ChupaiRecord record = new ChupaiRecord(chupai, this);
        final ChupaiType chupaiType = record.getType();
        if (chupaiType == null) {
            throw new RuntimeException("出牌不符合规则");
        }
        // 上一次的出牌记录为空, 或者其他两家没有出牌, 不需要判断大小
        if (lastRecord == null || lastRecord.getPlayer().equals(this)) {
            removeUsedCard(cardIndex);
            return record;
        } else if (!lastRecord.getType().equals(record.getType())) {
            throw new RuntimeException("出牌的类型不同， 不符合规则");
        } else if (!lastRecord.getSize().equals(record.getSize())) {
            throw new RuntimeException("出牌的size不同，不符合规则");
        } else {
            final Card curCard = new Card(record.getStartValue());
            final Card lastCard = new Card(lastRecord.getStartValue());
            if (curCard.compareTo(lastCard) <= 0) {
                throw new RuntimeException("你的牌没有人家的大，请重新出牌");
            }
        }
        // 将出了的牌从手牌中移除
        removeUsedCard(cardIndex);
        return record;
    }

    public void removeUsedCard(List<Integer> cardIndex) {
        cardIndex = cardIndex.stream().sorted().collect(Collectors.toList());
        for (int i = cardIndex.size() - 1; i >= 0; i--) {
            int idx = cardIndex.get(i);
            cards.remove(idx);
        }
    }

    public void napai(Card card) {
        cards.add(card);
    }

    public void paixu() {
        cards = cards.stream().sorted().collect(Collectors.toList());
    }

    public void zhanshishoupai() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(":");
        final List<Card> collect = cards.stream().sorted().collect(Collectors.toList());
        int i = 0;
        for (Card card : collect) {
            sb.append(card.toString());
            sb.append("=>(").append(i++).append(")");
            sb.append(" ");
        }
        System.out.println(sb);
    }

    public List<Integer> getInput() {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入你想要出的牌的index, 逗号分割!, 当前出牌人：" + name);
        final String str = sc.next();
        if (StrUtil.isBlank(str)) {
            return Collections.emptyList();
        }
        return Arrays.stream(str.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(3);
        list.add(6);
        list.add(8);
        list.add(5);
        final List<Integer> collect = list.stream().sorted().collect(Collectors.toList());
        for (Integer integer : collect) {
            System.out.println(integer);
        }
    }

}
