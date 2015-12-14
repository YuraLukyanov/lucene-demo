package yulu.lucene.prototype;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuriylukyanov on 12/12/15.
 */
public class SynonymEngine {

    RAMDirectory directory;
    IndexSearcher searcher;

    public SynonymEngine(File index) throws IOException {
        FSDirectory fsDir = new SimpleFSDirectory(index, null);
        directory = new RAMDirectory(fsDir);  // #1
        fsDir.close();
        searcher = new IndexSearcher(directory);


    }

    public String[] getSynonyms(String word) throws IOException {
        ArrayList synList = new ArrayList();
        Collector collector = ;  // #2

        searcher.search(new TermQuery(new Term("word", word)), collector);
        List<ScoreDoc> hits = collector.();
        Iterator<ScoreDoc> it = hits.iterator();
        while(it.hasNext()) {                   // #3
            ScoreDoc hit = it.next();
            Document doc = searcher.doc(hit.doc);
            String[] values = doc.getValues("syn");
            for (int j = 0; j < values.length; j++) {  // #4
                synList.add(values[j]);
            } }
        return (String[]) synList.toArray(new String[0]);
    }
}
