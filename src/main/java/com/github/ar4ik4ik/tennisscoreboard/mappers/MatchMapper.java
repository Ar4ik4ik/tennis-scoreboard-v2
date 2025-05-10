package com.github.ar4ik4ik.tennisscoreboard.mappers;

import com.github.ar4ik4ik.tennisscoreboard.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.dto.OngoingMatchResponseDto;

import com.github.ar4ik4ik.tennisscoreboard.entity.MatchEntity;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;

public class MatchMapper {

    public static FinishedMatchResponseDto fromEntity(MatchEntity match) {
        return FinishedMatchResponseDto.builder()
                .id(match.getId())
                .firstPlayer(PlayerMapper.fromEntity(match.getFirstPlayerEntity()))
                .secondPlayer(PlayerMapper.fromEntity(match.getSecondPlayerEntity()))
                .winner(PlayerMapper.fromEntity(match.getWinner()))
                .build();
    }

    public static OngoingMatchResponseDto fromModel(Match<Player> match) {
        return OngoingMatchResponseDto.builder()
                .firstPlayerScore(match.getFirstCompetitorScore())
                .secondPlayerScore(match.getSecondCompetitorScore())
                .build();
    }
}
