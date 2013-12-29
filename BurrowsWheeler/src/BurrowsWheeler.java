import java.util.Arrays;

/**
 * Created by Administrator on 13-12-29.
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        StringBuffer sbf = new StringBuffer();
        int i = 0;
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            char c = BinaryStdIn.readChar();
            //StdOut.printf("%02x", c & 0xff);
            sbf.append(c);
        }
        int length = i;
//        String str = "ABRACADABRA!"; length = str.length();
        CircularSuffixArray array = new CircularSuffixArray(sbf.toString());
        int zero = 0;
        while(array.index(zero) != 0) zero++;
        //System.out.println("zero=" + zero);
        BinaryStdOut.write(zero);
        for(i = 0; i < array.length(); i++) {
            int mod = array.index(i) % (length);
            char character = (mod == 0)?sbf.charAt(length - 1):sbf.charAt(mod - 1);
            BinaryStdOut.write(character);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        int i = 0;
        StringBuffer sbf = new StringBuffer();
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            char c = BinaryStdIn.readChar();
            //StdOut.printf("%02x", c & 0xff);
            sbf.append(c);
        }
        int length = i;
        char[] t = sbf.toString().toCharArray();
        char[] firstLetters = new char[t.length];
        int[] next = new int[t.length];
        int[] count = new int[256 + 1];
        //count number
        for(int j = 0; j < t.length; j++) {
            count[t[j] + 1]++;
        }
        //accum number
        for(int j = 0; j < 256; j++) {
            count[j + 1] += count[j];
        }
        //construct first letters and next array
        for(int j = 0; j < t.length; j++) {
            firstLetters[count[t[j]]] = t[j];
            next[count[t[j]]] = j;
            count[t[j]]++;
        }
        //decode
        int cnt = 0;
        for(int j = first; cnt < length; j = next[j]) {
            BinaryStdOut.write(firstLetters[j]);
            cnt++;
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        String operator = args[0];
        if("-".equals(operator)) encode();
        if("+".equals(operator)) decode();
    }
}
