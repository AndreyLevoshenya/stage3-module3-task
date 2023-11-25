package com.mjc.school.repository.implementation;

import com.mjc.school.repository.BaseRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
public class TagRepository implements BaseRepository<Tag, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Optional<Tag> readById(Long id) {
        Tag tag = entityManager.getReference(Tag.class, id);
        return Optional.of(tag);
    }

    @Override
    public Tag create(Tag entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Tag update(Tag entity) {
        if(existById(entity.getId())) {
            Tag tag = entityManager.find(Tag.class, entity.getId());
            tag.setName(entity.getName());
            return tag;
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(Tag.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(Tag.class, id) != null;
    }

    public List<News> getNewsByTagId(Long id) {
        List<News> news = new ArrayList<>(entityManager.find(Tag.class, id).getNews());
        return news;
    }

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
