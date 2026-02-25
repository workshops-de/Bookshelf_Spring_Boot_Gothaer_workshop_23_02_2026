package de.workshops.bookshelf.book;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BookJdbcClientRepository {
  private final JdbcClient jdbcClient;

  public BookJdbcClientRepository(JdbcClient jdbcClient) {
    this.jdbcClient = jdbcClient;
  }

  List<Book> findAllBooks() {
    var sql = "SELECT * FROM book";
    return jdbcClient.sql(sql)
        .query(new BeanPropertyRowMapper<>(Book.class))
        .list();
  }

  Book saveBook(Book book) {
    var sql = "INSERT INTO book (title, description, author, isbn) VALUES (?, ?, ?, ?)";
    var generatedKeyHolder = new GeneratedKeyHolder();
    jdbcClient.sql(sql)
        .params(
            book.getTitle(),
            book.getDescription(),
            book.getAuthor(),
            book.getIsbn())
        .update(generatedKeyHolder);

    book.setId(generatedKeyHolder.getKey().longValue());
    return book;
  }
}
