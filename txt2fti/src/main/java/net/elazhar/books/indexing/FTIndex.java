package net.elazhar.books.indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class FTIndex {
    private HashMap<String, ArrayList<Integer>> index = new HashMap<>();
    private Logger logger = Logger.getLogger("FTSIndex");

    private void showUniqueWords() {
        Set<String> keys = index.keySet();
        Set<String> sorted = new TreeSet<>(keys);

        logger.info("Showing list of unique words");
        for(String word: sorted) {
            logger.info(word);
        }
        logger.info("Total unique words: " + keys.size());
    }

    public void indexWords(String bookId, int pageId, String title, String documentNoVowels) {
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
