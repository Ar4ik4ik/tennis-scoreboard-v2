package com.github.ar4ik4ik.tennisscoreboard.mappers;

import com.github.ar4ik4ik.tennisscoreboard.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.dto.PlayerResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.entity.PlayerEntity;

public class PlayerMapper {

    public static PlayerResponseDto fromModel(PlayerEntity player) {
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
