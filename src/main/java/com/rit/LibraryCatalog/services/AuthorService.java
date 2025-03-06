package com.rit.LibraryCatalog.services;

import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthorService {

    AuthorEntity convertToEntity(AuthorDTO authorDTO);

    AuthorDTO convertToDTO(AuthorEntity authorEntity);

    AuthorEntity saveAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean exists(Long id);

    AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity);

    void delete(Long id);
}
