package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.MatchRepository;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.PlayerRepository;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.MatchEntityMapper;
import lombok.*;

@Builder
@AllArgsConstructor
public class FinishedMatchesPersistenceService {

    @Builder.Default
    private final MatchRepository matchRepository = new MatchRepository();

    // Сервис всегда принимает уже завершенный матч
    // TODO: Логика только для плаеров, решить как работать с командами
    public void saveMatch(Match<Player> match) {
        matchRepository.save(MatchEntityMapper.fromModel(match));
    }
}
