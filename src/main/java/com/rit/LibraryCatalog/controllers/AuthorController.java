package com.rit.LibraryCatalog.controllers;

import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import com.rit.LibraryCatalog.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(path = "/authors")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO){
        AuthorEntity authorEntity = authorService.convertToEntity(authorDTO);
        AuthorEntity savedAuthor = authorService.saveAuthor(authorEntity);
        return new ResponseEntity<>(authorService.convertToDTO(savedAuthor), HttpStatus.CREATED);
    }

    @GetMapping(path = "/authors")
    public ResponseEntity<List<AuthorDTO>> listOfAuthors(){
        List<AuthorEntity> entities = authorService.findAll();
        return new ResponseEntity<>(
                entities.stream().map(authorService::convertToDTO).toList(),
                HttpStatus.OK);
    }

    @GetMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id){
        Optional<AuthorEntity> foundAuthor = authorService.findOne(id);

        return foundAuthor.map(authorEntity -> {
            AuthorDTO authorDTO = authorService.convertToDTO(authorEntity);
            return new ResponseEntity<>(authorDTO, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> fullUpdateAuthor(
            @PathVariable Long id,
            @RequestBody AuthorDTO authorDTO) {

        if(!authorService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AuthorEntity authorEntity = authorService.convertToEntity(authorDTO);
        authorEntity.setId(id);
        AuthorEntity updatedAuthor = authorService.saveAuthor(authorEntity);

        return new ResponseEntity<>(authorService.convertToDTO(updatedAuthor), HttpStatus.OK);
    }

    @PatchMapping(path = "/authors/{id}")
    public ResponseEntity<AuthorDTO> partialUpdateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO){
        if(!authorService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AuthorEntity authorEntity = authorService.convertToEntity(authorDTO);
        authorEntity.setId(id);
        AuthorEntity partialUpdatedAuthor = authorService.partialUpdate(id, authorEntity);

        return new ResponseEntity<>(authorService.convertToDTO(partialUpdatedAuthor), HttpStatus.OK);
    }

    @DeleteMapping(path = "/authors/{id}")
    public ResponseEntity deleteAuthor(@PathVariable Long id){
        authorService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
