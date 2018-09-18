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


    FTIndex index = new FTIndex();
    TextStore text = new TextStore();
    TOCStore toc = new TOCStore();

    String bookCode;
    @Override
    public void init(String bookCode) throws Exception {
        this.bookCode = bookCode;
        //open and decompress index files
    }

    @Override
    public void close() throws Exception {

        //showUniqueWords();

        //compress
        //store to files

    }



    @Override
    public void appendRecord(int pageId, String parentId, String title, String document, String documentNoVowels) throws Exception {
        index.indexWords(bookCode, pageId, title, documentNoVowels);
        text.storeText(bookCode, pageId, title, document);
        toc.storeTOC(bookCode, pageId, parentId);
    }
}
