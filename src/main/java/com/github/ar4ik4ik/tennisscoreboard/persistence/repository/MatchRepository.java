package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> {

    public MatchRepository() {
        super(MatchEntity.class);
    }

}
