package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.PlayerNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private final FinishedMatchesPersistenceService finishedMatchesService;

    public MatchResponseDto addPointToPlayer(ScoreIncreaseDto requestDto) throws MatchPersistenceException {
        var matchUUID = requestDto.matchUUID();
        var match = ongoingMatchesService.getMatch(matchUUID);

        match.addPoint(chooseScoringPlayer(requestDto, match));
        if (match.getState() == State.FINISHED) {
            boolean removed = ongoingMatchesService.removeMatch(matchUUID, match);
            if (removed) {
                finishedMatchesService.saveMatch(match);
            } else {
                 log.warn("Concurrent finish detected for match {}", matchUUID);
            }
        }

        return MatchMapper.fromModel(match);
    }

    private Player chooseScoringPlayer(ScoreIncreaseDto requestDto, Match<Player> match) {
        if (requestDto.scoringPlayerId() == match.getFirstCompetitor().getId()) {
            return match.getFirstCompetitor();
        } else if (requestDto.scoringPlayerId() == match.getSecondCompetitor().getId()) {
            return match.getSecondCompetitor();
        } else {
            throw new PlayerNotFoundException(
                    String.format("Player with id %d not found", requestDto.scoringPlayerId()));
        }
    }
}
