import java.io.File;
import java.util.ArrayList;

public class UserFile
{
    private File userFile;
    private ArrayList<String> wordList;

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

    public boolean doesExist()
    {
        return this.userFile.exists();
    }
}
