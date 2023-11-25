package com.mjc.school.repository.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
public class AuthorRepository implements BaseRepository<Author, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Author> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = criteriaQuery.from(Author.class);
        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Optional<Author> readById(Long id) {
        return Optional.of(entityManager.getReference(Author.class, id));
    }

    @Override
    public Author create(Author entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Author update(Author entity) {
        if(existById(entity.getId())) {
            Author author = entityManager.find(Author.class, entity.getId());
            author.setName(entity.getName());
            author.setLastUpdateDate(entity.getLastUpdateDate());
            return author;
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(Author.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(Author.class, id) != null;
    }

    public List<News> getNewsByAuthorName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Set<News> news = new HashSet<>();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = criteriaQuery.from(Author.class);
        List<Author> authors = entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), name))).getResultList();
        for (Author author : authors) {
            news.addAll(author.getNews());
        }
        return new ArrayList<>(news);
    }
}
