import java.nio.charset.CoderResult;

public class Main
{
    public static void main(String[] args)
    {
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
        UserFile uf = new UserFile();
        uf.setUserFile(io.getUserFile());

        CorrectedFile correctedFile = new CorrectedFile(dictionary.spellCheck(uf.getUserFile()));

        correctedFile.makeFile();

    }
}
