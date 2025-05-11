package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository <K extends Serializable, E extends BaseEntity<K>> {
    E save(E entity);
    void delete(K id);
    E update(E entity);
    List<E> findByField(String field, Object value);
    List<E> findAll();
    Optional<E> findById(K id);
}
