package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MatchScoreCalculationService {
    @Builder.Default
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.builder().build();
    @Builder.Default
    private final FinishedMatchesPersistenceService finishedMatchesService = FinishedMatchesPersistenceService.builder().build();

    public OngoingMatchResponseDto addPointToPlayer(ScoreIncreaseDto requestDto) {
        var matchUUID = requestDto.matchUUID();
        var optMatch = ongoingMatchesService.getMatch(matchUUID);
        if (optMatch.isPresent()) {
            var currentMatch = optMatch.get();

            var scoringPlayer = currentMatch.getFirstCompetitor().getId().equals(requestDto.scoringPlayerId()) ?
                    currentMatch.getFirstCompetitor() : currentMatch.getSecondCompetitor();
            currentMatch.addPoint(scoringPlayer);
            if (currentMatch.getState() == State.FINISHED) {
                ongoingMatchesService.removeMatch(matchUUID);
                finishedMatchesService.saveMatch(currentMatch);
            }
            return MatchMapper.fromModel(currentMatch);
        } else {
            throw new MatchNotFoundException(String.format("Match with uuid %s not found", matchUUID));
        }
    }
}
