package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import lombok.Builder;

@Builder
public record MatchRequestDto(PlayerRequestDto firstPlayer, PlayerRequestDto secondPlayer) {
}
