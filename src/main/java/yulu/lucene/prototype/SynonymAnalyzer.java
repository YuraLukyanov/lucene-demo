package yulu.lucene.prototype;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.Version;

import java.io.Reader;

/**
 * Created by yuriylukyanov on 12/12/15.
 */
public class SynonymAnalyzer extends Analyzer {

    public SynonymAnalyzer() {
        super();
    }

    @Override
    public TokenStream tokenStream(String s, Reader reader) {
        SynonymFilter synonymFilter = new SynonymFilter();
        TokenStream ts = new StopFilter(Version.LUCENE_33, new LowerCaseFilter(new StandardFilter(new StandardTokenizer(Version.LUCENE_33, reader))), StandardAnalyzer.STOP_WORDS_SET);

        return ts;
    }
}
