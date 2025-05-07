package com.github.ar4ik4ik.tennisscoreboard.repository;

import com.github.ar4ik4ik.tennisscoreboard.entity.PlayerEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import jakarta.persistence.EntityManager;
import lombok.Getter;

import java.util.Optional;

public class PlayerRepository extends BaseRepository<Integer, PlayerEntity> {

    @Getter
    private final static PlayerRepository INSTANCE = new PlayerRepository(
            PlayerEntity.class, SessionManager.getSession());

    public PlayerRepository(Class<PlayerEntity> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
    }

    public Optional<PlayerEntity> findByName(String name) {
        return findByField("name", name).stream().findFirst();
    }
}
