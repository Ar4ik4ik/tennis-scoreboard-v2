package com.github.ar4ik4ik.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record OngoingMatchResponseDto(
        Integer firstPlayerScore, Integer secondPlayerScore) {
}
