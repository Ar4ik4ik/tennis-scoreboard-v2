package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.domain.Set;
import lombok.Builder;

import java.util.List;

@Builder
public record MatchResponseDto(PlayerResponseDto firstPlayer,
                               PlayerResponseDto secondPlayer,
                               ScoreDto score,
                               List<Set<Player>> sets,
                               String state) {
}
