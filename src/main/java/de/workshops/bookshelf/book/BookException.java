package de.workshops.bookshelf.book;

public class BookException extends RuntimeException {
    public BookException(String message) {
        super(message);
    }
}