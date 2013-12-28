import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Administrator on 13-12-20.
 */


public class BoggleSolver
{
    private ModifiedTrieST dict;
    //BoggleBoard board;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        //this.dictionary = dictionary;
        dict = new ModifiedTrieST();
        for(int i = 0; i < dictionary.length; i++) {
            this.dict.put(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> result = new HashSet<String>();
        for(int i = 0; i < board.rows(); i++) {
            for(int j = 0; j < board.cols(); j++) {
                boolean[] marks = new boolean[board.cols() * board.rows()];
                Arrays.fill(marks, false);
                char[] substring = new char[2 * board.cols() * board.rows()]; //consider each char could be Q(u)
                Arrays.fill(substring, (char)0);
                dfs(board, i, j, result, 0, substring, marks);
            }
        }
        return result;
    }

    private void dfs(BoggleBoard board, int i, int j, Set<String> result, int index, char[] substring, boolean[] marks) {
        if(i < 0 || i > board.rows() - 1) return;
        if(j < 0 || j > board.cols() - 1) return;
        boolean[] localmarks = marks.clone();
        if(localmarks[i * board.cols() + j] == true) return;
        else localmarks[i * board.cols() + j] = true;
        char ch = board.getLetter(i, j);

        substring[index] = ch;
        if(ch == 'Q') {
            substring[index+1] = 'U';
        }

        String curWord = new String(substring, 0, (ch == 'Q')?(index + 2):(index + 1));

        /*****************************************************
         * DO NOT USER contains method of TrieST and use hasPrefix
         * Because contains will search ST for once and hasPrefix
         * will search ST again. It will be twice searching if contains and hasPrefix
         * both called.
         */

        //System.out.println(curWord);
//        if(dict.contains(curWord) ) {
//            if(curWord.length() >= 3) {
//                result.add(curWord);
//                //System.out.println(curWord);
//            }
//        }
        //System.out.println("testing:" + curWord);
        //if(ch != substring[(index == 0) ? 0 : (index - 1)])
        int flag = dict.hasPrefix(curWord);
            //if(!dict.hasPrefix(curWord))
        if(flag == -1) return;
        if(flag == 0) {
            if(curWord.length() >= 3) { result.add(curWord); }
        }


        int nextLevel = (ch == 'Q')?(index + 2):(index + 1);

        dfs(board, i + 1 , j, result, nextLevel, substring, localmarks);
        dfs(board, i - 1 , j, result, nextLevel, substring, localmarks);
        dfs(board, i , j + 1, result, nextLevel, substring, localmarks);
        dfs(board, i , j - 1, result, nextLevel, substring, localmarks);
        dfs(board, i + 1 , j - 1, result, nextLevel, substring, localmarks);
        dfs(board, i + 1 , j + 1, result, nextLevel, substring, localmarks);
        dfs(board, i - 1 , j - 1, result, nextLevel, substring, localmarks);
        dfs(board, i - 1 , j + 1, result, nextLevel, substring, localmarks);
        return;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if(word == null) throw new IllegalArgumentException();
        if(!dict.contains(word)) return 0;
        int length = word.length();
        int score = 0;
        if(length <= 2) score = 0;
        if(length >= 3 && length <= 4 ) score = 1;
        if(length == 5) score = 2;
        if(length == 6) score = 3;
        if(length == 7) score = 5;
        if(length >= 8) score = 11;
        return score;
    }

    public static void main(String[] args)
    {
        long time1 = System.currentTimeMillis();
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
//        String word = "SIT";
//        System.out.println(solver.dict.contains(word));
//        System.out.println(solver.dict.hasPrefix(word));
        for (String word : solver.getAllValidWords(board))
    {
        //StdOut.println(word);
        score += solver.scoreOf(word);
    }
        StdOut.println("Score = " + score);
        System.out.println(System.currentTimeMillis() - time1);
   }
}