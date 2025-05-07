package com.github.ar4ik4ik.tennisscoreboard.mappers;

import com.github.ar4ik4ik.tennisscoreboard.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.dto.MatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.entity.PlayerEntity;

public class MatchMapper {

    public static MatchResponseDto fromModel(MatchEntity match) {
        return MatchResponseDto.builder()
                .id(match.getId())
                .build();
    }

    public static MatchEntity fromDto(MatchRequestDto requestDto) {
        return MatchEntity.builder()
                .firstPlayerEntityId(PlayerEntity.builder()
                        .name(requestDto.firstPlayer().name())
                        .build())
                .secondPlayerEntityId(PlayerEntity.builder()
                        .name(requestDto.secondPlayer().name())
                        .build())
                .build();
    }
}
