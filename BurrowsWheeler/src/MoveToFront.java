import java.util.Arrays;

/**
 * Created by Administrator on 13-12-28.
 */
public class MoveToFront {
    private int length = 0;
    private char[] str;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        StringBuffer sbf = new StringBuffer();
        int i;
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            char c = BinaryStdIn.readChar();
            //StdOut.printf("%02x", c & 0xff);
            sbf.append(c);
        }
        char[] str = sbf.toString().toCharArray();
        int length = i;
        LinkedList list = new LinkedList();
        for(i = 0; i < length; i++) {
            list.moveToFirst(str[i]);
            BinaryStdOut.write((char)list.getLastFind(), 8);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        StringBuffer sbf = new StringBuffer();
          int i;
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            char c = BinaryStdIn.readChar();
            //StdOut.printf("%02x", c & 0xff);
            sbf.append(c);
        }
        char[] str = sbf.toString().toCharArray();
        //char[] str = {0x41, 0x42, 0x52, 0x02, 0x44, 0x01, 0x45, 0x01, 0x04, 0x04, 0x02, 0x26};
        int length = str.length;
        LinkedList list = new LinkedList();
        for(i = 0; i < length; i++) {
            char value = (char)list.findByStep(str[i]);
            list.moveToFirst(value);
            BinaryStdOut.write(value, 8);
        }
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        String operator = args[0];
        if("-".equals(operator)) encode();
        if("+".equals(operator)) decode();

    }
}