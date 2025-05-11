package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;

public class MatchEntityMapper {

    public static MatchEntity fromModel(Match<Player> match) {
        return MatchEntity.builder()
                .firstPlayerEntity(PlayerEntityMapper.fromModel(match.getFirstCompetitor()))
                .secondPlayerEntity(PlayerEntityMapper.fromModel(match.getSecondCompetitor()))
                .winner(PlayerEntityMapper.fromModel(match.getWinner()))
                .build();
    }

    public static Match<Player> fromEntity(MatchEntity match) {
        var buildedMatch = Match.<Player>builder()
                .firstCompetitor(PlayerEntityMapper.fromEntity(match.getFirstPlayerEntity()))
                .secondCompetitor(PlayerEntityMapper.fromEntity(match.getSecondPlayerEntity()))
                .build();
        // TODO Костыль с проставлением виннера, также нужно подумать насчет поля id,
        //  остальные параметры типа правил кажутся не особо важными
        buildedMatch.finishCompetition(PlayerEntityMapper.fromEntity(match.getWinner()));
        return buildedMatch;
    }
}
