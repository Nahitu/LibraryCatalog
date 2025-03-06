package com.rit.LibraryCatalog.controllers;


import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.BookEntity;
import com.rit.LibraryCatalog.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    // this function creates and updates book
    @PutMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> createUpdateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO) {
        BookEntity bookEntity = bookService.convertToEntity(bookDTO);
        boolean bookExists = bookService.exists(isbn);
        BookEntity savedBook = bookService.createUpdateBook(isbn, bookEntity);

        BookDTO savedBookDTO = bookService.convertToDTO(savedBook);

        if(bookExists){
            return new ResponseEntity<>(savedBookDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
        }
    }

//    @GetMapping(path = "/books")
//    public ResponseEntity<List<BookDTO>> listOfBooks(){
//        List<BookDTO> books = bookService.findAll().stream().map(bookService::convertToDTO).toList();
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }

    @GetMapping(path = "/books")
    public Page<BookDTO> listOfPageableBooks(Pageable pageable){
        Page<BookEntity> books = bookService.findAll(pageable);
        return books.map(bookService::convertToDTO);
    }

    @GetMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable String isbn){
        Optional<BookEntity> foundBook = bookService.findOne(isbn);

        return foundBook.map(bookEntity -> {
            BookDTO bookDTO = bookService.convertToDTO(bookEntity);
            return new ResponseEntity<>(bookDTO, HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/books/{isbn}")
    public ResponseEntity<BookDTO> partialUpdateBook(@PathVariable String isbn, @RequestBody BookDTO bookDTO){
        if(!bookService.exists(isbn)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BookEntity bookEntity = bookService.convertToEntity(bookDTO);
        bookEntity.setIsbn(isbn);
        BookEntity savedBook = bookService.partialUpdate(isbn, bookEntity);

        return new ResponseEntity<>(bookService.convertToDTO(savedBook), HttpStatus.OK);
    }

    @DeleteMapping(path = "/books/{isbn}")
    public ResponseEntity deleteBook(@PathVariable String isbn){
        bookService.delete(isbn);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
