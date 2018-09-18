package net.elazhar.books.indexing;

import org.junit.Test;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Set;

import static org.junit.Assert.assertEquals;


public class FTIndexTest {



    @Test
    public void oneBook() throws IllegalAccessException {
        FTIndex index = new FTIndex();
        index.indexWords("book1", 1, "some title", "word1, word2");
        Integer hitCounter = (Integer) FieldUtils.readDeclaredField(index, "hitCounter", true);
        assertEquals(4, hitCounter.intValue());
    }

    @Test
    public void avoidDuplicateHits() throws IllegalAccessException {
        FTIndex index = new FTIndex();
        index.indexWords("book1", 1, "some title", "word1, word2, word3 word1, word2, word3");
        Integer hitCounter = (Integer) FieldUtils.readDeclaredField(index, "hitCounter", true);
        assertEquals(5, hitCounter.intValue());
    }


    @Test
    public void twoBooksWithDuplicateHits() throws IllegalAccessException {
        FTIndex index = new FTIndex();
        index.indexWords("book1", 1, "some title", "word1, word1 word2, word3");
        index.indexWords("book2", 1, "some title title3", "word1, word4, word5");
        Integer hitCounter = (Integer) FieldUtils.readDeclaredField(index, "hitCounter", true);
        assertEquals(11, hitCounter.intValue());
    }

    @Test
    public void uniqueDictionaryWords() throws IllegalAccessException {
        FTIndex index = new FTIndex();
        index.indexWords("book1", 1, "some other title", "word1, word2 word1, word2 word1, word2");
        assertEquals(5, index.getDicWordsCount());
    }

    @Test
    public void exactWordHits() {
        FTIndex index = new FTIndex();
        index.indexWords("book1", 1, "title1", "word1, word1 word2, word3");
        index.indexWords("book2", 1, "some title title3", "word1, word4, word5");
        index.indexWords("book3", 1, "some title title3", "word1, word4, word5");

        Set<Hit> hits = index.getWordHits("title1");
        assertEquals(1, hits.size());
        hits = index.getWordHits("word1");
        assertEquals(3, hits.size());
        hits = index.getWordHits("word5");
        assertEquals(2, hits.size());

    }


}