package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;

public record ScoreIncreaseDto(String matchUUID, Player scoringPlayer) {
}
