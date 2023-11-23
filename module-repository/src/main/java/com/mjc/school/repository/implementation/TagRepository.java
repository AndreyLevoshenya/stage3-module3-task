package com.mjc.school.repository.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import org.springframework.core.metrics.StartupStep;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TagRepository implements BaseRepository<Tag, Long> {
    private static final String SELECT_ALL_TAGS = "SELECT t FROM Tag t";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<Tag> readAll() {
        return entityManager.createQuery(SELECT_ALL_TAGS).getResultList();
    }

    @Override
    @Transactional
    public Optional<Tag> readById(Long id) {
        return Optional.of(entityManager.find(Tag.class, id));
    }

    @Override
    @Transactional
    public Tag create(Tag entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Tag update(Tag entity) {
        if(existById(entity.getId())) {
            Tag tag = entityManager.find(Tag.class, entity.getId());
            tag.setName(entity.getName());
            return tag;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(Tag.class, id));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean existById(Long id) {
        return entityManager.find(Tag.class, id) != null;
    }

    @Transactional
    public List<News> getNewsByTagId(Long id) {
        return new ArrayList<>(entityManager.find(Tag.class, id).getNews());
    }

    @Transactional
    public List<News> getNewsByTagName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Set<News> news = new HashSet<>();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        List<Tag> tags = entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), name))).getResultList();
        for (Tag tag : tags) {
            news.addAll(tag.getNews());
        }
        return new ArrayList<>(news);
    }
}
