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

            do {
                io.setCorpus();
                cb.setCorpusFile(io.getCorpusFile());
            }
            while(!cb.doesExist());
            cb.readCorpus();
            dictionary.setDictionaryMap(db.fillDictionary(cb.getDictionaryHash()));
        }
        else
        {
            if(io.askToWipe())
            {
                do {
                    io.setCorpus();
                    cb.setCorpusFile(io.getCorpusFile());
                }
                while(!cb.doesExist());
                cb.readCorpus();
                dictionary.setDictionaryMap(db.fillDictionary(cb.getDictionaryHash()));
            }
            else
            {
                //read dictionary file to create map
                dictionary.setDictionaryArray(db.readDictionary());
            }
        }

        UserFile uf = new UserFile();

        do {
            uf.setUserFile(io.askUserFile());
        }
        while(!uf.doesExist());

        uf.setUserFileContent(uf.readUserFile());

        CorrectedFile correctedFile = new CorrectedFile(dictionary.spellCheck(uf.getUserFileContent()));
        correctedFile.makeFile();

    }
}
