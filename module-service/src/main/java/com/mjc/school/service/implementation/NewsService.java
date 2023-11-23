package com.mjc.school.service.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.implementation.AuthorRepository;
import com.mjc.school.repository.implementation.NewsRepository;
import com.mjc.school.repository.implementation.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.annotations.Valid;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.ParametersDtoRequest;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.NewsDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mjc.school.service.exceptions.ExceptionErrorCodes.NEWS_DOES_NOT_EXIST;

@Service
public class NewsService implements BaseService<NewsDtoRequest, NewsDtoResponse, Long> {
    private final BaseRepository<News, Long> newsRepository;
    private final BaseRepository<Author, Long> authorRepository;
    private final BaseRepository<Tag, Long> tagRepository;
    private final NewsDtoMapper newsDtoMapper;

    @Autowired
    public NewsService(BaseRepository<News, Long> newsRepository, BaseRepository<Author, Long> authorRepository, BaseRepository<Tag, Long> tagRepository, NewsDtoMapper newsDtoMapper) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.newsDtoMapper = newsDtoMapper;
    }


    @Override
    public List<NewsDtoResponse> readAll() {
        return newsDtoMapper.modelListToDtoList(newsRepository.readAll());
    }

    @Override
    public NewsDtoResponse readById(@Valid Long id) {
        if(newsRepository.existById(id)){
            News news = newsRepository.readById(id).get();
            return newsDtoMapper.modelToDto(news);
        }
        else {
            throw new NotFoundException(String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Override
    @Transactional
    public NewsDtoResponse create(@Valid NewsDtoRequest createRequest) {
        News model = newsDtoMapper.dtoToModel(createRequest);
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        model.setCreateDate(date);
        model.setLastUpdateDate(date);

        return newsDtoMapper.modelToDto(newsRepository.create(model));
    }

    @Override
    public NewsDtoResponse update(@Valid NewsDtoRequest updateRequest) {
        if(newsRepository.existById(updateRequest.id())){
            News news = newsDtoMapper.dtoToModel(updateRequest);
            news.setLastUpdateDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            news.setAuthor(authorRepository.readById(updateRequest.authorId()).get());
            return newsDtoMapper.modelToDto(newsRepository.update(news));
        }
        else {
            throw new NotFoundException(String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), updateRequest.id()));
        }
    }

    @Override
    public boolean deleteById(@Valid Long id) {
        if(newsRepository.existById(id)){
            return newsRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    public List<NewsDtoResponse> getNewsByParameters(@Valid ParametersDtoRequest parametersDtoRequest) {
        Set<NewsDtoResponse> news = new HashSet<>();
        if(parametersDtoRequest.tagId() != null) {
            news.addAll(newsDtoMapper.modelListToDtoList(((TagRepository) tagRepository).getNewsByTagId(parametersDtoRequest.tagId())));
        }
        if(!parametersDtoRequest.tagName().isEmpty()) {
            news.addAll(newsDtoMapper.modelListToDtoList(((TagRepository) tagRepository).getNewsByTagName(parametersDtoRequest.tagName())));
        }
        if(!parametersDtoRequest.authorName().isEmpty()) {
            news.addAll(newsDtoMapper.modelListToDtoList(((AuthorRepository) authorRepository).getNewsByAuthorName(parametersDtoRequest.authorName())));
        }
        if(!parametersDtoRequest.newsTitle().isEmpty()) {
            news.addAll(newsDtoMapper.modelListToDtoList(((NewsRepository) newsRepository).getNewsByTitle(parametersDtoRequest.newsTitle())));
        }
        if(!parametersDtoRequest.newsContent().isEmpty()) {
            news.addAll(newsDtoMapper.modelListToDtoList(((NewsRepository) newsRepository).getNewsByContent(parametersDtoRequest.newsContent())));
        }
        return news.stream().toList();
    }
}
