import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class CorrectedFile
{
    private String correctedFileContent;
    private File correctedFile;

    public CorrectedFile(String correctedFileContent)
    {
        this.correctedFileContent = correctedFileContent;
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

        try (PrintWriter out = new PrintWriter("output.txt"))
        {
            out.println(this.correctedFileContent);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}
