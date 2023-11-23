package com.mjc.school.controller.implementation;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotations.CommandBody;
import com.mjc.school.controller.annotations.CommandHandler;
import com.mjc.school.controller.annotations.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.AuthorDtoRequest;
import com.mjc.school.service.dto.AuthorDtoResponse;
import com.mjc.school.service.implementation.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController implements BaseController<AuthorDtoRequest, AuthorDtoResponse, Long> {
    private final BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> authorService;

    @Autowired
    public AuthorController(BaseService<AuthorDtoRequest, AuthorDtoResponse, Long> authorService) {
        this.authorService = authorService;
    }

    @Override
    @CommandHandler(operationNumber = 6)
    public List<AuthorDtoResponse> readAll() {
        return authorService.readAll();
    }

    @Override
    @CommandHandler(operationNumber = 7)
    public AuthorDtoResponse readById(@CommandParam(name = "id") Long id) {
        return authorService.readById(id);
    }

    @Override
    @CommandHandler(operationNumber = 8)
    public AuthorDtoResponse create(@CommandBody AuthorDtoRequest createRequest) {
        return authorService.create(createRequest);
    }

    @Override
    @CommandHandler(operationNumber = 9)
    public AuthorDtoResponse update(@CommandBody AuthorDtoRequest updateRequest) {
        return authorService.update(updateRequest);
    }

    @Override
    @CommandHandler(operationNumber = 10)
    public boolean deleteById(@CommandParam(name = "id") Long id) {
        return authorService.deleteById(id);
    }

    @CommandHandler(operationNumber = 16)
    public AuthorDtoResponse getAuthorByNewsId(@CommandParam(name = "id") Long id) {
        return ((AuthorService) authorService).getAuthorByNewsId(id);
    }
}
