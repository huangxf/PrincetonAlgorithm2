/**
 * Created with IntelliJ IDEA.
 * User: huangxf
 * Date: 13-12-12
 * Time: 3:17 pm
 * To change this template use File | Settings | File Templates.
 */
public class TopologicalSort {

    private boolean[] marked;
    private Stack<Integer> stack = new Stack<Integer>();

    public TopologicalSort(int w, int h) {
        marked = new boolean[h*w+2];
        for(int i = 0; i<marked.length; i++){
            if(!marked[i]){
                dfs(i, h, w);
            }
        }
    }


    private void dfs(int i, int h, int w) {
        marked[i] = true;
        for(int adj:Utils.adj(w, h , i, true)){
            if(adj != -1 && !marked[adj]){
                dfs(adj, h, w);
            }
        }
        stack.push(i);
    }

    public static void main(String[] args) {
        System.out.println(new TopologicalSort(3, 7).stack);
    }


    public Iterable<Integer> order() {
        return stack;
    }
}
