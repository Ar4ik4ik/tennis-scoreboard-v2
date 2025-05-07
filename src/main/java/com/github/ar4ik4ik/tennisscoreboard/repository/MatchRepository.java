package com.github.ar4ik4ik.tennisscoreboard.repository;

import com.github.ar4ik4ik.tennisscoreboard.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import jakarta.persistence.EntityManager;
import lombok.Getter;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> {

    @Getter
    private final static MatchRepository INSTANCE = new MatchRepository(MatchEntity.class, SessionManager.getSession());

    public MatchRepository(Class<MatchEntity> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
    }

}
