package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.BaseEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class BaseRepository<K extends Serializable, E extends BaseEntity<K>>
        implements Repository<K, E> {

    protected final Class<E> clazz;


    @Override
    public E save(E entity) {
        var entityManager = SessionManager.getSession();
        entityManager.persist(entity);
        log.debug("Saved new entity {}", entity);
        return entity;
    }

    public Optional<E> findById(K id) {
        var entityManager = SessionManager.getSession();
        return Optional.of(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        var entityManager = SessionManager.getSession();
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        return entityManager.createQuery(criteria).getResultList();
    }
}
