package com.github.ar4ik4ik.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record FinishedMatchResponseDto(
        Integer id, PlayerResponseDto firstPlayer, PlayerResponseDto secondPlayer, PlayerResponseDto winner) {
}
