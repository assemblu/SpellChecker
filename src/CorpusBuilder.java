import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CorpusBuilder extends LoadingBar
{
    private File corpusFile;
    private HashMap<String, Integer> dictionaryHash;

    public CorpusBuilder()
    {
        this.dictionaryHash = new HashMap<String, Integer>(90000000);
    }

    public HashMap<String, Integer> getDictionaryHash()
    {
        return dictionaryHash;
    }

    public boolean checkFileInput(String input)
    {
        try
        {
            if (!input.contains(".txt")) return false;

            if (!input.substring(input.length() - 4).equals(".txt"))
            {
                return false;
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

        return true;
    }

    public void askCorpusFile()
    {
        var reader = new Scanner(System.in);
        try
        {
            System.out.println("\nPlease enter a corpus file name. Example: \"corpus.txt\"");
            System.out.print("Corpus file name: ");
            String temp = reader.nextLine();

            if (!checkFileInput(temp))
            {
                System.out.println("Wrong input.");
                askCorpusFile();
            } else
            {
                setCorpusFile(temp);
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
    }

    public File getCorpusFile()
    {
        return corpusFile;
    }

    public void setCorpusFile(String corpusFile)
    {
        this.corpusFile = new File(corpusFile);
    }

    public void readCorpus()
    {
        int count = 0;
        try(BufferedReader read = new BufferedReader(new FileReader(corpusFile)))
        {
            var patter = Pattern.compile("\\w+");

            //BufferedReader read = new BufferedReader(new FileReader(corpusFile));
            String line;
            while((line = read.readLine()) != null)
            {
                super.loadingBar(count);

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

    }

    public boolean doesExist()
    {
        return this.corpusFile.exists();
    }
}
