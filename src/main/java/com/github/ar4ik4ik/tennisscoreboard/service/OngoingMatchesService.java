package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.mappers.PlayerMapper;
import com.github.ar4ik4ik.tennisscoreboard.model.Match;
import com.github.ar4ik4ik.tennisscoreboard.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@RequiredArgsConstructor
public class OngoingMatchesService {
    private final Map<String, Match> currentMatches = new ConcurrentHashMap<>();
    private final MatchRepository matchRepository = MatchRepository.getINSTANCE();
    private final PlayerManagerService playerManagerService = PlayerManagerService.getInstance();

    public String createNewMatch(MatchRequestDto requestDto) {
        String matchId = UUID.randomUUID().toString();
        var firstPlayer = PlayerMapper.fromResponseDto(playerManagerService
                .getOrCreatePlayer(requestDto.firstPlayer()));
        var secondPlayer = PlayerMapper.fromResponseDto(playerManagerService
                .getOrCreatePlayer(requestDto.secondPlayer()));


        return matchId;
    }

    public Optional<Match> getMatch(String matchId) {
        return Optional.ofNullable(currentMatches.get(matchId));
    }

    public void removeMatch(String matchId) {
        currentMatches.remove(matchId);
    }

}
