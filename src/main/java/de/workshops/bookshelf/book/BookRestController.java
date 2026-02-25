package de.workshops.bookshelf.book;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
public class BookRestController {
    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping( "/{isbn}")
    public ResponseEntity<Book> getByIsbn(@PathVariable(name = "isbn") @Size(min = 5, max = 14) String anIsbn) {
        var foundBook = bookService.getByIsbn(anIsbn);
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping(params = "author")
    public List<Book> getByAuthor(@RequestParam(required = false) String author) {
        return bookService.getByAuthor(author);
    }

    @PostMapping("/search")
    public List<Book> searchBooks(@RequestBody @Valid BookSearchRequest bookSearchRequest) {
        return bookService.searchBooks(bookSearchRequest);
    }

    @PostMapping
    public Book addBook(@RequestBody @Valid Book book) {
        return bookService.create(book);
    }

    @ExceptionHandler(BookException.class)
    public ResponseEntity<String> handleBookException(BookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }
}