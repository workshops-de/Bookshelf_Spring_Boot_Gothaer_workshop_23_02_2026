package de.workshops.bookshelf.book;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface BookJpaRepository extends ListCrudRepository<Book, Long> {
    Book findByIsbn(String isbn);

    List<Book> findByAuthorStartingWith(String author);

    @Query("SELECT b FROM Book b WHERE b.isbn=?2 or b.author LIKE ?1%")
    List<Book> searchBy(String author, String isbn);
}