package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.mappers.PlayerEntityMapper;
import com.github.ar4ik4ik.tennisscoreboard.mappers.PlayerMapper;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Match;

import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class OngoingMatchesService {
    private final Map<String, Match<Player>> currentMatches = new ConcurrentHashMap<>();
    private final PlayerManagerService playerManagerService = PlayerManagerService.getInstance();
    @Getter
    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();
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
