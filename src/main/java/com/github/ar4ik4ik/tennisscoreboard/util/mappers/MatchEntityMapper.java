package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.persistence.entity.MatchEntity;

public class MatchEntityMapper {

    public static MatchEntity fromModel(Match<Player> match) {
        return MatchEntity.builder()
                .firstPlayer(PlayerEntityMapper.fromModel(match.getFirstCompetitor()))
                .secondPlayer(PlayerEntityMapper.fromModel(match.getSecondCompetitor()))
                .winner(PlayerEntityMapper.fromModel(match.getWinner()))
                .build();
    }

    public static Match<Player> fromEntity(MatchEntity match) {
        var buildedMatch = Match.<Player>builder()
                .firstCompetitor(PlayerEntityMapper.fromEntity(match.getFirstPlayer()))
                .secondCompetitor(PlayerEntityMapper.fromEntity(match.getSecondPlayer()))
                .build();
        // TODO Костыль с проставлением виннера, также нужно подумать насчет поля id,
        //  остальные параметры типа правил кажутся не особо важными
        buildedMatch.finishCompetition(PlayerEntityMapper.fromEntity(match.getWinner()));
        return buildedMatch;
    }
}
