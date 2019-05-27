import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class CorrectedFile
{
    private ArrayList<String> words;
    private File correctedFile;

    public CorrectedFile(ArrayList<String> words)
    {
        this.words = words;
        correctedFile = new File("output.txt");
        if(doesFileExist())
        {
            correctedFile.delete();

        }
        try
        {
            correctedFile.createNewFile();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }

    //write to file
    public boolean doesFileExist()
    {
        try
        {
            if(this.correctedFile.length() == 0)
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

    public void makeFile()
    {
        var count = 0;

        try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(correctedFile), "utf-8")))
        {
            for(var word : this.words)
            {
                writer.write(word + " ");
                if((count % 50) == 0 && count != 0)
                {
                    ((BufferedWriter) writer).newLine();
                }
                count++;
            }
            writer.close();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}
