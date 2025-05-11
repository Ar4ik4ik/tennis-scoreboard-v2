package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.PlayerEntity;

public class PlayerEntityMapper {

    public static Player fromEntity(PlayerEntity playerEntity) {
        return Player.builder()
                .name(playerEntity.getName())
                .id(playerEntity.getId())
                .build();
    }

    public static PlayerEntity fromModel(Player player) {
        return PlayerEntity.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }
}
