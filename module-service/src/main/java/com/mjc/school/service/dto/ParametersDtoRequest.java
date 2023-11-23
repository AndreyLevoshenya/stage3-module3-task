package com.mjc.school.service.dto;

import java.util.Objects;

public record ParametersDtoRequest(
        String tagName,
        Long tagId,
        String authorName,
        String newsTitle,
        String newsContent) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametersDtoRequest that = (ParametersDtoRequest) o;
        return Objects.equals(tagName, that.tagName) && Objects.equals(tagId, that.tagId) && Objects.equals(authorName, that.authorName) && Objects.equals(newsTitle, that.newsTitle) && Objects.equals(newsContent, that.newsContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagId, authorName, newsTitle, newsContent);
    }
}
