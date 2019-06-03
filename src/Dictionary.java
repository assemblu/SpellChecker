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

    public String spellCheck(String toCheck)
    {
        var count = 0;
        String[] words = toCheck.replaceAll("\\p{P}", "").split(" ");
        for(var word : words)
        {
            super.loadingBar(count);

            //if it is an array it means user didn't supply new corpus
            //so we read the current dictionary which is sorted according to frequency
            if(!this.isArray)
            {
                if(dictionaryMap.containsKey(word))
                {
                    //word is correct, do nothing
                }
                else
                {
                    //manipulate the incorrect word frm the string
                    for(var dictionaryWord : dictionaryMap.entrySet())
                    {
                        if(similarity(word, dictionaryWord.getKey()) >= 0.65)
                        {
                            if(Character.isUpperCase(word.charAt(0)))
                            {
                                String temp = Character.toUpperCase(dictionaryWord.getKey().charAt(0)) + dictionaryWord.getKey().substring(1);
                                toCheck = toCheck.replace(word, temp);
                            }
                            else
                            {
                                toCheck = toCheck.replace(word, dictionaryWord.getKey());
                            }
                            break;
                        }
                    }
                }
            }
            else
            {
                if(dictionaryArray.contains(word))
                {
                    //word is correct, do nothing
                }
                else
                {
                    //manipulate the atomic word from the string
                    for(var dictionaryWord : dictionaryArray)
                    {
                        if(similarity(word, dictionaryWord) >= 0.65)
                        {
                            if(Character.isUpperCase(word.charAt(0)))
                            {
                                String temp = Character.toUpperCase(dictionaryWord.charAt(0)) + dictionaryWord.substring(1);
                                toCheck = toCheck.replace(word, temp);
                            }
                            else
                            {
                                toCheck = toCheck.replace(word, dictionaryWord);
                            }
                            break;
                        }
                    }
                }
            }
            count++;
        }
        return toCheck;
    }
}
