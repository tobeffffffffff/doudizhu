
package doudizhu;

public class CountNode implements Comparable<CountNode> {

    /**
     * 下标
     */
    private int index;

    /**
     * 频率
     */
    private int freq;

    public CountNode(int index, int freq) {
        this.index = index;
        this.freq = freq;
    }

    @Override
    public int compareTo(CountNode other) {
        if (other.freq != this.freq) {
            return Integer.compare(other.freq, this.freq);
        } else {
            return Integer.compare(this.index, other.index);
        }
    }

    @Override
    public String toString() {
        return "CountNode{" +
                "index=" + index +
                ", freq=" + freq +
                '}';
    }

    public void increaseFreq() {
        this.freq++;
    }
}
