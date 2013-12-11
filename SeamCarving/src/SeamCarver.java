import java.util.ArrayList;
import java.util.Iterator;


/**
 *  @author huangxf
 */
class MyAcyclicSP {
    private double[] distTo;         // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;   // edgeTo[v] = last edge on shortest s->v path


    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
     * the directed acyclic graph <tt>G</tt>.
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public MyAcyclicSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // visit vertices in toplogical order
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // relax edge e
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     *    <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     *    <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
     *    as an iterable of edges, and <tt>null</tt> if no such path
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }


}


/**
 * Created by Administrator on 13-12-9.
 */
public class SeamCarver {

    private Picture picture;
    private final double borderEnergy = 195075.0;
    private double[][] energyMatrix = null;
    private double[][] reservEnergyMatrix = null;
    private boolean vertical = true; //flag
    private int width = 0, height = 0;

    //private

    public SeamCarver(Picture picture) {
        this.picture = picture;
        width = width();
        height = height();
        energyMatrix = SCUtility.toEnergyMatrix(this);

    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width  of current picture
    public     int width() {
        return picture.width();
    }

    // height of current picture
    public     int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y in current picture
    public  double energy(int x, int y) {
        //check if pixel is in range of graph
        if(x < 0 || x > width -1 || y < 0 || y > height)
            throw new IndexOutOfBoundsException();
        //check if pixel is a border pixel
        if(x == 0 || x == width - 1 || y == 0 || y == height -1) {
            return borderEnergy;
        }
        double energy = 0.0;
        double deltaXSqr = Math.pow((picture.get(x+1,y).getRed() - picture.get(x-1,y).getRed()),2.0)
                + Math.pow((picture.get(x+1,y).getGreen() - picture.get(x-1,y).getGreen()),2.0)
                + Math.pow((picture.get(x+1,y).getBlue() - picture.get(x-1,y).getBlue()),2.0);
        double deltaYsqr = Math.pow((picture.get(x,y+1).getRed() - picture.get(x,y-1).getRed()),2.0)
                + Math.pow((picture.get(x,y+1).getGreen() - picture.get(x,y-1).getGreen()),2.0)
                + Math.pow((picture.get(x,y+1).getBlue() - picture.get(x,y-1).getBlue()),2.0);
        energy = deltaXSqr + deltaYsqr;
        return energy;
    }

    // sequence of indices for horizontal seam in current picture
    public   int[] findHorizontalSeam() {
        EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(this.width * this.height);
        for(int y = 0; y< this.height; y++) {
            for(int x = 0; x< this.width; x++) {
                DirectedEdge edge;
                if(x+1 <= this.width - 1) {
                    edge = new DirectedEdge(y*this.width + x, y * this.width + x + 1, energyMatrix[x][y]);
                    digraph.addEdge(edge);
                    if(y - 1 >= 0) {
                        edge = new DirectedEdge(y*this.width + x, (y - 1) * this.width + x + 1, energyMatrix[x][y]);
                        digraph.addEdge(edge);
                    }
                    if(y + 1 <= this.height -1) {
                        edge = new DirectedEdge(y*this.width + x , (y + 1) * this.width + x + 1, energyMatrix[x][y]);
                        digraph.addEdge(edge);
                    }
                }
            }
        }

        int minPosY1 = 0, minPosY2 = 0;
        double minLength = Double.MAX_VALUE;

        double percentage = 0.0;

        for(int y1 = 0; y1< this.height; y1++) {
            for(int y2 = 0; y2< this.height; y2++) {
                //System.out.println((++percentage) / (height * width) * 100 + "% completed!");
                double curLength = 0.0;
                AcyclicSP sp = new AcyclicSP(digraph, y1 * width);
                curLength = sp.distTo(y2 * width + width -1);
                System.out.println("caculate energy sum (0,"+ y1 +")->("+width + ","+y2+"):" + curLength);
                if(minLength > curLength) {
                    minPosY1 = y1;
                    minPosY2 = y2;
                    minLength = curLength;
                }
            }
        }

        System.out.println("Picked (0,"+ minPosY1 +")->("+width + ","+minPosY2+"):" + minLength);

        AcyclicSP sp = new AcyclicSP(digraph, minPosY1 * width);
        Iterable it = sp.pathTo(minPosY2 * width + width -1);

        ArrayList<Integer> list = new ArrayList<Integer>();
        Iterator<DirectedEdge> itor= it.iterator();

        while(itor.hasNext()) {
            list.add(itor.next().from());
        }

        int[] result = new int[list.size() + 1];
        for(int i = 0; i<list.size(); i++) {
            result[i] = list.get(i) / width;
        }
        result[list.size()] = minPosY2 / width;
        //list.toArray(result);

        return result;
        //return null;
    }

    // sequence of indices for vertical   seam in current picture
    public   int[] findVerticalSeam() {

        EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(this.width * this.height);
        for(int x = 0; x< this.width; x++) {
            for(int y = 0; y< this.height; y++) {
                DirectedEdge edge;
                if(y+1 <= this.height - 1) {
                    edge = new DirectedEdge(y*this.width + x, (y+1) * this.width + x, energyMatrix[x][y]);
                    digraph.addEdge(edge);
                    if(x - 1 >= 0) {
                        edge = new DirectedEdge(y*this.width + x, (y+1) * this.width+x-1, energyMatrix[x][y]);
                        digraph.addEdge(edge);
                    }
                    if(x + 1 <= this.width -1) {
                        edge = new DirectedEdge(y*this.width + x , (y+1) * this.width + x + 1, energyMatrix[x][y]);
                        digraph.addEdge(edge);
                    }
                }
            }
        }

        int minPosX1 = 0, minPosX2 = 0;
        double minLength = Double.MAX_VALUE;

        for(int x1 = 0; x1< this.width; x1++) {
            for(int x2 = 0; x2< this.width; x2++) {
                double curLength = 0.0;
                MyAcyclicSP sp = new MyAcyclicSP(digraph, x1);
                curLength = sp.distTo((height - 1) * width + x2);
                //System.out.println("caculate energy sum:" + curLength);
                if(minLength > curLength) {
                    minPosX1 = x1;
                    minPosX2 = x2;
                    minLength = curLength;
                }
            }
        }

        MyAcyclicSP sp = new MyAcyclicSP(digraph, minPosX1);
        Iterable it = sp.pathTo(minPosX2 + width * (height -1));

        ArrayList<Integer> list = new ArrayList<Integer>();
        Iterator<DirectedEdge> itor= it.iterator();

        while(itor.hasNext()) {
            list.add(itor.next().from());
        }

        int[] result = new int[list.size() + 1];
        for(int i = 0; i<list.size(); i++) {
            result[i] = list.get(i) % width;
        }
        result[list.size()] = minPosX2 % width;
        //list.toArray(result);

        return result;
        //return null;
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] a) {

    }

    // remove vertical   seam from current picture
    public    void removeVerticalSeam(int[] a) {

    }
}
