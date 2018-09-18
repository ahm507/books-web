package net.elazhar.books;

import net.elazhar.scanner.IndexWriter;
import org.springframework.stereotype.Component;

@Component
public class FTSWriter implements IndexWriter {

    @Override
    public void init(String bookCode) throws Exception {

    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void appendRecord(int pageId, String parentId, String title, String page, String pageNoVowels) throws Exception {

    }
}
