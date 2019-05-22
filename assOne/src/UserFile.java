import java.io.File;
import java.util.ArrayList;

public class UserFile
{
    private File userFile;
    private ArrayList<String> wordList;

    UserFile(String file)
    {
        this.userFile = new File(file);
        this.wordList = new ArrayList<String>();
    }

    public File getUserFile()
    {
        return this.userFile;
    }


}
