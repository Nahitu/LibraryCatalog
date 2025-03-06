package com.rit.LibraryCatalog;

import com.rit.LibraryCatalog.domain.dto.AuthorDTO;
import com.rit.LibraryCatalog.domain.dto.BookDTO;
import com.rit.LibraryCatalog.domain.entities.AuthorEntity;
import com.rit.LibraryCatalog.domain.entities.BookEntity;

public class TestDataUtil {

    public static AuthorEntity createAuthorA(){
        return AuthorEntity.builder()
                .name("Andetsion")
                .age(45)
                .build();
    }

    public static AuthorEntity createAuthorB(){
        return AuthorEntity.builder()
                .name("Merhawit")
                .age(34)
                .build();
    }
    public static AuthorEntity createAuthorC(){
        return AuthorEntity.builder()
                .name("Bereket")
                .age(80)
                .build();
    }
    public static AuthorDTO createAuthorDTOA(){
        return AuthorDTO.builder()
                .name("Bereket")
                .age(80)
                .build();
    }

    public static BookEntity createBookA(AuthorEntity authorEntity){
        return BookEntity.builder()
                .isbn("123-345-511")
                .title("Learn something")
                .authorEntity(authorEntity)
                .build();
    }

    public static BookDTO createBookDTOA(AuthorDTO authorDTO){
        return BookDTO.builder()
                .isbn("123-345-511")
                .title("Learn something")
                .author(authorDTO)
                .build();
    }

}
