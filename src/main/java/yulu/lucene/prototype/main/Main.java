package yulu.lucene.prototype.main;

import org.apache.lucene.analysis.miscellaneous.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.RAMDirectory;
import yulu.lucene.prototype.SynonymAnalyzer;

import java.io.IOException;

/**
 * Created by yuriylukyanov on 12/15/15.
 */
public class Main {

    public static void main(String[] args) {
        RAMDirectory idx = new RAMDirectory();
        IndexReader reader = null;

        try {
            // Make an writer to create the index
            IndexWriter writer = new IndexWriter(idx, new IndexWriterConfig(new LimitTokenCountAnalyzer(new StandardAnalyzer(), 10)));

            // Add some Document objects containing quotes
            writer.addDocument(createDocument("Theodore Roosevelt freedom",
                    "It behooves every man to remember that the work of the " +
                            "critic, is of altogether secondary importance, and that, " +
                            "in the end, progress is accomplished by the man who does " +
                            "things."));
            writer.addDocument(createDocument("Friedrich Hayek",
                    "The case for individual freedom rests largely on the " +
                            "recognition of the inevitable and universal ignorance " +
                            "of all of us concerning a great many of the factors on " +
                            "which the achievements of our ends and welfare depend."));
            writer.addDocument(createDocument("Ayn Rand",
                    "There is nothing to take a man's freedom away from " +
                            "him, save other men. To be free, a man must be free " +
                            "of his brothers."));
            writer.addDocument(createDocument("Mohandas Gandhi",
                    "Freedom is not worth having if it does not connote " +
                            "freedom to err. zero"));

            // close the writer to finish building the index
            writer.close();

            // Build an IndexSearcher using the in-memory index
            reader = DirectoryReader.open(idx);
            IndexSearcher searcher = new IndexSearcher(reader);

            // Run a query
            search(searcher, "the freedom is nothing");
        }
        catch (IOException ioe) {
            // In this example we aren't really doing an I/O, so this
            // exception should never actually be thrown.
            ioe.printStackTrace();
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // In this example we aren't really doing an I/O, so this
                    // exception should never actually be thrown.
                    e.printStackTrace();
                }
            }
        }
    }

    private static Document createDocument(String title, String content) {
        Document doc = new Document();

        doc.add(new StringField("title", title, Field.Store.YES));

        doc.add(new TextField("content", content, Field.Store.YES));

        return doc;
    }

    private static void search(IndexSearcher searcher, String queryString)
            throws ParseException, IOException {

        // Build a Query object
        QueryParser parser = new QueryParser("content",
                new SynonymAnalyzer());
        Query query = parser.parse(queryString);
        System.out.println("Query:" + query.toString());


        int hitsPerPage = 10;
        // Search for the query
        TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitsPerPage);
        searcher.search(query, collector);

        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        int hitCount = collector.getTotalHits();
        System.out.println(hitCount + " total matching documents");

        // Examine the Hits object to see if there were any matches

        if (hitCount == 0) {
            System.out.println(
                    "No matches were found for \"" + queryString + "\"");
        } else {
            System.out.println("Hits for \"" +
                    queryString + "\" were found in quotes by:");

            // Iterate over the Documents in the Hits object
            for (int i = 0; i < hitCount; i++) {
                ScoreDoc scoreDoc = hits[i];
                int docId = scoreDoc.doc;
                float docScore = scoreDoc.score;
                System.out.println("docId: " + docId + "\t" + "docScore: " + docScore);

                Document doc = searcher.doc(docId);

                // Print the value that we stored in the "title" field. Note
                // that this Field was not indexed, but (unlike the
                // "contents" field) was stored verbatim and can be
                // retrieved.
                System.out.println("  " + (i + 1) + ". " + doc.get("title"));
                System.out.println("Content: " + doc.get("content"));
            }
        }
        System.out.println();
    }
}
