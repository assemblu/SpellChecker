import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserFile
{
    private File userFile;
    private ArrayList<String> wordList;
    private String userFileContent;
    private File toCheck;

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

    public String askUserFile()
    {
        var reader = new Scanner(System.in);
        var temp = "";
        do {
            System.out.println("\nPlease enter a user file name. Example: \"check.txt\"");
            System.out.print("File name: ");
            temp = reader.nextLine();
            this.toCheck = new File(temp);
        }while(!this.toCheck.exists());

        return temp;
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
