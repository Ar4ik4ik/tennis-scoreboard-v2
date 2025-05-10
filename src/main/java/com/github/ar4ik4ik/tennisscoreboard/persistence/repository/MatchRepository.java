package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;
import lombok.Getter;


public class MatchRepository extends BaseRepository<Integer, MatchEntity> {

    @Getter
    private final static MatchRepository INSTANCE = new MatchRepository(MatchEntity.class);

    public MatchRepository(Class<MatchEntity> clazz) {
        super(clazz);
    }

}
