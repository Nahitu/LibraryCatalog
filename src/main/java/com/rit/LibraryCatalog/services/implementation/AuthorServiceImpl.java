package com.rit.LibraryCatalog.services.implementation;

import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import com.rit.LibraryCatalog.mappers.AuthorMapper;
import com.rit.LibraryCatalog.repositories.AuthorRepository;
import com.rit.LibraryCatalog.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorMapper authorMapper, AuthorRepository authorRepository) {
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorEntity convertToEntity(AuthorDTO authorDTO) {
        return authorMapper.convertAuthorDTOToAuthorEntity(authorDTO);
    }

    @Override
    public AuthorDTO convertToDTO(AuthorEntity authorEntity) {
        return authorMapper.convertAuthorEntityToAuthorDTO(authorEntity);
    }

    @Override
    public AuthorEntity saveAuthor(AuthorEntity authorEntity) {
        return authorRepository.save(authorEntity);
    }

    @Override
    public List<AuthorEntity> findAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public Optional<AuthorEntity> findOne(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public boolean exists(Long id) {
        return authorRepository.existsById(id);
    }

    @Override
    public AuthorEntity partialUpdate(Long id, AuthorEntity authorEntity) {
        return authorRepository.findById(id).map(foundAuthor -> {
            Optional.ofNullable(authorEntity.getName()).ifPresent(foundAuthor::setName);
            Optional.ofNullable(authorEntity.getAge()).ifPresent(foundAuthor::setAge);

            return authorRepository.save(foundAuthor);
        }).orElseThrow(() -> new RuntimeException("Author not found"));
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
