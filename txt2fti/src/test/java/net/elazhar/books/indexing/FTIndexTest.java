package net.elazhar.books.indexing;

import org.junit.Test;
import org.apache.commons.lang3.reflect.FieldUtils;

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

}