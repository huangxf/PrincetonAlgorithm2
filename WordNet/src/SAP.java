import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Administrator on 13-12-5.
 */
public class SAP {

    private Digraph diagraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.diagraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        //int ancestor = ancestor(v,w);
        //if(ancestor == -1) return -1;

        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(diagraph, w);
        int length = Integer.MAX_VALUE;

        for (int i = 0; i < this.diagraph.V(); i++) {
            int curLength = Integer.MAX_VALUE;
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                curLength = bfdpV.distTo(i) + bfdpW.distTo(i);
            }
            if (length > curLength) {
                length = curLength;
            }
        }

        if (length == Integer.MAX_VALUE) {
            return -1;
        } else {
            return length;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int pathLength = 1000000;
        int ancestor = -1;

        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(diagraph, w);


        for (int i = 0; i < diagraph.V(); i++) {
            int length = Integer.MAX_VALUE;
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                length = bfdpV.distTo(i) + bfdpW.distTo(i);
            }
            if (pathLength > length) {
                pathLength = length;
                ancestor = i;
            }
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int length = -1;

        //int ancestor = -1;
        int minLength = Integer.MAX_VALUE;

        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(diagraph, w);

        for (int i = 0; i < diagraph.V(); i++) {
            int curLength = Integer.MAX_VALUE;
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                curLength = bfdpV.distTo(i) + bfdpW.distTo(i);
            }
            if (minLength > curLength) {
                minLength = curLength;
            }
        }

        if (minLength != Integer.MAX_VALUE) {
            length = minLength;
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = -1;
        int minLength = Integer.MAX_VALUE;

        BreadthFirstDirectedPaths bfdpV = new BreadthFirstDirectedPaths(diagraph, v);
        BreadthFirstDirectedPaths bfdpW = new BreadthFirstDirectedPaths(diagraph, w);

        for (int i = 0; i < diagraph.V(); i++) {
            //int curAncestor = -1;
            int curLength = Integer.MAX_VALUE;
            if (bfdpV.hasPathTo(i) && bfdpW.hasPathTo(i)) {
                curLength = bfdpV.distTo(i) + bfdpW.distTo(i);
            }
            if (minLength > curLength) {
                ancestor = i;
                minLength = curLength;
            }
        }

        return ancestor;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        //testGroup(sap);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

//    private static void testGroup(SAP sap) {
//        ArrayList<Integer> v = new ArrayList<>();
//        v.add(new Integer(10));
//        ArrayList<Integer> w = new ArrayList<>();
//        w.add(new Integer(7));
//        w.add(new Integer(8));
//        System.out.println(sap.ancestor(v, w));
//    }
}
