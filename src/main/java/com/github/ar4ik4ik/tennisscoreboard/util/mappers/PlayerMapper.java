package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.PlayerEntity;

public class PlayerMapper {

    public static PlayerResponseDto fromModel(Player player) {
        return PlayerResponseDto.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

    public static PlayerResponseDto fromEntity(PlayerEntity player) {
        return PlayerResponseDto.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

    public static PlayerEntity fromRequestDto(PlayerRequestDto requestDto) {
        return PlayerEntity.builder()
                .name(requestDto.name())
                .build();
    }

    public static PlayerEntity fromResponseDto(PlayerResponseDto responseDto) {
        return PlayerEntity.builder()
                .id(responseDto.id())
                .name(responseDto.name())
                .build();
    }

}
