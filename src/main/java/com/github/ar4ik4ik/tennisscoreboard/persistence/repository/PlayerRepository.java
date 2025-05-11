package com.github.ar4ik4ik.tennisscoreboard.persistence.repository;

import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.PlayerEntity;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;


import java.util.Optional;


public class PlayerRepository extends BaseRepository<Integer, PlayerEntity> {

    public PlayerRepository() {
        super(PlayerEntity.class);
    }

    public Optional<PlayerEntity> findByPlayerName(String name) {
        var entityManager = SessionManager.getSession();
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);

        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get("name"), name));

        return Optional.of(entityManager.createQuery(criteriaQuery).getResultList().getFirst());
    }
}
