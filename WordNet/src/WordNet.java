import java.util.*;

/**
 * Created by Administrator on 13-12-5.
 */
public class WordNet {

    private class SynSets {

        private String id;
        private String[] synsets;
        private String explain;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String[] getSynsets() {
            return synsets;
        }

        public void setSynsets(String syn) {
            if (syn == null) this.synsets = null;
            this.synsets = syn.split(" ");
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }
    }

//    private class Hypernyms {
//
//        private String synset;
//        private String hypernym1;
//        private String hypernym2;
//
//        public String getHypernym2() {
//            return hypernym2;
//        }
//
//        public void setHypernym2(String hypernym2) {
//            this.hypernym2 = hypernym2;
//        }
//
//        public String getHypernym1() {
//            return hypernym1;
//        }
//
//        public void setHypernym1(String hypernym1) {
//            this.hypernym1 = hypernym1;
//        }
//
//        public String getSynset() {
//            return synset;
//        }
//
//        public void setSynset(String synset) {
//            this.synset = synset;
//        }
//    }

    private ArrayList<SynSets> synSetsList = new ArrayList<SynSets>();
    //private ArrayList<Hypernyms> hypernymsList = new ArrayList<Hypernyms>();
    private HashSet<String> nouns = new HashSet<String>();
    private HashMap<String, ArrayList<Integer>> dictionary = new HashMap<String, ArrayList<Integer>>();
    private HashMap<Integer, String> idToSynSetsDict = new HashMap<Integer, String>();
    private Digraph wordNet = null;




    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
        String line = "";
        while (line != null) {
            line = in.readLine();
            if (line == null) continue;
            //split
            String[] elements = line.split(",");
            //Parse 3 parts
            SynSets syn = new SynSets();
            syn.setId(elements[0]);
            syn.setSynsets(elements[1]);
            syn.setExplain(elements[2]);


            idToSynSetsDict.put(new Integer(elements[0]), elements[1]);

            if (elements != null) {
                for (String noun : elements[1].split(" ")) {
                    nouns.add(noun);
                    //dictionary.put(noun ,new Integer(elements[0]));

                    if (dictionary.containsKey(noun)) {
                        dictionary.get(noun).add(new Integer(elements[0]));
                    } else {
                        ArrayList<Integer> idlist = new ArrayList<Integer>();
                        idlist.add(new Integer(elements[0]));
                        dictionary.put(noun, idlist);
                    }


//                    if(idToSynSetsDict.containsKey(new Integer(elements[0]))) {
//                        idToSynSetsDict.get(new Integer(elements[0])).add(noun);
//                    } else {
//                        ArrayList<String> synlist = new ArrayList<String>();
//                        synlist.add(noun);
//                        idToSynSetsDict.put(new Integer(elements[0]), synlist);
//                    }
                }
            }

            synSetsList.add(syn);
        }

        wordNet = new Digraph(synSetsList.size());

        in = new In(hypernyms);
        line = "";
        while (line != null) {
            line = in.readLine();
            if (line == null) continue;
            //System.out.println(line);
            //split
            String[] elements = line.split(",");
//            //Parse 3 parts
//            Hypernyms hyp = new Hypernyms();
//            hyp.setSynset(elements[0]);
//            hyp.setHypernym1(elements[1]);
//            hyp.setHypernym2(elements[2]);
//            hypernymsList.add(hyp);
            for (int i = 1; i < elements.length; i++) {
                wordNet.addEdge(Integer.parseInt(elements[0]), Integer.parseInt(elements[i]));
                //wordNet.addEdge(Integer.parseInt(elements[0]), Integer.parseInt(elements[2]));
            }
        }

        //check if the graph is a DAG rooted
        int RootCount = 0;
        for(int i = 0; i< wordNet.V(); i++) {
            Iterable<Integer> it = wordNet.adj(i);
            //Only the root has no adjacent vertices.
            //And in DAG, There is only one root allowed.
            if(!it.iterator().hasNext()) RootCount ++;
        }
        if(RootCount > 1)
            throw new IllegalArgumentException();

        DirectedCycle cycleFinder = new DirectedCycle(wordNet);
        if(cycleFinder.hasCycle())
            throw new IllegalArgumentException();

        sap = new SAP(wordNet);

    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
//        Set<String> nounsSet = new TreeSet<String>();
//        Iterator<SynSets> it = this.synSetsList.iterator();
//        while(it.hasNext()) {
//            String[] item = ((SynSets)it.next()).getSynsets();
//            if(item != null) {
//                for(int i = 0; i< item.length; i++) {
//                    nounsSet.add(item[i]);
//                }
//            }
//        }
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
//        Set<String> nounsSet = (Set<String>)nouns();
//        if(nounsSet.contains(word)) {
//            return true;
//        }
        if (nouns.contains(word)) return true;
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (isNoun(nounA) && isNoun(nounB)) {
            SAP sap = new SAP(this.wordNet);
            ArrayList<Integer> vA = dictionary.get(nounA);
            ArrayList<Integer> vB = dictionary.get(nounB);
            return sap.length(vA, vB);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        //SAP sap = new SAP(this.wordNet);
        ArrayList<Integer> vA = dictionary.get(nounA);
        ArrayList<Integer> vB = dictionary.get(nounB);
        int ancestor = sap.ancestor(vA, vB);
        return idToSynSetsDict.get(new Integer(ancestor));
        //return "";
    }

    // for unit testing of this class
    public static void main(String[] args) {
        String synsetPath = args[0];
        String hypernymsPath = args[1];
        WordNet wordNet = new WordNet(synsetPath, hypernymsPath);
        System.out.println(wordNet.isNoun("fuck"));
    }
}
