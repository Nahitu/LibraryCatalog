package com.rit.LibraryCatalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rit.LibraryCatalog.TestDataUtil;
import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import com.rit.LibraryCatalog.domain.entities.BookEntity;
import com.rit.LibraryCatalog.services.AuthorService;
import com.rit.LibraryCatalog.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    private final ObjectMapper objectMapper;
    private final BookService bookService;
    private final MockMvc mockMvc;
    private final AuthorService authorService;


    @Autowired
    public BookControllerIntegrationTest(ObjectMapper objectMapper, BookService bookService, MockMvc mockMvc, AuthorService authorService) {
        this.authorService = authorService;
        this.objectMapper = new ObjectMapper();
        this.bookService = bookService;
        this.mockMvc = mockMvc;
    }

    @Test
    public void testThatCreateBookReturnsHttpStatus201Created() throws Exception {
        AuthorEntity authorA = TestDataUtil.createAuthorA();
        authorA.setId(null);
        BookEntity bookA = TestDataUtil.createBookA(authorA);

        String jsonBook = objectMapper.writeValueAsString(bookA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", bookA.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );

    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        AuthorDTO authorDTO = authorService.convertToDTO(authorEntity);

        BookDTO bookDTO = TestDataUtil.createBookDTOA(null);
        String bookJson = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", bookDTO.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookDTO.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookDTO.getTitle())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.author").value(bookDTO.getAuthor())
        );
    }

    @Test
    public void testThatListBooksReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

//    @Test
//    public void testThatListBooksReturnsListOfBooks() throws Exception {
//        BookEntity testBookEntity = TestDataUtil.createBookA(null);
//        bookService.createUpdateBook()(testBookEntity.getIsbn(), testBookEntity);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0][0].isbn").value(testBookEntity.getIsbn())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$[0][0].title").value(testBookEntity.getTitle())
//        );
//    }

    @Test
    public void testThatGetBookReturnsHttpStatus302FoundWhenBookExists() throws Exception {
        BookEntity testBookEntity = TestDataUtil.createBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isFound()
        );
    }

    @Test
    public void testThatGetBookReturnsHttpStatus404NotFoundWhenBookDoesNotExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/000-1123-113")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookReturnsCorrectBook() throws Exception {
        BookEntity testBookEntity = TestDataUtil.createBookA(null);
        bookService.createUpdateBook(testBookEntity.getIsbn(), testBookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/{isbn}", testBookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(testBookEntity.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(testBookEntity.getTitle())
        );
    }

    @Test
    public void testThatFullBookUpdateReturnsHttpResponse200OkWhenBookExists() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDTOA(null);
        String jsonBook = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );

    }
    @Test
    public void testThatFullBookUpdateReturnsUpdatedBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDTOA(null);
        String jsonBook = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/{isbn}", savedBook.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(savedBook.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(savedBook.getTitle())
        );

    }

    @Test
    public void testThatPartialBookUpdateReturnsHttpStatus200Ok() throws Exception{
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDTOA(null);
        bookDTO.setTitle("Updated");
        String jsonBook = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/{isbn}", bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
    @Test
    public void testThatPartialBookUpdateReturnsUpdatedBook() throws Exception{
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = TestDataUtil.createBookDTOA(null);
        bookDTO.setTitle("Updated");
        String jsonBook = objectMapper.writeValueAsString(bookDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/{isbn}", bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Updated")
        );
    }

    // Delete Book

    @Test
    public void testThatDeleteBookReturnsHttpStatus204NoContent() throws Exception {
        BookEntity bookEntity = TestDataUtil.createBookA(null);
        BookEntity savedBook = bookService.createUpdateBook(bookEntity.getIsbn(), bookEntity);

        BookDTO bookDTO = bookService.convertToDTO(bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/{isbn}", bookDTO.getIsbn())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

}
