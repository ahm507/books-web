package net.elazhar.books.indexing;

public class Hit implements Comparable<Hit> {

    private String bookId;
    private int docId;


    public Hit(String bookId, int docId) {
        this.bookId = bookId;
        this.docId = docId;
    }

    private int getDocId() {
        return docId;
    }

    private String getBookId() {
        return bookId;
    }


    @Override
    public int compareTo(Hit o) {
        int bookCompare = o.getBookId().compareTo(bookId);
        if(bookCompare == 0) {
            return o.getDocId() - docId ;
        }
        return bookCompare;
    }

}
