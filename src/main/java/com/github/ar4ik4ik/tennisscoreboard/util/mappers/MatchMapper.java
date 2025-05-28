package com.github.ar4ik4ik.tennisscoreboard.util.mappers;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;

public class MatchMapper {

    public static OngoingMatchResponseDto fromModel(Match<Player> match, String matchId) {
        boolean isTieBreak = match.getCurrentSet().getState().equals(State.TIEBREAK);

        String[] scoreParts = isTieBreak
                ? match.getCurrentSet().getTieBreakGame().getTieBreakScore().split("-")
                : match.getCurrentSet().getCurrentGame().getGameScore().split("-");


        return OngoingMatchResponseDto.builder()
                .matchId(matchId)
                .player1(PlayerMapper.fromModel(match.getFirstCompetitor()))
                .player2(PlayerMapper.fromModel(match.getSecondCompetitor()))
                .player1Sets(match.getScore().first())
                .player2Sets(match.getScore().second())
                .player1GamesInCurrentSet(match.getCurrentSet().getScore().first())
                .player2GamesInCurrentSet(match.getCurrentSet().getScore().second())
                .player1Points(scoreParts[0])
                .player2Points(scoreParts[1])
                .matchState(match.getState())
                .isTieBreak(isTieBreak)
                .build();
    }
}
