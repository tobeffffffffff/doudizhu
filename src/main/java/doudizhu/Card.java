

package doudizhu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Function:
 *
 * @author 水果
 * @Date 2023/10/10
 */
@AllArgsConstructor
@Data
public class Card implements Comparable {

    public static final Card BLACK_JOKER = new Card(-1, null);

    public static final Card RED_JOKER = new Card(0, null);

    private Integer value;

    private TypeEnum type;

    //  A => 14, 2 => 15, 小王 => 16, 大王 => 17
    @Override
    public int compareTo(Object o) {
        Card card = (Card) o;
        if (this.value.equals(card.value)) {
            return 0;
        }
        // 两边都在3-13之间
        if ((3 <= this.value && this.value <= 13) && (3 <= card.value && card.value <= 13)) {
            return this.value - card.value;
        }
        // 某一边为特殊值（1、2、-1、0）
        if (this.value < 3 && card.value >= 3) {
            return 1;
        }
        if (card.value < 3 && this.value >= 3) {
            return -1;
        }
        // 两边都为特殊值
        if (this.value == -1) {
            // 小王只比大王小
            return card.value == 0 ? -1 : 1;
        } else if (this.value == 0) {
            // 大王最大
            return 1;
        } else if (this.value == 2) {
            // 2 比小王大王小
            return (card.value == -1 || card.value == 0) ? -1 : 1;
        } else {
            // 1 比2 小王 大王 小
            return (card.value == -1 || card.value == 0 || card.value == 2) ? -1 : 1;
        }
    }

    public Card(Integer value) {
        this.value = value;
    }

    public static void main(String[] args) {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(1, TypeEnum.BLACK_TAO));
        cards.add(Card.BLACK_JOKER);
        cards.add(new Card(2, TypeEnum.BLACK_TAO));
        cards.add(Card.RED_JOKER);
        cards.add(new Card(10, TypeEnum.BLACK_TAO));
        cards.add(new Card(3, TypeEnum.BLACK_TAO));
        cards.add(new Card(5, TypeEnum.RED_TAO));
        cards.add(new Card(4, TypeEnum.RED_TAO));
        cards = cards.stream().sorted().peek(System.out::println).collect(Collectors.toList());
        System.out.println(new Card(1, null).equals(new Card(1, null)));
    }

    @Override
    public String toString() {
        if (this.equals(Card.BLACK_JOKER)) {
            return "小王";
        } else if (this.equals(Card.RED_JOKER)) {
            return "大王";
        } else {
            String val;
            if (value == 1) {
                val = "A";
            } else if (value == 11) {
                val = "J";
            } else if (value == 12) {
                val = "Q";
            } else if (value == 13) {
                val = "K";
            } else {
                val = value.toString();
            }
            return type.getValue() + val;
        }
    }

}

@AllArgsConstructor
enum TypeEnum {
    BLACK_TAO("黑桃"),
    RED_TAO("红桃"),
    CAOHUA("草花"),
    FANGPIAN("方片")
    ;

    @Getter
    private String value;

}
