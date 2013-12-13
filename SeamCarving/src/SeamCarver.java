import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;



/**
 * Created by Administrator on 13-12-9.
 */
public class SeamCarver {

    private double[][] energyMatrix = null;
    private int[][] pictureMatrix = null;
//    private double[][] cachedEnergyMatrix;
//    private int[][] cachedPictureMatrix;

    //private
    private void toPictrueMatrix(Picture picture) {

        if(picture == null) {
            return;
        }

        pictureMatrix = new int[picture.width()][picture.height()];
        for(int y = 0; y < picture.height(); y ++) {
            for(int x = 0; x< picture.width(); x++) {
                pictureMatrix[x][y] = picture.get(x,y).getRGB();
            }
        }
    }

    private void cacluateEnergy() {
//        cachedEnergyMatrix = null;
//        cachedPictureMatrix = null;
        double[][] newEnergyMatrix = new double[pictureMatrix.length][pictureMatrix[0].length];
        for(int y = 0; y < pictureMatrix[0].length; y ++) {
            for(int x = 0; x< pictureMatrix.length; x++) {
                newEnergyMatrix[x][y] = energy(x,y);
            }
        }
        this.energyMatrix = newEnergyMatrix;
    }

    public SeamCarver(Picture picture) {
        toPictrueMatrix(picture);
        energyMatrix = SCUtility.toEnergyMatrix(this);

    }

    // current picture
    public Picture picture() {
//        cachedEnergyMatrix = null;
//        cachedPictureMatrix = null;
        if(pictureMatrix == null)
            return null;
        Picture newPic = new Picture(pictureMatrix.length, pictureMatrix[0].length);
        for(int y = 0; y < pictureMatrix[0].length; y++) {
            for(int x = 0; x < pictureMatrix.length; x++) {
                newPic.set(x, y, new Color(pictureMatrix[x][y]));
            }
        }
        return newPic;
    }

    // width  of current picture
    public     int width() {
        return this.pictureMatrix.length;
    }

    // height of current picture
    public     int height() {
        return this.pictureMatrix[0].length;
    }

    // energy of pixel at column x and row y in current picture
    public  double energy(int x, int y) {
        if(pictureMatrix == null) return 0;
        int width = pictureMatrix.length;
        int height = pictureMatrix[0].length;
        //check if pixel is in range of graph
        if(x < 0 || x > width -1 || y < 0 || y > height - 1)
            throw new IndexOutOfBoundsException();
        //check if pixel is a border pixel
        if(x == 0 || x == width - 1 || y == 0 || y == height -1) {
            return 195075.0;
        }
        double energy = 0.0;
        Color c1 = new Color(pictureMatrix[x+1][y]);
        Color c2 = new Color(pictureMatrix[x-1][y]);
        Color c3 = new Color(pictureMatrix[x][y+1]);
        Color c4 = new Color(pictureMatrix[x][y-1]);
        double deltaXSqr = Math.pow((c1.getRed() - c2.getRed()),2.0)
                + Math.pow((c1.getGreen() - c2.getGreen()),2.0)
                + Math.pow((c1.getBlue() - c2.getBlue()),2.0);
        double deltaYsqr = Math.pow((c3.getRed() - c4.getRed()),2.0)
                + Math.pow((c3.getGreen() - c4.getGreen()),2.0)
                + Math.pow((c3.getBlue() - c4.getBlue()),2.0);
        energy = deltaXSqr + deltaYsqr;
        return energy;
    }

    private void transpose() {
        if(this.energyMatrix == null)
            return;

            double[][] transposed = new double[energyMatrix[0].length][energyMatrix.length]; //create an 2d array and swap dimensions for transpose
            int[][] colorTranposed = new int[energyMatrix[0].length][energyMatrix.length];
            for(int y = 0; y < energyMatrix.length; y++) {
                for(int x = 0; x < energyMatrix[0].length; x++) {
                    //transposed[x][y] = energyMatrix[y][height - x -1];
                    transposed[x][y] = energyMatrix[y][x];
                    colorTranposed[x][y] = pictureMatrix[y][x];
                }
            }

        //swap cached memory
        this.energyMatrix = transposed;
        this.pictureMatrix = colorTranposed;
    }

    // sequence of indices for horizontal seam in current picture
    public   int[] findHorizontalSeam() {
        if(this.energyMatrix == null) return null;
        transpose();
        MyESP esp = new MyESP(this.energyMatrix);
        int[] result = esp.getPath();
        transpose();
        //list.toArray(result);

        return result;
        //return null;
    }

    // sequence of indices for vertical   seam in current picture
    public   int[] findVerticalSeam() {
        if(this.energyMatrix == null) return null;
        MyESP esp = new MyESP(energyMatrix);
        int[] result = esp.getPath();
        return result;
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] a) {
        transpose();
        removeVerticalSeam(a);
        transpose();
    }

    // remove vertical   seam from current picture
    public    void removeVerticalSeam(int[] a) {

        if(a == null) {
            throw new IllegalArgumentException();
        }

        if(energyMatrix == null || this.pictureMatrix == null)
            throw new IndexOutOfBoundsException ();

        if(a.length != energyMatrix[0].length)
            throw new IllegalArgumentException();

        if(energyMatrix.length == 1) {
            this.energyMatrix = null;
            this.pictureMatrix = null;
            return;
        }

        double[][] removed = new double[energyMatrix.length-1][energyMatrix[0].length];
        int[][] colorRemoved = new int[pictureMatrix.length-1][pictureMatrix[0].length];
        for(int y = 0; y < energyMatrix[0].length; y++) {
            for(int x = 0; x < energyMatrix.length-1; x++) {
                if(x < a[y]) {
                    removed[x][y] = energyMatrix[x][y];
                    colorRemoved[x][y] = pictureMatrix[x][y];
                } else {
                    removed[x][y] = energyMatrix[x+1][y];
                    colorRemoved[x][y] = pictureMatrix[x+1][y];
                }
            }
        }
        this.energyMatrix = removed;
        this.pictureMatrix = colorRemoved;
        cacluateEnergy();
     }

    private static void print(double[][] matrix) {
        for(int y = 0; y < matrix[0].length; y ++) {
            for(int x = 0; x < matrix.length; x++) {
                System.out.print(matrix[x][y] + " ");
            }
            System.out.println("");
        }
    }
}
