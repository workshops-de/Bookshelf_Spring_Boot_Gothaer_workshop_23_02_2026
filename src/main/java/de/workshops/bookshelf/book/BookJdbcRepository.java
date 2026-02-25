package de.workshops.bookshelf.book;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BookJdbcRepository {
  private final JdbcTemplate jdbcTemplate;

  public BookJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  List<Book> findAllBooks() {
    return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
  }

  Book saveBook(Book book) {
    var keyHolder = new GeneratedKeyHolder();
    var sql = "INSERT INTO book (title, description, author, isbn) VALUES (?, ?, ?, ?)";
    int count = jdbcTemplate.update(con -> {
      var ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, book.getTitle());
      ps.setString(2, book.getDescription());
      ps.setString(3, book.getAuthor());
      ps.setString(4, book.getIsbn());
      return ps;}
    , keyHolder);

    if (count == 1) {
      book.setId(keyHolder.getKey().longValue());
      return book;
    }
    throw new RuntimeException("Could not save book");
  }
}
