package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.OngoingMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class OngoingMatchesService {

    private final Map<String, Match<Player>> currentMatches = new ConcurrentHashMap<>();
    private final PlayerManagerService playerManagerService;
    private final MatchFactory matchFactory;

    public String createNewMatch(MatchRequestDto requestDto) {
        String matchId = UUID.randomUUID().toString();

        var firstPlayer = playerManagerService.getOrCreatePlayer(requestDto.firstPlayer());
        var secondPlayer = playerManagerService.getOrCreatePlayer(requestDto.secondPlayer());

        // TODO: Костыль с созданием матча через фактори, кажется временным решением, чтобы не передавать из контроллера много параметров
        this.currentMatches.put(matchId, matchFactory.classicMatch(firstPlayer, secondPlayer));
        return matchId;
    }

    public OngoingMatchResponseDto getOngoingMatch(String matchId) {
        var foundMatch = currentMatches.get(matchId);
        if (foundMatch == null) {
            throw new MatchNotFoundException(String.format("Match with id: %s not found", matchId));
        } else {
            return MatchMapper.fromModel(foundMatch, matchId);
        }
    }

    public Match<Player> getMatch(String matchId) {
        var foundMatch = currentMatches.get(matchId);
        if (foundMatch == null) {
            throw new MatchNotFoundException(String.format("Match with id: %s not found", matchId));
        } else {
            return foundMatch;
        }
    }

    public boolean removeMatch(String matchId, Match<Player> match) {
        currentMatches.remove(matchId, match);
        return true;
    }
}
