package net.elazhar.books.indexing;

import java.util.*;
import java.util.logging.Logger;

public class FTIndex {
//    private HashMap<String, ArrayList<Hit>> index = new HashMap<>();
    private HashMap<String, Set<Hit>> index = new HashMap<>();
    private Logger logger = Logger.getLogger("FTSIndex");
    int hitCounter;

    private void showUniqueWords() {
        Set<String> keys = index.keySet();
        Set<String> sorted = new TreeSet<>(keys);

        logger.info("Showing list of unique words");
        for(String word: sorted) {
            logger.info(word);
        }
        logger.info("Total unique words: " + keys.size());
    }

    public FTIndex() {
        hitCounter = 0;
    }

    public void indexWords(String bookId, int docId, String title, String documentNoVowels) {
        String fullDocument = title + " " + documentNoVowels;
        String stopWords = "[ \\t,\\.;:،!?؟\\r\\n(){}\\[\\]«»'\\-_]";
        String[] words = fullDocument.split(stopWords);

        for(String word: words) {
            if( ! word.trim().isEmpty()) {
                Set<Hit> wordHits = index.get(word);
                if(wordHits == null) wordHits = new TreeSet<>();
                Hit hit = new Hit(bookId, docId);
                if( ! wordHits.contains(hit)) {
                    wordHits.add(hit);
                    index.put(word, wordHits);
                    hitCounter ++;
                }
            }
        }
    }


    public void showStats() {

        logger.info("Total hits stored in memory is: " + hitCounter);
        logger.info("show first 10 words");

        Set<String> keys = index.keySet();
        int count = 10;
        Iterator<String> it = keys.iterator();
        while(count > 0) {
            if(it.hasNext()) {
                String key = it.next();
                Set<Hit> hits = index.get(key);
                logger.info("word: " + key + ", " + hits);
            }
            count--;

        }


    }



}
