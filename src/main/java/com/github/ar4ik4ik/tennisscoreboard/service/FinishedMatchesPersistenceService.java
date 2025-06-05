package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.exceptions.MatchPersistenceException;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepository;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepositoryImpl;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@AllArgsConstructor
public class FinishedMatchesPersistenceService {

    private final MatchRepositoryImpl matchRepository;

    // Сервис всегда принимает уже завершенный матч
    public void saveMatch(Match<Player> match) throws MatchPersistenceException {
        var entity = MatchEntityMapper.fromModel(match);
        try {
            matchRepository.save(entity);
            log.info("Saved finished match {}", match);
        } catch (RuntimeException ex) {
            log.error("Error saving match {}", match, ex);
            throw new MatchPersistenceException("Failed to save match " + match + " " + ex.getMessage());
        }
    }

}
