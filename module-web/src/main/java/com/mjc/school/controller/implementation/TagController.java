package com.mjc.school.controller.implementation;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotations.CommandBody;
import com.mjc.school.controller.annotations.CommandHandler;
import com.mjc.school.controller.annotations.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.TagDtoRequest;
import com.mjc.school.service.dto.TagDtoResponse;
import com.mjc.school.service.implementation.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class TagController implements BaseController<TagDtoRequest, TagDtoResponse, Long> {
    private final BaseService<TagDtoRequest, TagDtoResponse, Long> tagService;

    @Autowired
    public TagController(BaseService<TagDtoRequest, TagDtoResponse, Long> tagService) {
        this.tagService = tagService;
    }

    @Override
    @CommandHandler(operationNumber = 11)
    public List<TagDtoResponse> readAll() {
        return tagService.readAll();
    }

    @Override
    @CommandHandler(operationNumber = 12)
    public TagDtoResponse readById(@CommandParam(name = "id") Long id) {
        return tagService.readById(id);
    }

    @Override
    @CommandHandler(operationNumber = 13)
    public TagDtoResponse create(@CommandBody TagDtoRequest createRequest) {
        return tagService.create(createRequest);
    }

    @Override
    @CommandHandler(operationNumber = 14)
    public TagDtoResponse update(@CommandBody TagDtoRequest updateRequest) {
        return tagService.update(updateRequest);
    }

    @Override
    @CommandHandler(operationNumber = 15)
    public boolean deleteById(@CommandParam(name = "id") Long id) {
        return tagService.deleteById(id);
    }

    @CommandHandler(operationNumber = 17)
    public Set<TagDtoResponse> getTagByNewsId(@CommandParam(name = "id") Long id) {
        return ((TagService) tagService).getTagsByNewsId(id);
    }
}
