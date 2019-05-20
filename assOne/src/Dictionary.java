import java.io.*;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Dictionary
{
    private File dictionaryFile;
    private File corpusFile;
    private HashMap<String, Integer> dictionaryHash;

    public Dictionary(File dictionaryFile)
    {
        this.dictionaryFile = dictionaryFile;
        dictionaryHash = new HashMap<String, Integer>(1300000);
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



    //could use .matches("[a-zA-Z]*") instead but this is faster
    public boolean isAlpha(String name)
    {
        char[] chars = name.toCharArray();

        for(char c : chars)
        {
            if(!Character.isLetter(c))
            {
                return false;
            }
        }
        return true;
    }

    /*
    Example output:
        1,000 is the similarity between "" and ""
        0,100 is the similarity between "1234567890" and "1"
     */
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
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

    public void spellCheck(File userFile)
    {

        try
        {
            String line;
            String dLine;
            var isCorrect = false;
            var isCorrected = false;
            BufferedReader reader = new BufferedReader(new FileReader(userFile));

            //read userfile
            while((line = reader.readLine()) != null)
            {
                String[] words = line.split(" ");
                for(var word : words) //loop user words
                {
                    BufferedReader readerD = new BufferedReader(new FileReader(dictionaryFile));
                    //read dictionary
                    while((dLine = readerD.readLine()) != null && !isCorrect)
                    {
                        String[] dictionaryWords = dLine.split(" ");
                        for(var dictionaryWord : dictionaryWords) //loop dictionary words
                        {
                            if(dictionaryWord.equals(word) && !isCorrect)
                            {
                                System.out.println(word + " - Correct");
                                isCorrect = true;
                            }
                        }
                    }

                    //if still wrong
                    if(!isCorrect)
                    {
                        //System.out.println(word + " - Incorrect");
                        BufferedReader readToFix = new BufferedReader(new FileReader(dictionaryFile));
                        String ddLine = "";
                        while ((ddLine = readToFix.readLine()) != null)
                        {
                            String[] dictionaryWords = ddLine.split(" ");
                            for (var dictionaryWord : dictionaryWords)
                            {
                                if((similarity(word, dictionaryWord) >= 0.70 && (!isCorrect)))
                                {
                                    System.out.println(word + " - should be => " + dictionaryWord);
                                    //printSimilarity(word, dictionaryWord);
                                    isCorrect = true;
                                }
                            }
                        }


                        if(!isCorrect)
                        {
                            readToFix = new BufferedReader(new FileReader(dictionaryFile));
                            ddLine = "";

                            while ((ddLine = readToFix.readLine()) != null)
                            {
                                String[] dictionaryWords = ddLine.split(" ");
                                for (var dictionaryWord : dictionaryWords)
                                {
                                    if((similarity(word, dictionaryWord) >= 0.60 && (!isCorrect)))
                                    {
                                        System.out.println(word + " - should be => " + dictionaryWord);
                                        //printSimilarity(word, dictionaryWord);
                                        isCorrect = true;
                                    }
                                }
                            }
                        }
                    }

                    //reset
                    isCorrect = false;
                    //isCorrected = false;
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Error reading file.");
        }
    }
}
