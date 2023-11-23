package com.mjc.school.service.implementation;

import com.mjc.school.repository.implementation.AuthorRepository;
import com.mjc.school.repository.implementation.NewsRepository;
import com.mjc.school.repository.implementation.TagRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.service.dto.NewsDtoRequest;
import com.mjc.school.service.dto.NewsDtoResponse;
import com.mjc.school.service.mapper.NewsDtoMapperImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private NewsDtoMapperImpl newsDtoMapper;

    @InjectMocks
    private NewsService newsService;

    @Test
    void readAll() {
        //when
        newsService.readAll();

        //then
        verify(newsRepository).readAll();
    }

    @Test
    @Disabled
    void readById() {
    }

    @Test
    void create() {

        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Author author = new Author(1L, "name", date, date, null);
        News news = new News(1L, "title", "context", date, date, author, null);

        NewsDtoRequest request = new NewsDtoRequest(null, "title", "context", author.getId(), null);

        //when
        when(newsRepository.create(Mockito.any(News.class))).thenReturn(news);

        //then
        NewsDtoResponse createdNews = newsService.create(request);

        Assertions.assertThat(createdNews).isNotNull();
    }

    @Test
    @Disabled
    void update() {
    }

    @Test
    @Disabled
    void deleteById() {
    }

    @Test
    @Disabled
    void getNewsByParameters() {
    }
}