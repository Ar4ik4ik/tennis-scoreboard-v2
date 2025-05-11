package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.ScoreIncreaseDto;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchMapper;

public class MatchScoreCalculationService {

    private final static OngoingMatchesService ONGOING_MATCHES_SERVICE = OngoingMatchesService.getINSTANCE();

    public OngoingMatchResponseDto addPointToPlayer(ScoreIncreaseDto requestDto) {
        var matchUUID = requestDto.matchUUID();
        var optMatch = ONGOING_MATCHES_SERVICE.getMatch(matchUUID);
        if (optMatch.isPresent()) {
            var currentMatch = optMatch.get();
            if (currentMatch.getState() == State.FINISHED) {
                ONGOING_MATCHES_SERVICE.removeMatch(matchUUID);
            }
            return MatchMapper.fromModel(currentMatch);
        } else {
            throw new MatchNotFoundException(String.format("Match with uuid %s not found", matchUUID));
        }
    }
}
