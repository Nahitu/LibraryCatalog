package com.rit.LibraryCatalog.services.implementation;

import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.BookEntity;
import com.rit.LibraryCatalog.mappers.BookMapper;
import com.rit.LibraryCatalog.repositories.BookRepository;
import com.rit.LibraryCatalog.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    public BookServiceImpl(BookMapper bookMapper, BookRepository bookRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }


    @Override
    public BookDTO convertToDTO(BookEntity bookEntity) {
        return bookMapper.convertBookEntityToBookDTO(bookEntity);
    }

    @Override
    public BookEntity convertToEntity(BookDTO bookDTO) {
        return bookMapper.convertBookDTOToBookEntity(bookDTO);
    }

    @Override
    public boolean exists(String isbn) {
        return bookRepository.existsById(isbn);
    }

    @Override
    public BookEntity createUpdateBook(String isbn, BookEntity bookEntity) {
        bookEntity.setIsbn(isbn);
        return bookRepository.save(bookEntity);
    }

    @Override
    public List<BookEntity> findAll() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookEntity> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    public Optional<BookEntity> findOne(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public void delete(String isbn) {
        bookRepository.deleteById(isbn);
    }

    @Override
    public BookEntity partialUpdate(String isbn, BookEntity bookEntity) {
        return bookRepository.findById(isbn).map(foundBook -> {
            Optional.ofNullable(bookEntity.getTitle()).ifPresent(foundBook::setTitle);
            Optional.ofNullable(bookEntity.getAuthorEntity()).ifPresent(foundBook::setAuthorEntity);

            return foundBook;
        }).orElseThrow(()-> new RuntimeException("Book Not Found"));
    }


}
