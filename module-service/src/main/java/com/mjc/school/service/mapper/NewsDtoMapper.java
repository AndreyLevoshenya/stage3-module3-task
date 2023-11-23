package com.mjc.school.service.mapper;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class NewsDtoMapper {
    @Autowired
    protected BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> authorService;

    @Autowired
    protected BaseRepository<Author, Long> authorRepository;

    @Autowired
    protected BaseRepository<Tag, Long> tagRepository;

    @Autowired TagDtoMapper tagDtoMapper;

    public abstract List<NewsDtoResponse> modelListToDtoList(List<News> modelList);

    @Mappings({
            @Mapping(target = "createDate", ignore = true),
            @Mapping(target = "lastUpdateDate", ignore = true),
            @Mapping(target = "author", expression = "java(dtoRequest.authorId() != null ? authorRepository.readById(dtoRequest.authorId()).get() : null)"),
            @Mapping(target = "tags", expression = "java(dtoRequest.tagIds() != null ? dtoRequest.tagIds().stream().map(id -> tagRepository.readById(id).get()).collect(java.util.stream.Collectors.toSet()) : null)")
    })
    public abstract News dtoToModel(NewsDtoRequest dtoRequest);

    @Mappings({
            @Mapping(target = "authorDtoResponse", expression = "java(news.getAuthor() != null ? authorService.readById(news.getAuthor().getId()) : null)"),
            @Mapping(target = "tagDtoResponseSet", expression = "java(tagDtoMapper.modelSetToDtoSet(news.getTags()))")
    })
    public abstract NewsDtoResponse modelToDto(News news);
}
