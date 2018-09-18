package net.elazhar.books.indexing;

import net.elazhar.scanner.IndexWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

@Component
public class FTSWriter implements IndexWriter {

    private Logger logger = Logger.getLogger("FTSWriter");
    HashMap<String, ArrayList<Integer>> index = new HashMap<>();



    @Override
    public void init(String bookCode) throws Exception {
    }

    @Override
    public void close() throws Exception {

        Set<String> keys = index.keySet();
        Set<String> sorted = new TreeSet<>(keys);


        showUniqueWords(keys, sorted);

    }

    private void showUniqueWords(Set<String> keys, Set<String> sorted) {
        logger.info("Showing list of unique words");
        for(String word: sorted) {
            logger.info(word);
        }
        logger.info("Total unique words: " + keys.size());
    }

    @Override
    public void appendRecord(int pageId, String parentId, String title, String document, String documentNoVowels) throws Exception {
        String stopWords = "[ \\t,.;:،!?؟\\r\\n(){}\\[\\]«»'\"]-_";
        String[] words = documentNoVowels.split(stopWords);
        for(String word: words) {
            if( ! word.trim().isEmpty()) {
                //get word


                index.put(word, null);
            }
        }


    }
}
