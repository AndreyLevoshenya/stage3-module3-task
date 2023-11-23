package com.mjc.school.controller.implementation;

import com.mjc.school.controller.BaseController;
import com.mjc.school.controller.annotations.CommandBody;
import com.mjc.school.controller.annotations.CommandHandler;
import com.mjc.school.controller.annotations.CommandParam;
import com.mjc.school.service.BaseService;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.dto.ParametersDtoRequest;
import com.mjc.school.service.implementation.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class NewsController implements BaseController<NewsDtoRequest, NewsDtoResponse, Long> {
    private final BaseService<NewsDtoRequest, NewsDtoResponse, Long> newsService;

    @Autowired
    public NewsController(BaseService<NewsDtoRequest, NewsDtoResponse, Long> newsService) {
        this.newsService = newsService;
    }

    @Override
    @CommandHandler(operationNumber = 1)
    public List<NewsDtoResponse> readAll() {
        return newsService.readAll();
    }

    @Override
    @CommandHandler(operationNumber = 2)
    public NewsDtoResponse readById(@CommandParam(name = "id") Long id) {
        return newsService.readById(id);
    }

    @Override
    @CommandHandler(operationNumber = 3)
    public NewsDtoResponse create(@CommandBody NewsDtoRequest createRequest) {
        return newsService.create(createRequest);
    }

    @Override
    @CommandHandler(operationNumber = 4)
    public NewsDtoResponse update(@CommandBody NewsDtoRequest updateRequest) {
        return newsService.update(updateRequest);
    }

    @Override
    @CommandHandler(operationNumber = 5)
    public boolean deleteById(@CommandParam(name = "id") Long id) {
        return newsService.deleteById(id);
    }

    @CommandHandler(operationNumber = 18)
    public List<NewsDtoResponse> getNewsByParameters(@CommandBody ParametersDtoRequest parametersDtoRequest) {
        return ((NewsService)newsService).getNewsByParameters(parametersDtoRequest);
    }
}
