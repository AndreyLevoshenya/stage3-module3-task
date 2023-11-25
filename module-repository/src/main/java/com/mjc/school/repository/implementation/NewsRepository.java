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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class NewsRepository implements BaseRepository<News, Long> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<News> readAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Optional<News> readById(Long id) {
        return Optional.of(entityManager.getReference(News.class, id));
    }

    @Override
    public News create(News entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public News update(News entity) {
        if(existById(entity.getId())) {
            News news = entityManager.find(News.class, entity.getId());
            news.setTitle(entity.getTitle());
            news.setContent(entity.getContent());
            news.setLastUpdateDate(news.getLastUpdateDate());
            news.setAuthor(entity.getAuthor());
            entityManager.flush();
            return news;
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if(existById(id)) {
            entityManager.remove(entityManager.find(News.class, id));
            return true;
        }
        return false;
    }

    @Override
    public boolean existById(Long id) {
        return entityManager.find(News.class, id) != null;
    }

    public Set<Tag> getNewsTagsByNewsId(Long id) {
        News news = entityManager.getReference(News.class, id);
        return news.getTags();
    }

    public List<News> getNewsByTitle(String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        return new ArrayList<>(entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("title"), title))).getResultList());
    }

    public List<News> getNewsByContent(String content) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> root = criteriaQuery.from(News.class);
        return new ArrayList<>(entityManager.createQuery(criteriaQuery.select(root).where(criteriaBuilder.like(root.get("content"), "%" + content + "%"))).getResultList());
    }
}
