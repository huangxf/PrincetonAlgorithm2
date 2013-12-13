import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: huangxf
 * Date: 13-12-12
 * Time: 1:24 pm
 * To change this template use File | Settings | File Templates.
 */
public class MyESP {

    private double[] distTo;         // distTo[v] = distance  of shortest s->v path
    private int[] edgeTo;
    private int width;
    private int height;
    private static double[][] energyMatrix;  // user static to save memory

    public MyESP(double[][] matrix) {

        this.energyMatrix = matrix;

        width = energyMatrix.length;
        height = energyMatrix[0].length;

        distTo = new double[width * height + 2];
        edgeTo = new int[width * height + 2];
        Arrays.fill(distTo, Double.POSITIVE_INFINITY);
        distTo[width * height] = 0.0;

        TopologicalSort topological = new TopologicalSort(width, height);
        for (int topoVertex : topological.order()) {
            for (int adj : Utils.adj(width, height, topoVertex, true)) {
                if(adj != -1) relax(topoVertex, adj);
            }
        }
    }

    public void relax(int topoVertex, int adj) {
        //System.out.print("relax:" + topoVertex + "->" + adj);
        int x = adj % width;
        int y = adj / width;

        double weight = 0;

        if(adj != width * height && adj != width * height + 1)
            weight = energyMatrix[x][y];

        if (distTo[adj] > distTo[topoVertex] + weight) {
            //System.out.print(" update " + adj + " distTo from " + distTo[adj] + " to ");
            distTo[adj] = distTo[topoVertex] + weight;
            edgeTo[adj] = topoVertex;
            //System.out.print(distTo[adj]);
        }
          
        //System.out.println("");
    }

    public double distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public int[] getPath() {
        if(hasPathTo(width * height + 1)) {
            int[] path = new int[height];
            int count = height - 1;
            for(int v = width * height+1; v != width * height; v = edgeTo[v]) {
                if(v >= width * height) continue; //if the vertex is virtual source or sink, do not store them in path
                path[count] = v % width;
                count --;
                //if(v < width * height)
                    //System.out.print("["+ v + "]->");
            }
            //System.out.println("");
            return path;
        } else
            return null;
    }


}
