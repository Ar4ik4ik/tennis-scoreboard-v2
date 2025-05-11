package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;

public class MatchMapper {

    public static FinishedMatchResponseDto fromEntity(MatchEntity match) {
        return FinishedMatchResponseDto.builder()
                .id(match.getId())
                .firstPlayer(PlayerMapper.fromEntity(match.getFirstPlayer()))
                .secondPlayer(PlayerMapper.fromEntity(match.getSecondPlayer()))
                .winner(PlayerMapper.fromEntity(match.getWinner()))
                .build();
    }

    public static OngoingMatchResponseDto fromModel(Match<Player> match) {
        return OngoingMatchResponseDto.builder()
                .firstPlayerScore(match.getScore().first())
                .secondPlayerScore(match.getScore().second())
                .build();
    }
}
