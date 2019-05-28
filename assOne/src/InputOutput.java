import java.io.*;
import java.sql.SQLOutput;
import java.util.Scanner;

public class InputOutput
{
    private File dictionaryFile;
    private Scanner reader;
    private String corpusFile;

    public InputOutput()
    {
        dictionaryFile = new File("dictionary.txt");
        reader = new Scanner(System.in);
        setCorpusFile(null);
        greetUser();
    }

    void setCorpusFile(String corpusFile)
    {
        this.corpusFile = corpusFile;
    }

    public String getCorpusFile()
    {
        return corpusFile;
    }

    void greetUser()
    {
        System.out.println("Welcome.");
    }


    void doesDictionaryExist(boolean state)
    {
        if(!state)
        {
            System.out.println("Dictionary file exists.");
        }
        else
        {
            System.out.println("Dictionary doesn't exist! Creating new dictionary.");
        }

    }

    void isDictionaryEmpty(boolean state)
    {
        if(state)
        {
            System.out.println("Dictionary file is empty.");
        }
        else
        {
            System.out.println("Dictionary file has content.");
        }
    }

    void setCorpus()
    {
        try
        {
            System.out.println("\nPlease enter a corpus file name. Example: \"corpus.txt\"");
            System.out.print("Corpus file name: ");
            String temp = reader.nextLine();

            if (!checkFileInput(temp))
            {
                System.out.println("Wrong input.");
                setCorpus();
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

    boolean checkFileInput(String input)
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

    public boolean isCorpusEmpty(boolean state)
    {
        if(state)
        {
            System.out.println("Corups file is empty!");
            return true;
        }
        return false;
    }

    public boolean askToWipe()
    {
        try
        {
            System.out.print("Would you like to wipe the data on the dictionary file and start over? [y]: ");

            var answer = reader.nextLine();
            answer.toLowerCase();
            if (answer.equals("y") || answer.equals("yes") || answer.equals("ye"))
            {
                var db = new DictionaryBuilder();
                System.out.println("Okay!");
                var dictionary = new Dictionary(db.getDictionaryFile());
                dictionaryFile.delete();
                System.out.println("Task complete.");
                return true;
            }

            System.out.println("Okay.");
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        return false;

    }

    public String askUserFile()
    {
        var temp = "";
        try
        {
            System.out.println("\nPlease enter a user file name. Example: \"check.txt\"");
            System.out.print("User name: ");
            temp = reader.nextLine();

            if (!checkFileInput(temp))
            {
                System.out.println("Wrong input.");
                askUserFile();
            } else
            {
                System.out.println("Thank you.");
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        return temp;
    }

}