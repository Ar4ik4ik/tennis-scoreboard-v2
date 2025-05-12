package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.*;

public class MatchMapper {

    public static MatchResponseDto fromModel(Match<Player> match) {
        return MatchResponseDto.builder()
                .firstPlayer(PlayerResponseDto.builder()
                        .id(match.getFirstCompetitor().getId())
                        .name(match.getFirstCompetitor().getName())
                        .build())
                .secondPlayer(PlayerResponseDto.builder()
                        .id(match.getSecondCompetitor().getId())
                        .name(match.getSecondCompetitor().getName())
                        .build())

                .state(match.getState().toString())
                .score(ScoreDto.builder()
                        .firstPlayerScore(match.getScore().first())
                        .secondPlayerScore(match.getScore().second())
                        .build())
                .sets(match.getSets())
                .build();
    }
}
