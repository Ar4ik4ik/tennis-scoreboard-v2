package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.PlayerEntity;
import lombok.Getter;

import java.util.Optional;

public class PlayerRepository extends BaseRepository<Integer, PlayerEntity> {

    @Getter
    private final static PlayerRepository INSTANCE = new PlayerRepository(
            PlayerEntity.class);

    public PlayerRepository(Class<PlayerEntity> clazz) {
        super(clazz);
    }

    public Optional<PlayerEntity> findByName(String name) {
        return findByField("name", name).stream().findFirst();
    }
}
