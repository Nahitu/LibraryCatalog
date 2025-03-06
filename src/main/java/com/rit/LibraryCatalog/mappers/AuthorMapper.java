package com.rit.LibraryCatalog.mappers;

import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorEntity convertAuthorDTOToAuthorEntity(AuthorDTO authorDTO);

    AuthorDTO convertAuthorEntityToAuthorDTO(AuthorEntity authorEntity);

}
