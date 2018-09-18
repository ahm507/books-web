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

    private FTIndex index = new FTIndex();
    private TextStore text = new TextStore();
    private TOCStore toc = new TOCStore();

    private String bookCode;


    @Override
    public void init(String bookCode) throws Exception {
        this.bookCode = bookCode;
        //open and decompress index files
    }

    @Override
    public void close() throws Exception {

        logger.info("Count of unique words indexed: " + index.getDicWordsCount() + " for hits of " + index.getTotalHitsIndexed());

        //showUniqueWords();

        //compress
        //store to files

    }
    
    @Override
    public void appendRecord(int docId, String parentId, String title, String document, String documentNoVowels) throws Exception {
        index.indexWords(bookCode, docId, title, documentNoVowels);
        text.storeText(bookCode, docId, title, document);
        toc.storeTOC(bookCode, docId, parentId);
    }
}
