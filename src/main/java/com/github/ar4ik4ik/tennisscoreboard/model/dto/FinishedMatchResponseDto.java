package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import lombok.Builder;

@Builder
public record FinishedMatchResponseDto(
        String firstPlayerName, String secondPlayerName, String winnerName) {
}
