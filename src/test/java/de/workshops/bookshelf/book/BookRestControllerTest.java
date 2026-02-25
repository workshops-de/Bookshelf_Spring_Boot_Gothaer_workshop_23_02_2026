package de.workshops.bookshelf.book;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookRestControllerTest {

    @Autowired
    BookRestController bookRestController;

    @Test
    void getAllBooks() {
        var allBooks = bookRestController.getAllBooks();

        assertThat(allBooks).hasSize(3);
        assertEquals(3, allBooks.size());
    }

    @Test
    void getByIsbn() {
        var responseEntity = bookRestController.getByIsbn("978-3826655487");

        assertThat(responseEntity).isNotNull()
            .hasFieldOrPropertyWithValue("status", HttpStatusCode.valueOf(200));

        var body = responseEntity.getBody();
        assertThat(body).hasFieldOrPropertyWithValue("title", "Clean Code");
    }
}