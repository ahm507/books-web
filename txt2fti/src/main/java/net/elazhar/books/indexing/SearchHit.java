package net.elazhar.books.indexing;

public class SearchHit {

    private String bookId;
    private int docId;


    public SearchHit(String bookId, int docId) {
        this.bookId = bookId;
        this.docId = docId;
    }
}
