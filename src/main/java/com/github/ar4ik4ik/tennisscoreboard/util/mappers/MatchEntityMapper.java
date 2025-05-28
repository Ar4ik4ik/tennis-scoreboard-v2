package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;

public class MatchEntityMapper {

    public static MatchEntity fromModel(Match<Player> match) {
        return MatchEntity.builder()
                .firstPlayer(PlayerEntityMapper.fromModel(match.getFirstCompetitor()))
                .secondPlayer(PlayerEntityMapper.fromModel(match.getSecondCompetitor()))
                .winner(PlayerEntityMapper.fromModel(match.getWinner()))
                .build();
    }

    public static FinishedMatchResponseDto fromEntity(MatchEntity match) {
        return FinishedMatchResponseDto.builder()
                .firstPlayerName(match.getFirstPlayer().getName())
                .secondPlayerName(match.getSecondPlayer().getName())
                .winnerName(match.getWinner().getName())
                .build();
    }
}
