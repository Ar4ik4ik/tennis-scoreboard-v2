package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.PlayerNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
public class MatchScoreCalculationService {

    private final OngoingMatchesService ongoingMatchesService;
    private final FinishedMatchesPersistenceService finishedMatchesService;

    public OngoingMatchResponseDto addPointToPlayer(ScoreIncreaseDto requestDto) throws MatchPersistenceException {
        var matchUUID = requestDto.matchUUID();
        var match = ongoingMatchesService.getMatch(matchUUID);
        match.addPoint(chooseScoringPlayer(requestDto, match));
        if (canFinishMatch(match)) {
            boolean removed = ongoingMatchesService.removeMatch(matchUUID, match);
            if (removed) {
                trySaveMatchOrThrowAndAbort(matchUUID, match);
            } else {
                 log.warn("Concurrent finish detected for match {}", matchUUID);
            }
        }
        return MatchMapper.fromModel(match, matchUUID);
    }

    private Player chooseScoringPlayer(ScoreIncreaseDto requestDto, Match<Player> match) {
        int firstId = match.getFirstCompetitor().getId();
        int secondId = match.getSecondCompetitor().getId();

        if (requestDto.scoringPlayerId() == firstId) {
            return match.getFirstCompetitor();
        } else if (requestDto.scoringPlayerId() == secondId) {
            return match.getSecondCompetitor();
        } else {
            throw new PlayerNotFoundException(
                    String.format("Player with id %d not found", requestDto.scoringPlayerId()));
        }
    }

    private boolean canFinishMatch(Match<Player> match) {
        return match.getState() == State.FINISHED;
    }

    private void trySaveMatchOrThrowAndAbort(String matchUUID, Match<Player> match) throws MatchPersistenceException {
        try {
            finishedMatchesService.saveMatch(match);
        } catch (MatchPersistenceException e) {
            ongoingMatchesService.insertMatch(matchUUID, match);
            throw new MatchPersistenceException(e.getMessage());
        }
    }
}
