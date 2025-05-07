package com.github.ar4ik4ik.tennisscoreboard.repository;

import com.github.ar4ik4ik.tennisscoreboard.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository<K extends Serializable, E extends BaseEntity<K>>
        implements Repository<K, E> {

    private final Class<E> clazz;
    @Getter(AccessLevel.PROTECTED)
    private final EntityManager entityManager;


    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        var foundedEntity = findById(id);
        if (foundedEntity.isPresent()) {
            entityManager.remove(foundedEntity);
        } else {
            throw new RuntimeException();
        }
    }

    public Optional<E> findById(K id) {
        return Optional.of(entityManager.find(clazz, id));
    }

    @Override
    public E update(E entity) {
        return entityManager.merge(entity);
    }

    @Override
    public List<E> findByField(String field, Object value) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(field), value));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<E> findAll() {
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        return entityManager.createQuery(criteria).getResultList();
    }
}
