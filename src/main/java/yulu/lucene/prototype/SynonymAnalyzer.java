package yulu.lucene.prototype;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.util.CharArraySet;

import java.io.*;
import java.text.ParseException;

/**
 * Created by yuriylukyanov on 12/12/15.
 */
public class SynonymAnalyzer extends Analyzer {

    private static final String PATH_SYNONYM_INDEX = "src/main/synonym_index/prolog/wn_s.pl";      //"wn/wn_s.pl"

    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer source = new ClassicTokenizer();
        SynonymMap mySynonymMap = null;
        try {
            mySynonymMap = buildSynonym();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TokenStream filter = new LowerCaseFilter(source);

        //add synonyms
        filter = new SynonymFilter(filter, mySynonymMap, false);

        filter = new StandardFilter(filter);

        // remove "stop" words from query
        filter = new StopFilter(filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        return new TokenStreamComponents(source, filter);
    }

    private SynonymMap buildSynonym() throws IOException, ParseException
    {
        File file = new File(PATH_SYNONYM_INDEX);
        System.out.println("File: " + file.isFile() + " Path: " + file.getAbsolutePath());
        InputStream stream = new FileInputStream(file);
        Reader rulesReader = new InputStreamReader(stream);

        SynonymMap.Builder parser = new WordnetSynonymParser(true, true, new StandardAnalyzer(CharArraySet.EMPTY_SET));
        ((WordnetSynonymParser) parser).parse(rulesReader);

        SynonymMap synonymMap = parser.build();

        return synonymMap;
    }
}
