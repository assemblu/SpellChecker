import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class DictionaryBuilder extends LoadingBar
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
        try
        {
            if(dictionaryFile.length() == 0)
            {
                return true;
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

        return false;
    }

    private Map<String, Integer> dictionarySorter(HashMap<String, Integer> dictionaryHash)
    {
        //commented for production
        //System.out.println("Map before sorting: " + dictionaryHash);
        Map<String, Integer> sorted = dictionaryHash
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        //commented for production
        //System.out.println("Map after sorting:" + sorted);
        return sorted;
    }

    public void fillDictionary(HashMap<String, Integer> dictionaryHash)
    {
        var count = 0;
        var sortedMap = dictionarySorter(dictionaryHash);

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("dictionary.txt"), "utf-8")))
        {
            for(var word : sortedMap.keySet())
            {
                if(word.length() >= 3)
                {
                    writer.write(word + " " + sortedMap.get(word));
                    ((BufferedWriter) writer).newLine();
                    writer.flush();
                }
                super.loadingBar(count);
                count++;
            }
            writer.close();
        }
        catch(IOException e)
        {
            System.err.println("FAILED\nError writing to dictionary file. Restart required.");
        }
        System.out.println(" - Task complete.");
    }
}
