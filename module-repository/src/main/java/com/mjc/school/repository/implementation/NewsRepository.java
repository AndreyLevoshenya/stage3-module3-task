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
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class NewsRepository implements BaseRepository<News, Long> {
    private static final String SELECT_ALL_NEWS_QUERY = "SELECT n FROM News n";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<News> readAll() {
        return entityManager.createQuery(SELECT_ALL_NEWS_QUERY).getResultList();
    }

    @Override
    @Transactional
    public Optional<News> readById(Long id) {
        return Optional.of(entityManager.find(News.class, id));
    }

    @Override
    @Transactional
    public News create(News entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public News update(News entity) {
        if(existById(entity.getId())) {
            News news = entityManager.find(News.class, entity.getId());
            news.setTitle(entity.getTitle());
            news.setContent(entity.getContent());
            news.setLastUpdateDate(news.getLastUpdateDate());
            news.setAuthor(entity.getAuthor());
            return news;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(News.class, id));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean existById(Long id) {
        return entityManager.find(News.class, id) != null;
    }

    @Transactional
    public Set<Tag> getNewsTagsByNewsId(Long id) {
        News news = entityManager.getReference(News.class, id);
        return news.getTags();
    }

    @Transactional
    public List<News> getNewsByTitle(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        return new ArrayList<>(entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("title"), title))).getResultList());
    }

    @Transactional
    public List<News> getNewsByContent(String content) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        return new ArrayList<>(entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("content"), "%" + content + "%"))).getResultList());
    }
}
