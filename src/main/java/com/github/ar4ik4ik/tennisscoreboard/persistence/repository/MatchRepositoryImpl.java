package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;

import java.util.List;

public interface MatchRepositoryImpl extends Repository<Integer, MatchEntity> {

    List<MatchEntity> findAll(int offset, int limit);
    List<MatchEntity> findAllByPlayerName(String playerName, int offset, int limit);
    long getObjectCount();
    long getObjectCount(String playerName);
}