package yulu.lucene.prototype;

import org.apache.lucene.analysis.TokenFilter;

import java.io.IOException;

/**
 * Created by yuriylukyanov on 12/12/15.
 */
public class SynonymFilter extends TokenFilter {

    @Override
    public boolean incrementToken() throws IOException {
        return false;
    }
}
