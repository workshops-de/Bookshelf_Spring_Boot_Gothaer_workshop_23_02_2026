package de.workshops.bookshelf.book;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookJpaRepository bookRepository;

    public BookService(BookJpaRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getByIsbn(String isbn) {
        var book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new BookException("No book for this ISBN");
        }
        return book;
    }

    public List<Book> getByAuthor(String author) {
        return bookRepository.findByAuthorStartingWith(author);
    }

    public List<Book> searchBooks(BookSearchRequest bookSearchRequest) {
        return bookRepository.searchBy(bookSearchRequest.author(), bookSearchRequest.isbn());
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }
}