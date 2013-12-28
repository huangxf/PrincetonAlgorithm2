/*************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *
 *  A string symbol table for ASCII strings, implemented using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *************************************************************************/

public class ModifiedTrieST {
    private static final int R = 26;        // extended ASCII

    private Node root = new Node();

    private static class Node {
        private boolean val = false;
        private Node[] next = new Node[R];
    }

    /****************************************************
     * Is the key in the symbol table?
     ****************************************************/
    public boolean contains(String key) {
        Node x = get(key);
        if(x != null )
            return get(key).val;
        else
            return false;
    }

    public Node get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = (char)(key.charAt(d) - 'A');
        return get(x.next[c], key, d+1);
    }

    public int hasPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        if(x == null)
            return -1; //nothing found
        else if(x.val == true)
            return 0; //valid word
        else
            return 1; //prefix
    }

    /****************************************************
     * Insert key-value pair into the symbol table.
     ****************************************************/
    public void put(String key) {
        root = put(root, key,  0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = true;
            return x;
        }
        char c = (char)(key.charAt(d) - 'A');
        x.next[c] = put(x.next[c], key, d+1);
        return x;
    }
}
