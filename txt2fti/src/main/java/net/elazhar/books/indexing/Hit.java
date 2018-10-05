package net.elazhar.books.indexing;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if(!(obj instanceof Hit)) {
            return false;
        }
        Hit other = (Hit) obj;
        return other.getBookId().equals(bookId) && other.getDocId() == docId;
        //You can use Objects.equals()
    }

    @Override
    public int hashCode() {
        //The 17 and 31 hash code idea is from the classic Java book – effective Java : item 9
//        int result = 17;
//        result = 31 * result + bookId.hashCode();
//        result = 31 * result + docId;
//        return result;
        return Objects.hash(bookId, docId); //JDK 7
    }

    @Override public String toString() {
        return String.format("bookId: %s, docId:", bookId, docId);
    }
}
