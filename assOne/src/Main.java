import java.io.FileNotFoundException;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        int similarityTest = 0; //set 1 for testing similarity function
        var io = new InputOutput();
        var dictionary = new Dictionary();

        io.doesDictionaryExist(dictionary.doesDictionaryExist());
        io.isDictionaryEmpty(dictionary.isDictionaryEmpty());
        if(dictionary.isDictionaryEmpty()) //if dictionary is empty ask for corpus file
        {
            io.setCorpus();
            while(io.isCorpusEmpty(dictionary.isCorpusEmpty(io.getCorpusFile())))
            {
                io.setCorpus();
            }
            dictionary.setCorpusFile(io.getCorpusFile());
            dictionary.readCorpus();
            dictionary.fillDictionary();
        }

        dictionary.clearHashBuffer();
        UserFile uf = new UserFile(io.getUserFile());
        dictionary.spellCheck(uf.getUserFile());



        if(similarityTest > 0)
        {
            dictionary.printSimilarity("", "");
            dictionary.printSimilarity("1234567890", "1");
            dictionary.printSimilarity("1234567890", "123");
            dictionary.printSimilarity("1234567890", "1234567");
            dictionary.printSimilarity("1234567890", "1234567890");
            dictionary.printSimilarity("1234567890", "1234567980");
            dictionary.printSimilarity("47/2010", "472010");
            dictionary.printSimilarity("47/2010", "472011");
            dictionary.printSimilarity("47/2010", "AB.CDEF");
            dictionary.printSimilarity("47/2010", "4B.CDEFG");
            dictionary.printSimilarity("47/2010", "AB.CDEFG");
            dictionary.printSimilarity("The quick fox jumped", "The fox jumped");
            dictionary.printSimilarity("The quick fox jumped", "The fox");
            dictionary.printSimilarity("The quick fox jumped", "The quick fox jumped off the balcany");
            dictionary.printSimilarity("kitten", "sitting");
            dictionary.printSimilarity("1", "2");
        }





        /*var sb = new StringBuilder();

        try(BufferedReader br = Files.newBufferedReader(Paths.get("test.txt")))
        {
            String line;
            while((line = br.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
        }catch(IOException e)
        {
            System.err.format("IOException: %s%n", e);
        }

        System.out.println(sb.toString());*/
    }
}
