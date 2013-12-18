import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 13-12-17.
 */
public class BaseballElimination {

    private FlowNetwork stFlow = null;
    private int N; //number of teams
    private int win[] = null;
    private int lose[] = null;
    private int rest[] = null;
    private int relation[][] = null;
    private ArrayList<String> teams = null;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        String line = "";
        line = in.readLine();
        if(line == null) throw new IllegalArgumentException("Cannot read the number of teams");
        this.N = Integer.parseInt(line);

        //Init Matrix
        this.win = new int[N];
        this.lose = new int[N];
        this.rest = new int[N];
        this.relation = new int[N][N];

        this.teams = new ArrayList<>(N);

        for(int i = 0; line != null; i++) {
            line = in.readLine();
            proceedLine(line, i);
        }


    }


    private void constructFlow(String team) {
        int teamNum = this.teams.indexOf((String)team);
        int nodeNum = N - 1  + (N-1)*(N-2)/2 + 2; //vertices s and t, game vertices 1 for C(4,2), team vertices for N-1
        this.stFlow = new FlowNetwork(nodeNum);
        int index = 0;
        //stFlow[0]: s stFlow[Max-1]: t (source and sink)
        //stFlow[1 to (N-1)*(N-2)/2)]: team vs team other than team x (game vertices)
        //stFlow[(N-1)*(N-2) + 1 to (N-1)*(N-2)/2 + N]: team 1 to team N (team vertices)
        for(int i = 0; i < N; i++) {
            if(i == teamNum) continue;
            for(int j = i + 1; j < N; j++) {
                if(j == teamNum) continue;
                //System.out.println("match pait["+i+","+j+"]");
                FlowEdge edgeLayer1 = new FlowEdge(0, index + 1 , relation[i][j]);
                FlowEdge edgeI = new FlowEdge(index + 1 , (N-1)*(N-2)/2 + ((i > teamNum)?(i-1):i) + 1, Double.POSITIVE_INFINITY); //((i > teamNum)?(i-1):i) because teamNum is skipped
                FlowEdge edgeJ = new FlowEdge(index + 1 , (N-1)*(N-2)/2 + ((j > teamNum)?(j-1):j) + 1, Double.POSITIVE_INFINITY);
                stFlow.addEdge(edgeLayer1);
                stFlow.addEdge(edgeI);
                stFlow.addEdge(edgeJ);
                index++;
            }
        }

        for(int k = 0; k < N; k++) {
            if(k == teamNum) continue;
            FlowEdge edge = new FlowEdge((N-1)*(N-2)/2 + ((k > teamNum)?k-1:k) + 1, stFlow.V() - 1, win[teamNum] + rest[teamNum] - win[k]);
            stFlow.addEdge(edge);
        }


    }

    private int skipSpace(String[] datas, int curIndex) {
        if(datas == null || datas.length == 0) return 0;
        while("".equals(datas[curIndex]) && curIndex < datas.length) curIndex++;
        return curIndex;
    }

    private void proceedLine(String line, int i) {
        if(line == null) return;
        line = line.trim();
        String[] datas = line.split(" ");
        int index = 0;
        this.teams.add(datas[0]);

        index = skipSpace(datas,++index);

        this.win[i] = Integer.parseInt(datas[index]);
        index = skipSpace(datas,++index);
        this.lose[i] = Integer.parseInt(datas[index]);
        index = skipSpace(datas,++index);
        this.rest[i] = Integer.parseInt(datas[index]);


        //index = skipSpace(datas,++index);

        for(int k = 0; k < N; k ++) {
            index = skipSpace(datas,++index);
            this.relation[i][k] = Integer.parseInt(datas[index]);
        }
    }

    // number of teams
    public              int numberOfTeams() {

        return N;
    }

    // all teams
    public Iterable<String> teams() {

        return this.teams;
    }

    // number of wins for given team
    public              int wins(String team) {
        int i = teams.indexOf((String)team);
        return win[i];
    }

    // number of losses for given team
    public              int losses(String team) {
        int i = teams.indexOf((String)team);
        return lose[i];
    }

    // number of remaining games for given team
    public              int remaining(String team) {
        int i = teams.indexOf((String)team);
        return rest[i];
    }

    public              int against(String team1, String team2) {
        int i = teams.indexOf((String)team1);
        int j = teams.indexOf((String)team2);
        return relation[i][j];
    }

    private int isTrivial(int index) {
        for(int i = 0; i < N; i++) {
            if(i == index) continue;
            if(win[index] + rest[index] < win[i]) return i;
        }
        return -1;
    }

    // is given team eliminated?
    public          boolean isEliminated(String team) {
        int index = teams.indexOf((String)team);
        int potential = win[index] + rest[index];
        if(isTrivial(index) != -1) return true; //Trivial elimination
        int mutualMatch = 0;

        for(int i = 0; i < N; i++) {
            if(i == index) continue;
            for(int j = i; j < N; j++) {
                if(j == index) continue;
                mutualMatch += relation[i][j];
            }
        }

        constructFlow(team);
        FordFulkerson ff = new FordFulkerson(stFlow, 0 , stFlow.V() - 1);
        double maxFlow = ff.value();

        if(mutualMatch > maxFlow) {
            return true;
        }else {
            return false;
        }
    }



    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int index = teams.indexOf((String)team);
        ArrayList<String> certificate = new ArrayList<String>();
        int trivial = isTrivial(index);
        if(trivial != -1) { //Trivial elimination
            certificate.add(teams.get(trivial));
            return certificate;
        }
        constructFlow(team);
        FordFulkerson ff = new FordFulkerson(stFlow, 0 , stFlow.V() - 1);
//        for(int i = 0; i < stFlow.V(); i++) {
//            System.out.println(i+":"+ff.inCut(i));
//        }
//        System.out.println("Max-flow:" + ff.value());
        for(int i = 1 + (N-1)*(N-2) / 2 , j = 0; i < stFlow.V(); i++, j++) {
            if(ff.inCut(i)) certificate.add(teams.get( j ));
        }

        if(certificate.size() == 0) return null;

        return certificate;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        //division.certificateOfElimination("Detroit");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
