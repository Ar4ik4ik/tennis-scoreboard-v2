package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerEntityMapper;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class OngoingMatchesService {
    private final Map<String, Match<Player>> currentMatches = new ConcurrentHashMap<>();
    private final PlayerManagerService playerManagerService;

    public String createNewMatch(MatchRequestDto requestDto) {
        String matchId = UUID.randomUUID().toString();

        var firstPlayer = PlayerEntityMapper.fromEntity(
                PlayerMapper.fromResponseDto(
                        playerManagerService.getOrCreatePlayer(requestDto.firstPlayer())));

        var secondPlayer = PlayerEntityMapper.fromEntity(
                PlayerMapper.fromResponseDto(
                        playerManagerService.getOrCreatePlayer(requestDto.secondPlayer())));

        // TODO: Костыль с созданием матча через фактори, кажется временным решением, чтобы не передавать из контроллера много параметров
        this.currentMatches.put(matchId, MatchFactory.classicMatch(firstPlayer, secondPlayer));

        return matchId;
    }

    public Optional<Match<Player>> getMatch(String matchId) {
        return Optional.ofNullable(currentMatches.get(matchId));
    }

    public void removeMatch(String matchId) {
        currentMatches.remove(matchId);
    }

}
