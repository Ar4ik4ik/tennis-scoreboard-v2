package com.github.ar4ik4ik.tennisscoreboard.mappers;

import com.github.ar4ik4ik.tennisscoreboard.entity.PlayerEntity;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;

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
