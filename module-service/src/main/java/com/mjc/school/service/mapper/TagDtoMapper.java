package com.mjc.school.service.mapper;

import com.mjc.school.repository.model.Tag;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TagDtoMapper {
    List<TagDtoResponse> modelListToDtoList(List<Tag> modelList);

    Set<TagDtoResponse> modelSetToDtoSet(Set<Tag> modelSet);

    TagDtoResponse modelToDto(Tag tag);

    @Mapping(target = "news", ignore = true)
    Tag dtoToModel(TagDtoRequest dtoRequest);
}
