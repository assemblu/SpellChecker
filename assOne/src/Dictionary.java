import java.io.*;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Dictionary extends LoadingBar
{
    private File dictionaryFile;
    private File corpusFile;
    private Map<String, Integer> dictionaryMap;
    private ArrayList<String> dictionaryArray;
    private boolean isArray;

    public Dictionary(File dictionaryFile)
    {
        this.dictionaryFile = dictionaryFile;
        this.isArray = false;
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
        this.isArray = true;
        this.dictionaryArray = dictionaryArray;
    }

    /*
    Example output:
        1,000 is the similarity between "" and ""
        0,100 is the similarity between "1234567890" and "1"
     */
    public double similarity(String s1, String s2)
    {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length())
        { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    public int editDistance(String s1, String s2)
    {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++)
        {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++)
            {
                if (i == 0)
                    costs[j] = j;
                else
                {
                    if (j > 0)
                    {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public void printSimilarity(String s, String t) {
        System.out.println(String.format(
                "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
        if(similarity(s, t) == 1.000)
        {
            System.out.println(s + "- correct");
        }
        else
        {
            System.out.println(s + "- incorrect");
        }

    }

    public ArrayList<String> spellCheck(File userFile)
    {
        //todo
        var correctedFile = new ArrayList<String>();
        var count = 0;
        try(BufferedReader read = new BufferedReader(new FileReader(userFile)))
        {
            var pattern = Pattern.compile("\\w+");

            String line;
            while((line =  read.readLine()) != null)
            {
                var matcher = pattern.matcher(line);
                while(matcher.find())
                {
                    super.loadingBar(count);

                    var word = matcher.group();

                    if(!this.isArray)
                    {
                        if(this.dictionaryMap.containsKey(word))
                        {
                            correctedFile.add(word);
                        }
                        else
                        {
                            for(var dictionaryWord : this.dictionaryMap.entrySet())
                            {
                                if(similarity(word, dictionaryWord.getKey()) >= 0.65)
                                {
                                    correctedFile.add(dictionaryWord.getKey());
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        if(this.dictionaryArray.contains(word))
                        {
                            correctedFile.add(word);
                        }
                        else
                        {
                            for(var dictionaryWord : this.dictionaryArray)
                            {
                                if(similarity(word, dictionaryWord) >= 0.65)
                                {
                                    correctedFile.add(dictionaryWord);
                                    break;
                                }
                            }
                        }
                    }

                    count++;
                }
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return correctedFile;
    }
}
