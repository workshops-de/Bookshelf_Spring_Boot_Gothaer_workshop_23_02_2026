package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerMockitoBeanTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @Captor
    ArgumentCaptor<String> isbnCaptor;

    @Test
    void getAllBooksUsingMockMvc() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(
            new Book(),
            new Book()
        ));

        var mvcResult = mockMvc.perform(get("/book"))
            .andExpect(status().isOk())
            .andReturn();

        var body = mvcResult.getResponse().getContentAsString();
        List<Book> books = objectMapper.readValue(body, new TypeReference<>() {
        });

        assertThat(books).hasSize(2);
    }

    @Test
    void getByIsbn() throws Exception {
        doThrow(new BookException("No book for this ISBN"))
            .when(bookService).getByIsbn(anyString());

        mockMvc.perform(get("/book/978-3826655487"))
            .andExpect(status().isIAmATeapot());
    }

    @Test
    void getByIsbnWithCaptor() throws Exception {
        String isbn = "978-3826655487";
        when(bookService.getByIsbn(isbnCaptor.capture())).thenReturn(new Book());

        mockMvc.perform(get("/book/" + isbn))
            .andExpect(status().isOk());

        assertThat(isbnCaptor.getValue()).isEqualTo(isbn);
    }

    @Test
    void createBook() throws Exception {
        String isbn = "111-1111111111";
        String title = "Data Oriented Programming with Java";
        String author = "Birgit Kratz";
        String description = "Using Java's new features";

        var expectedBook = new Book();
        expectedBook.setAuthor(author);
        expectedBook.setDescription(description);
        expectedBook.setIsbn(isbn);
        expectedBook.setTitle(title);

        when(bookService.create(any(Book.class))).thenReturn(expectedBook);

        var mvcResult = mockMvc.perform(post("/book")
                .content("""
                                {
                                    "isbn": "%s",
                                    "title": "%s",
                                    "author": "%s",
                                    "description": "%s"
                                }""".formatted(isbn, title, author, description))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();


        var body = mvcResult.getResponse().getContentAsString();
        Book actualBook = objectMapper.readValue(body, Book.class);

        assertThat(actualBook).isEqualTo(expectedBook);
    }
}