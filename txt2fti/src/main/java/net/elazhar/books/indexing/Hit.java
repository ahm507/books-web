package net.elazhar.books.indexing;

public class Hit {

    private String bookId;
    private int docId;


    public Hit(String bookId, int docId) {
        this.bookId = bookId;
        this.docId = docId;
    }
}
