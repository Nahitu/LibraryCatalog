package com.rit.LibraryCatalog.mappers;

import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthorMapper.class)
public interface BookMapper {

    @Mapping(source = "author", target = "authorEntity")
    BookEntity convertBookDTOToBookEntity(BookDTO bookDTO);

    @Mapping(source = "authorEntity", target = "author")
    BookDTO convertBookEntityToBookDTO(BookEntity bookEntity);
}
