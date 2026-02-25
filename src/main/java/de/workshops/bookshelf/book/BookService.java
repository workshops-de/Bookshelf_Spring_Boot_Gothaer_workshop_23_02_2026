package de.workshops.bookshelf.book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getByIsbn(String isbn) {
        return bookRepository.findAll().stream()
            .filter(book -> book.getIsbn().equals(isbn))
            .findFirst()
            .orElseThrow(() ->new BookException("No book for this ISBN"));
    }

    public List<Book> getByAuthor(String author) {
        return bookRepository.findAll().stream()
            .filter(book -> book.getAuthor().startsWith(author))
            .toList();
    }

    public List<Book> searchBooks(BookSearchRequest bookSearchRequest) {
        return bookRepository.findAll().stream()
            .filter(book ->
                book.getIsbn().equals(bookSearchRequest.isbn())
                    || book.getAuthor().startsWith(bookSearchRequest.author()))
            .toList();
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }
}