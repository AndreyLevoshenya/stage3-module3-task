package com.mjc.school.service.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.annotations.Valid;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.AuthorDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mjc.school.service.exceptions.ExceptionErrorCodes.AUTHOR_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionErrorCodes.NEWS_DOES_NOT_EXIST;

@Service
public class AuthorService implements BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> {
    private final BaseRepository<Author, Long> authorRepository;
    private final BaseRepository<News, Long> newsRepository;
    private final AuthorDtoMapper authorDtoMapper;

    @Autowired
    public AuthorService(BaseRepository<Author, Long> authorRepository, BaseRepository<News, Long> newsRepository, AuthorDtoMapper authorDtoMapper) {
        this.authorRepository = authorRepository;
        this.newsRepository = newsRepository;
        this.authorDtoMapper = authorDtoMapper;
    }

    @Override
    public List<AuthorDtoResponse> readAll() {
        return authorDtoMapper.modelListToDtoList(authorRepository.readAll());
    }

    @Override
    public AuthorDtoResponse readById(@Valid Long id) {
        if(authorRepository.existById(id)) {
            Author author = authorRepository.readById(id).get();
            return authorDtoMapper.modelToDto(author);
        }
        else {
            throw new NotFoundException(String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Override
    public AuthorDtoResponse create(@Valid AuthorDtoRequest createRequest) {
        Author model = authorDtoMapper.dtoToModel(createRequest);
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        model.setCreateDate(date);
        model.setLastUpdateDate(date);
        Author author = authorRepository.create(model);
        return authorDtoMapper.modelToDto(author);
    }

    @Override
    public AuthorDtoResponse update(@Valid AuthorDtoRequest updateRequest) {
        if(authorRepository.existById(updateRequest.id())) {
            Author author = authorDtoMapper.dtoToModel(updateRequest);
            author.setLastUpdateDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            return authorDtoMapper.modelToDto(authorRepository.update(author));
        }
        else {
            throw new NotFoundException(String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), updateRequest.id()));
        }
    }

    @Override
    public boolean deleteById(@Valid Long id) {
        if(authorRepository.existById(id)) {
            return authorRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(String.format(AUTHOR_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Transactional
    public AuthorDtoResponse getAuthorByNewsId(@Valid Long id) {
        if(newsRepository.existById(id)) {
            Author author = newsRepository.readById(id).get().getAuthor();
            return authorDtoMapper.modelToDto(author);
        }
        else {
            throw new NotFoundException(String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }
}
