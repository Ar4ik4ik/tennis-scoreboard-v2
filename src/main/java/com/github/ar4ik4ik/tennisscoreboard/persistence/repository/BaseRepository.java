package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.BaseEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<K extends Serializable, E extends BaseEntity<K>>
        implements Repository<K, E> {

    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        var entityManager = SessionManager.getSession();
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        var entityManager = SessionManager.getSession();
        var foundedEntity = findById(id);
        if (foundedEntity.isPresent()) {
            entityManager.remove(foundedEntity);
        } else {
            throw new RuntimeException();
        }
    }

    public Optional<E> findById(K id) {
        var entityManager = SessionManager.getSession();
        return Optional.of(entityManager.find(clazz, id));
    }

    @Override
    public E update(E entity) {
        var entityManager = SessionManager.getSession();
        return entityManager.merge(entity);
    }

    @Override
    public List<E> findByField(String field, Object value) {
        var entityManager = SessionManager.getSession();
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(field), value));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<E> findAll() {
        var entityManager = SessionManager.getSession();
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        return entityManager.createQuery(criteria).getResultList();
    }
    
}
