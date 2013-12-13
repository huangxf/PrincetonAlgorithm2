import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: huangxf
 * Date: 13-12-12
 * Time: 1:32 pm
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    public static int[] adj(int width, int height, int vertex, boolean isVertical) {


        if(vertex < 0 || vertex > width * height + 1)
            throw new IllegalArgumentException();

        if(vertex == width * height) {
            if(isVertical) {
                int[] firstRow = new int[width];
                for(int i = 0; i< width; i++) {
                    firstRow[i] = i;
                }
                return firstRow;
            } else {
                int[] firstColumn = new int[height];
                for(int i = 0; i< height; i++) {
                    firstColumn[i] = i * width;
                }
                return firstColumn;
            }
        }

        if(vertex > width * height) {
            int[] result = new int[1];
            result[0] = -1;
            return result;
        }



        int[] result = new int[3];
        Arrays.fill(result,-1);

        int x = vertex % width; //find cloumn number
        int y = vertex / width; //find row number

        if(isVertical) {
            if(y + 1 <= height -1) {
                result[1] = (x + width * (y + 1));
                if(x - 1 >= 0) {
                    result[0] = (x -1 + width * (y + 1));
                }
                if(x + 1 <= width - 1) {
                    result[2] = (x + 1 + width * (y + 1));
                }
            }
            if(y == height -1) {
                result = new int[1];
                result[0] = width * height + 1;
            }
        } else {
            if(x + 1 <= width -1) {
                result[1] = (x + 1 + width * y);
                if(y - 1 >= 0) {
                    result[0] = (x + 1 + width * (y-1));
                }
                if(y + 1 <= height -1) {
                    result[2] = (x + 1 + width * (y + 1));
                }
            }
            if(x == width - 1) {
                result = new int[1];
                result[0] = 0;
            }
        }

        return result;
    }

    public static void print(int[] array) {
        for(int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        print(adj(3, 4, 0, false));
        print(adj(3, 4, 6, false));
        print(adj(3, 4, 9, false));
        print(adj(3, 4, 8, false));
        print(adj(3, 4, 12, false));
        print(adj(3, 4, 13, false));
    }
}
