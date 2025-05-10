package com.github.ar4ik4ik.tennisscoreboard.dto;

import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;

public record ScoreIncreaseDto(String matchUUID, Player scoringPlayer) {
}
