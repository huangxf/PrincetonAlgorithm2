/**
 * Created by huangxf on 13-12-7.
 */
public class Outcast {

    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxLength = 0;
        String maxString = null;
        for (String noun1 : nouns) {
            int disNous1 = 0;
            for (String noun2 : nouns) {
                if (noun1.equals(noun2)) continue;
                //System.out.println(noun1+"->"+noun2+" distance accum = " + disNous1);
                disNous1 += wordnet.distance(noun1, noun2);
            }
            //System.out.print(noun1+":"+disNous1);
            if (maxLength < disNous1) {
                maxLength = disNous1;
                maxString = noun1;
            }
        }
        return maxString;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            @SuppressWarnings("deprecation")
            String[] nouns = In.readStrings(args[t]);
            System.out.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
