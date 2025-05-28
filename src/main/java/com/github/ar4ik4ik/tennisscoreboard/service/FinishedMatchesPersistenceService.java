package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.FinishedMatchResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepository;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchEntityMapper;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@AllArgsConstructor
public class FinishedMatchesPersistenceService {

    private final MatchRepository matchRepository;

    // Сервис всегда принимает уже завершенный матч
    // TODO: Логика только для плаеров, решить как работать с командами
    public void saveMatch(Match<Player> match) throws MatchPersistenceException {
        var entity = MatchEntityMapper.fromModel(match);
        try {
            matchRepository.save(entity);
            log.info("Saved finished match {}", match);
        } catch (RuntimeException ex) {
            log.error("Error saving match {}", match, ex);
            throw new MatchPersistenceException("Failed to save match " + match, ex);
        }
    }

    public List<FinishedMatchResponseDto> getAllFinishedMatches(int offset, int limit) {
        return matchRepository.findAll(offset, limit)
                .stream()
                .map(MatchEntityMapper::fromEntity)
                .toList();
    }

    public List<FinishedMatchResponseDto> getAllFinishedMatchesByName(String name, int offset, int limit) {
        return matchRepository.findAllByPlayerName(name, offset, limit)
                .stream()
                .map(MatchEntityMapper::fromEntity)
                .toList();
    }

}
