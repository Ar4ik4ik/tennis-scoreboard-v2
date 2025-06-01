package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.BaseEntity;

import java.io.Serializable;

public interface Repository <K extends Serializable, E extends BaseEntity<K>> {
    E save(E entity);
}
