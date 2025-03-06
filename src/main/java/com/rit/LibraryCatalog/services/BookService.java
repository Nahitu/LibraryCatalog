package com.rit.LibraryCatalog.services;

import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {

    BookDTO convertToDTO(BookEntity bookEntity);

    BookEntity convertToEntity(BookDTO bookDTO);

    boolean exists(String isbn);

    BookEntity createUpdateBook(String isbn, BookEntity bookEntity);

    List<BookEntity> findAll();

    Page<BookEntity> findAll(Pageable pageable);

    BookEntity partialUpdate(String isbn, BookEntity bookEntity);

    Optional<BookEntity> findOne(String isbn);

    void delete(String isbn);
}
