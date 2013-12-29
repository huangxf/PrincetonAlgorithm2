/**
 * Created by Administrator on 13-12-29.
 */
public class Modified3WaySort {
    // This class should not be instantiated.
    private Modified3WaySort() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(String original, int[] a) {
        //StdRandom.shuffle(a);
        sort(original, a, 0, a.length - 1);
        assert isSorted(original, a);
    }

    // quicksort the subarray a[lo .. hi] using 3-way partitioning
    private static void sort(String original, int[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        int v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = less(original, a[i], v , 0);
            if      (cmp < 0) exch(a, lt++, i++);
            else if(cmp > 0) exch(a, i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(original, a, lo, lt-1);
        sort(original, a, gt+1, hi);
        assert isSorted(original, a, lo, hi);
    }



    /***********************************************************************
     *  Helper sorting functions
     ***********************************************************************/

    // is v < w ?
    private static int less(String original, int v, int w, int extra) {
        if(extra == original.length()) return 0;
        if( v >= original.length() ) v = 0;
        if( w >= original.length() ) w = 0;
        if(original.charAt(v) < original.charAt(w)) return -1;
        else if(original.charAt(v) > original.charAt(w)) return 1;
        else {
            return less(original, v + 1 ,w + 1, extra + 1);
        }
        //return false;
    }


    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    /***********************************************************************
     *  Check if array is sorted - useful for debugging
     ***********************************************************************/
    private static boolean isSorted(String orgiginal, int[] a) {
        return isSorted(orgiginal, a, 0, a.length - 1);
    }

    private static boolean isSorted(String original, int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(original, a[i], a[i-1] , 0) < 0) return false;
        return true;
    }



    // print array to standard output
    private static void show(int[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; 3-way
     * quicksorts them; and prints them to standard output in ascending order.
     */
    public static void main(String[] args) {
        //char[] original = {'0', '1', '1', '0', '1', '1', '0', '0', '1', '0'};
        String original = "0110110010";
        int[] a = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Modified3WaySort.sort(original, a);
        show(a);
    }
}
