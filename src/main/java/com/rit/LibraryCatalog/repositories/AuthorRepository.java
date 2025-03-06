package com.rit.LibraryCatalog.repositories;

import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<AuthorEntity, Long> {
}
