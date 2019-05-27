import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class UserFile
{
    private File userFile;
    private ArrayList<String> wordList;
    private String userFileContent;

    UserFile()
    {
        this.wordList = new ArrayList<String>();
    }

    public File getUserFile()
    {
        return this.userFile;
    }

    public void setUserFile(String fileName)
    {
        this.userFile = new File(fileName);
    }

    public String getUserFileContent() {
        return userFileContent;
    }

    public void setUserFileContent(String userFileContent) {
        this.userFileContent = userFileContent;
    }

    public boolean doesExist()
    {
        return this.userFile.exists();
    }

    public String readUserFile()
    {
        var sb = new StringBuilder();
        try(BufferedReader read = new BufferedReader(new FileReader(userFile)))
        {
            String line;
            while((line = read.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }

        return sb.toString();
    }
}
