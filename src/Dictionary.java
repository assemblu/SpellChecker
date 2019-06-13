import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.regex.Pattern;


public class Dictionary extends LoadingBar
{
    private File dictionaryFile;
    private File corpusFile;
    private Map<String, Integer> dictionaryMap;
    private ArrayList<String> dictionaryArray;

    public Dictionary(File dictionaryFile)
    {
        this.dictionaryFile = dictionaryFile;
    }

    public File getDictionaryFile()
    {
        return dictionaryFile;
    }

    public void setDictionaryFile(File dictionaryFile)
    {
        this.dictionaryFile = dictionaryFile;
    }

    public File getCorpusFile()
    {
        return corpusFile;
    }

    public void setCorpusFile(File corpusFile)
    {
        this.corpusFile = corpusFile;
    }

    public void setCorpusFile(String corpusFile)
    {
        this.corpusFile = new File(corpusFile);
    }

    public Map<String, Integer> getDictionaryMap() {
        return dictionaryMap;
    }

    public void setDictionaryMap(Map<String, Integer> dictionaryMap) {
        this.dictionaryMap = dictionaryMap;
    }

    public ArrayList<String> getDictionaryArray() {
        return dictionaryArray;
    }

    public void setDictionaryArray(ArrayList<String> dictionaryArray) {
//        this.isArray = true;
        this.dictionaryArray = dictionaryArray;
    }

    /*
    Example output:
        1,000 is the similarity between "" and ""
        0,100 is the similarity between "1234567890" and "1"
     */
//    public double similarity(String s1, String s2)
//    {
//        String longer = s1, shorter = s2;
//        if (s1.length() < s2.length())
//        { // longer should always have greater length
//            longer = s2; shorter = s1;
//        }
//        int longerLength = longer.length();
//        if (longerLength == 0) { return 1.0; }
//        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
//    }
//
//    public int editDistance(String s1, String s2)
//    {
//        s1 = s1.toLowerCase();
//        s2 = s2.toLowerCase();
//
//        int[] costs = new int[s2.length() + 1];
//        for (int i = 0; i <= s1.length(); i++)
//        {
//            int lastValue = i;
//            for (int j = 0; j <= s2.length(); j++)
//            {
//                if (i == 0)
//                    costs[j] = j;
//                else
//                {
//                    if (j > 0)
//                    {
//                        int newValue = costs[j - 1];
//                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
//                            newValue = Math.min(Math.min(newValue, lastValue),
//                                    costs[j]) + 1;
//                        costs[j - 1] = lastValue;
//                        lastValue = newValue;
//                    }
//                }
//            }
//            if (i > 0)
//                costs[s2.length()] = lastValue;
//        }
//        return costs[s2.length()];
//    }
//
//    public void printSimilarity(String s, String t) {
//        System.out.println(String.format(
//                "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
//        if(similarity(s, t) == 1.000)
//        {
//            System.out.println(s + "- correct");
//        }
//        else
//        {
//            System.out.println(s + "- incorrect");
//        }
//
//    }

    public final int distance(final String s1, final String s2) {

        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        // INFinite distance is the max possible distance
        int inf = s1.length() + s2.length();

        // Create and initialize the character array indices
        HashMap<Character, Integer> da = new HashMap<Character, Integer>();

        for (int d = 0; d < s1.length(); d++) {
            da.put(s1.charAt(d), 0);
        }

        for (int d = 0; d < s2.length(); d++) {
            da.put(s2.charAt(d), 0);
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        int[][] h = new int[s1.length() + 2][s2.length() + 2];

        // initialize the left and top edges of H
        for (int i = 0; i <= s1.length(); i++) {
            h[i + 1][0] = inf;
            h[i + 1][1] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            h[0][j + 1] = inf;
            h[1][j + 1] = j;

        }

        // fill in the distance matrix H
        // look at each character in s1
        for (int i = 1; i <= s1.length(); i++) {
            int db = 0;

            // look at each character in b
            for (int j = 1; j <= s2.length(); j++) {
                int i1 = da.get(s2.charAt(j - 1));
                int j1 = db;

                int cost = 1;
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cost = 0;
                    db = j;
                }

                h[i + 1][j + 1] = min(
                        h[i][j] + cost, // substitution
                        h[i + 1][j] + 1, // insertion
                        h[i][j + 1] + 1, // deletion
                        h[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1));
            }

            da.put(s1.charAt(i - 1), i);
        }

        return h[s1.length() + 1][s2.length() + 1];
    }

    private static int min(
            final int a, final int b, final int c, final int d) {
        return Math.min(a, Math.min(b, Math.min(c, d)));
    }

    public String spellCheck(String toCheck)
    {
        var count = 0;
        String[] words = toCheck.replaceAll("\\p{P}", "").split(" ");

        var sb = new StringBuilder();
        var pattern = Pattern.compile("\\w+");
        var matcher = pattern.matcher(toCheck);
        while(matcher.find()){
            var word = matcher.group();
            if(dictionaryMap.containsKey(word)){
                matcher.appendReplacement(sb, word);
            }else{
                for(var dictionaryWord : dictionaryMap.entrySet())
                {
                    if (Math.abs(word.length() - dictionaryWord.getKey().length()) <= 2) {
                        if (distance(word, dictionaryWord.getKey()) <= 2) {
                            if (Character.isUpperCase(word.charAt(0))) {
                                String temp = Character.toUpperCase(dictionaryWord.getKey().charAt(0)) + dictionaryWord.getKey().substring(1);
                                matcher.appendReplacement(sb, temp);
                            } else {
                                matcher.appendReplacement(sb, dictionaryWord.getKey());
                            }
                            break;
                        }
                    }
                }

            }
        }

        toCheck = sb.toString();

        return toCheck;
    }
}
