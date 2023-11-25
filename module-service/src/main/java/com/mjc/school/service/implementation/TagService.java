package com.mjc.school.service.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.implementation.NewsRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.annotations.Valid;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.exceptions.NotFoundException;
import com.mjc.school.service.mapper.TagDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

import static com.mjc.school.service.exceptions.ExceptionErrorCodes.NEWS_DOES_NOT_EXIST;
import static com.mjc.school.service.exceptions.ExceptionErrorCodes.TAG_DOES_NOT_EXIST;

@Service
@Transactional
public class TagService implements BaseService<TagDtoRequest, TagDtoResponse, Long> {
    private final BaseRepository<Tag, Long> tagRepository;
    private final BaseRepository<News, Long> newsRepository;

    private final TagDtoMapper tagDtoMapper;

    @Autowired
    public TagService(BaseRepository<Tag, Long> tagRepository, BaseRepository<News, Long> newsRepository, TagDtoMapper tagDtoMapper) {
        this.tagRepository = tagRepository;
        this.newsRepository = newsRepository;
        this.tagDtoMapper = tagDtoMapper;
    }

    @Override
    public List<TagDtoResponse> readAll() {
        return tagDtoMapper.modelListToDtoList(tagRepository.readAll());
    }

    @Override
    public TagDtoResponse readById(@Valid Long id) {
        if(tagRepository.existById(id)) {
            Tag tag = tagRepository.readById(id).get();
            return tagDtoMapper.modelToDto(tag);
        }
        else {
            throw new NotFoundException(String.format(TAG_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    @Override
    public TagDtoResponse create(@Valid TagDtoRequest createRequest) {
        Tag tag = tagDtoMapper.dtoToModel(createRequest);
        return tagDtoMapper.modelToDto(tagRepository.create(tag));
    }

    @Override
    public TagDtoResponse update(@Valid TagDtoRequest updateRequest) {
        if(tagRepository.existById(updateRequest.id())) {
            Tag tag = tagDtoMapper.dtoToModel(updateRequest);
            return tagDtoMapper.modelToDto(tagRepository.update(tag));
        }
        else {
            throw new NotFoundException(String.format(TAG_DOES_NOT_EXIST.getErrorMessage(), updateRequest.id()));
        }
    }

    @Override
    public boolean deleteById(@Valid Long id) {
        if(tagRepository.existById(id)) {
            return tagRepository.deleteById(id);
        }
        else {
            throw new NotFoundException(String.format(TAG_DOES_NOT_EXIST.getErrorMessage(), id));
        }
    }

    public Set<TagDtoResponse> getTagsByNewsId(@Valid Long newsId) {
        if(newsRepository.existById(newsId)) {
            Set<Tag> tags = ((NewsRepository) newsRepository).getNewsTagsByNewsId(newsId);
            return tagDtoMapper.modelSetToDtoSet(tags);
        }
        else {
            throw new NotFoundException(String.format(NEWS_DOES_NOT_EXIST.getErrorMessage(), newsId));
        }
    }
}
