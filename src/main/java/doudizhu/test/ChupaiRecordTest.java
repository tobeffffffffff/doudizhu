

package doudizhu.test;


import com.google.common.collect.Lists;
import doudizhu.Card;
import doudizhu.ChupaiRecord;
import doudizhu.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Function:
 *
 * @author 水果
 * @Date 2023/10/12
 */
public class ChupaiRecordTest {

    @Test
    public void testLiandui() {
        final ArrayList<Card> cards = Lists.newArrayList(new Card(3), new Card(3), new Card(4), new Card(4), new Card(5), new Card(5));
        final ArrayList<Card> cards2 = Lists.newArrayList(new Card(4), new Card(4), new Card(5), new Card(5), new Card(6), new Card(6));
        final ChupaiRecord chupaiRecord = new ChupaiRecord(cards, new Player("A"));
        final ChupaiRecord chupaiRecord2 = new ChupaiRecord(cards2, new Player("B"));
        System.out.println();
    }

}
