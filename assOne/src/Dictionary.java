import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
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

    public Dictionary()
    {
        dictionaryFile = new File("dictionary.txt");
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

    public boolean doesDictionaryExist()
    {
        try
        {
            if(dictionaryFile.createNewFile())
            {
                return true;
            }
        }
        catch(IOException e)
        {
            System.err.println("Dictionary exists.");
        }
        return false;
    }

    public boolean isDictionaryEmpty()
    {
        if(dictionaryFile.length() == 0)
        {
            return true;
        }
        return false;
    }

    public boolean isCorpusEmpty(String corpusFile)
    {
        try(BufferedReader br = Files.newBufferedReader(Paths.get(corpusFile)))
        {
            if(br.readLine() == null)
            {
                //File empty
                return true;
            }
        }
        catch(IOException e)
        {
            System.err.println("Corpus file is empty!");
            return false;
        }
        return false;
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

    public void readCorpus()
    {
        int count = 0;
        try
        {
            var patter = Pattern.compile("[A-Za-z]+");

            BufferedReader read = new BufferedReader(new FileReader(corpusFile));
            String line;
            while((line = read.readLine()) != null)
            {
                var matcher = patter.matcher(line);
                while(matcher.find())
                {
                    var word = matcher.group();

                    Integer frequency = dictionaryHash.get(word);
                    if(frequency != null)
                    {
                        //word exists
                        dictionaryHash.put(word, frequency + 1);
                    }
                    else
                    {
                        //word doesn't exist
                        frequency = 1;
                        dictionaryHash.put(word, frequency);
                    }
                    count++;
                }
            }

        }
        catch(IOException e)
        {
            System.err.println("File doesn't exist.");
        }

        System.out.print("Word count: ");
        System.out.println(count);
    }

    void fillDictionary()
    {
        System.out.println("\nWorking...");
        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dictionary.txt"), "utf-8")))
        {
            for(var word : dictionaryHash.keySet())
            {
                if(word.length() > 4)
                {
                    writer.write(word + " " + dictionaryHash.get(word));
                    ((BufferedWriter) writer).newLine();
                    writer.flush();
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("FAILED\nError writing to dictionary file. Restart required.");
        }
        System.out.println("Task complete.");
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

    public void clearHashBuffer()
    {
        dictionaryHash.clear();
    }

    public void spellCheck(File userFile)
    {

    }
}
