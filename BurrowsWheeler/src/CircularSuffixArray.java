import java.util.Arrays;

/**
 * Created by Administrator on 13-12-29.
 */
public class CircularSuffixArray {
    //private int pointer;
    private int length;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if(s == null) throw new NullPointerException();
        length = s.length();
        index = new int[length];
        //char[] original = s.toCharArray();
        for(int i = 0; i < length; i++) {
            index[i] = i;
        }
        Modified3WaySort.sort(s, index);
        //Quick3Suffix.sort(s, index);
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if(i < 0 || i >= length) throw new IllegalArgumentException();
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray array = new CircularSuffixArray("0110110010");
        for(int i = 0; i < array.length(); i++)
            System.out.print(array.index[i] + " ");
        System.out.println("");
    }
}
