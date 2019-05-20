public class Main
{
    public static void main(String[] args)
    {
        int similarityTest = 0; //set 1 for testing similarity function
        var io = new InputOutput();
        var db = new DictionaryBuilder();
        var cb = new CorpusBuilder();
        var dictionary = new Dictionary(db.getDictionaryFile());


        io.doesDictionaryExist(db.doesDictionaryExist());
        io.isDictionaryEmpty(db.isDictionaryEmpty());
        if(db.isDictionaryEmpty()) //if dictionary is empty ask for corpus file
        {
            io.setCorpus();
            while(io.isCorpusEmpty(cb.isCorpusEmpty(io.getCorpusFile())))
            {
                io.setCorpus();
            }
            cb.setCorpusFile(io.getCorpusFile());
            cb.readCorpus();
            db.fillDictionary(cb.getDictionaryHash());
        }

        cb.clearHashBuffer();
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
