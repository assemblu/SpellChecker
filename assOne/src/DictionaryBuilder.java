import java.io.*;
import java.util.HashMap;

public class DictionaryBuilder
{
    private File dictionaryFile;

    public DictionaryBuilder()
    {
        dictionaryFile = new File("dictionary.txt");
    }

    public File getDictionaryFile()
    {
        return dictionaryFile;
    }

    public void setDictionaryFile(File dictionaryFile)
    {
        this.dictionaryFile = dictionaryFile;
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

    public void fillDictionary(HashMap<String, Integer> dictionaryHash)
    {
        System.out.println("\nWorking...");
        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dictionary.txt"), "utf-8")))
        {
            for(var word : dictionaryHash.keySet())
            {
                if(word.length() >= 4)
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
}
