package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepositoryImpl;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchEntityMapper;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FinishedMatchesQueryService {

    private final MatchRepositoryImpl matchRepository;

    public int getTotalPages(int maxItems) {
        long objectCount = matchRepository.getObjectCount();
        return (int) Math.ceil((double) objectCount / maxItems);
    }

    public int getTotalPages(int maxItems, String playerName) {
        long objectCount = matchRepository.getObjectCount(playerName.trim());
        return (int) Math.ceil((double) objectCount / maxItems);
    }

    public List<FinishedMatchResponseDto> getAllFinishedMatches(int currentPage, int limit) {

        int offset = calcOffset(currentPage, limit);

        return matchRepository.findAll(offset, limit)
                .stream()
                .map(MatchEntityMapper::fromEntity)
                .toList();
    }

    public List<FinishedMatchResponseDto> getAllFinishedMatchesByName(String name, int currentPage, int limit) {

        int offset = calcOffset(currentPage, limit);

        return matchRepository.findAllByPlayerName(name.trim(), offset, limit)
                .stream()
                .map(MatchEntityMapper::fromEntity)
                .toList();
    }

    private int calcOffset(int currentPage, int limit) {
        return (currentPage * limit) - limit;
    }
}
