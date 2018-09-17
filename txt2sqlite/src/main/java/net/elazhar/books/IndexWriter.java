package net.elazhar.books;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public interface IndexWriter {
    void init(String bookCode) throws Exception;

    void close() throws Exception;

    void appendRecord(int pageId, String parentId, String title, String page, String pageNoVowels)
			throws Exception;
}
