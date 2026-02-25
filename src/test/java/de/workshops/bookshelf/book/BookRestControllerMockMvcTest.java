package de.workshops.bookshelf.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(BookRestController.class)
//@Import({BookService.class, BookRepository.class})
@WithMockUser
class BookRestControllerMockMvcTest {

    @LocalServerPort
    private int port;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MockMvcTester mockMvcTester;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAllBooksUsingMockMvc() throws Exception {
        var mvcResult = mockMvc.perform(get("/book"))
            .andExpect(status().isOk())
            .andReturn();

        var body = mvcResult.getResponse().getContentAsString();
        List<Book> books = objectMapper.readValue(body, new TypeReference<>() {
        });

        assertThat(books).hasSize(3);
    }

    @Test
    void getAllBooksUsingMockMvcTester() throws Exception {
        assertThat(mockMvcTester.get().uri("/book"))
            .hasStatusOk()
            .bodyJson()
            .convertTo(InstanceOfAssertFactories.list(Book.class))
            .satisfies(books -> assertThat(books).hasSize(3));
    }
}