package com.rit.LibraryCatalog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rit.LibraryCatalog.TestDataUtil;
import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import com.rit.LibraryCatalog.services.AuthorService;
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
public class AuthorControllerIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthorService authorService;


    @Autowired
    public AuthorControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper, AuthorService authorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.authorService = authorService;
    }

    @Test
    public void testThatCreateAuthorReturnsHttpStatus201Created() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createAuthorA();

        String jsonAuthor = objectMapper.writeValueAsString(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthor)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAuthorReturnsCreatedAuthor() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createAuthorA();

        String jsonAuthor = objectMapper.writeValueAsString(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonAuthor)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatAuthorEntityFindAllSuccessfullyReturns200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFindAllReturnsListOfAuthors() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatFindAuthorByIdReturnsHttpStatus302Found() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createAuthorA();
        AuthorEntity savedAuthor = authorService.saveAuthor(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/{id}", savedAuthor.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isFound()
        );
    }

    @Test
    public void testThatFindAuthorReturnsHttpStatus404WhenAuthorDoesNotExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/87654567")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatFindAllReturnsAuthorById() throws Exception {
        AuthorEntity testAuthorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(testAuthorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testAuthorEntity.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(testAuthorEntity.getAge())
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttp200OkIfAuthorExists() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDTOA();
        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateAuthorReturnsHttp404IfAuthorDoesNotExists() throws Exception {
        AuthorDTO authorDTO = TestDataUtil.createAuthorDTOA();
        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", authorDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatUpdateAuthorFullUpdatesExistingAuthor() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        AuthorEntity savedAuthorEntity = authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDTOA();
        authorDTO.setId(authorEntity.getId());
        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/authors/{id}", savedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(authorDTO.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDTO.getAge())
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsHttpStatus200ok() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        AuthorEntity updatedAuthorEntity = authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDTOA();
        authorDTO.setName("Updated name");
        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/{id}", updatedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateExistingAuthorReturnsUpdatedAuthor() throws Exception {

        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        AuthorEntity updatedAuthorEntity = authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = TestDataUtil.createAuthorDTOA();
        authorDTO.setName("Updated name");
        String authorJson = objectMapper.writeValueAsString(authorDTO);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/authors/{id}", updatedAuthorEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Updated name")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.age").value(authorDTO.getAge())
        );
    }

    // Deleting Author

    @Test
    public void testThatDeleteAuthorReturnsHttpStatus204NoContent() throws Exception {
        AuthorEntity authorEntity = TestDataUtil.createAuthorA();
        authorService.saveAuthor(authorEntity);

        AuthorDTO authorDTO = authorService.convertToDTO(authorEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/authors/{id}", authorDTO.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }


}
