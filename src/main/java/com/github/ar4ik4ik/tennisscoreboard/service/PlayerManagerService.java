package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.PlayerRepository;
import com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerMapper.fromRequestDto;


@Log4j2
@AllArgsConstructor
public class PlayerManagerService {

    private final PlayerRepository playerRepository;

    public Player getOrCreatePlayer(PlayerRequestDto requestDto) {
        log.debug("Player request: {}", requestDto.toString());
        return playerRepository.findByPlayerName(requestDto.name())
                .map(PlayerEntityMapper::fromEntity)
                .orElseGet(() -> createPlayer(requestDto));
    }

    public Player createPlayer(PlayerRequestDto requestDto) {
        log.debug("Request for creating new player: {}", requestDto);
        return PlayerEntityMapper.fromEntity(playerRepository.save(fromRequestDto(requestDto)));
    }
}
