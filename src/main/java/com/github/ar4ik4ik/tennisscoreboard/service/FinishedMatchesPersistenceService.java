package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.mappers.MatchEntityMapper;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.repository.MatchRepository;

public class FinishedMatchesPersistenceService {

    private final static MatchRepository repository = MatchRepository.getINSTANCE();

    // Сервис всегда принимает уже завершенный матч
    // TODO: Логика только для плаеров, решить как работать с командами
    public void saveMatch(Match<Player> match) {
        repository.save(MatchEntityMapper.fromModel(match));
    }

}
