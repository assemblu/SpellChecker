import java.io.File;

public class UserFile
{
    private File userFile;

    UserFile(String file)
    {
        this.userFile = new File(file);
    }

    public File getUserFile()
    {
        return this.userFile;
    }


}
