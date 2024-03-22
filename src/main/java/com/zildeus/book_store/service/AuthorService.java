package com.zildeus.book_store.service;

import com.zildeus.book_store.dto.AuthorDto;
import com.zildeus.book_store.exceptions.DuplicateResourceException;
import com.zildeus.book_store.exceptions.ResourceNotFoundException;
import com.zildeus.book_store.mapper.IMapper;
import com.zildeus.book_store.model.Author;
import com.zildeus.book_store.model.Book;
import com.zildeus.book_store.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository repository;
    private final IMapper mapper;

    public void RegisterAuthor(AuthorDto registrationRequest)
    {
        if(repository.findByName(registrationRequest.name()).isPresent()) {
            throw new DuplicateResourceException("author with name %s already exists!".formatted(registrationRequest.name()));
        }
        Author author = new Author();
        author.setName(registrationRequest.name());
        author.setBirthYear(registrationRequest.birthYear());
        author.setLocation(registrationRequest.location());
        repository.save(author);
    }

    public List<AuthorDto> GetAuthors(){
        return repository.findAll().stream().map(mapper::AuthorDtoFromAuthor).toList();
    }
    public Author GetAuthorObject(String name){
        return repository.findByName(name)
                .orElseThrow(()->
                        new ResourceNotFoundException("author with name %s does not exist!".formatted(name))
                );
    }
    public AuthorDto GetAuthor(String name){
        Author author = GetAuthorObject(name);
        return mapper.AuthorDtoFromAuthor(author);
    }
}
