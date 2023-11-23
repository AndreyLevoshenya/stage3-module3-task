package com.mjc.school.repository.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.Author;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class AuthorRepository implements BaseRepository<Author, Long> {
    private static final String SELECT_ALL_AUTHORS_QUERY = "SELECT a FROM Author a";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Author> readAll() {
        return entityManager.createQuery(SELECT_ALL_AUTHORS_QUERY).getResultList();
    }

    @Override
    @Transactional
    public Optional<Author> readById(Long id) {
        return Optional.of(entityManager.find(Author.class, id));
    }

    @Override
    @Transactional
    public Author create(Author entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
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
    @Transactional
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(Author.class, id));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean existById(Long id) {
        return entityManager.find(Author.class, id) != null;
    }

    @Transactional
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
