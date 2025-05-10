package com.github.ar4ik4ik.tennisscoreboard.service;

import com.github.ar4ik4ik.tennisscoreboard.exceptions.PlayerNotFoundException;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerResponseDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.PlayerRepository;

import static com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerMapper.fromEntity;
import static com.github.ar4ik4ik.tennisscoreboard.util.mappers.PlayerMapper.fromRequestDto;

public class PlayerManagerService {

    private static final PlayerRepository playerRepository = PlayerRepository.getINSTANCE();
    private static final PlayerManagerService INSTANCE = new PlayerManagerService();

    public PlayerResponseDto getOrCreatePlayer(PlayerRequestDto requestDto) {
        try {
            return findPlayer(requestDto);
        } catch (PlayerNotFoundException e) {
            return createPlayer(requestDto);
        }
    }

    public PlayerResponseDto createPlayer(PlayerRequestDto requestDto) {
        return fromEntity(playerRepository.save(fromRequestDto(requestDto)));
    }

    public PlayerResponseDto findPlayer(PlayerRequestDto requestDto) {
        var foundedPlayer =  playerRepository.findByName(requestDto.name());
        if (foundedPlayer.isPresent()) {
            return fromEntity(foundedPlayer.get());
        } else {
            throw new PlayerNotFoundException(String.format("Player with name: %s, not found", requestDto.name()));
        }
    }

    public static PlayerManagerService getInstance() {
        return INSTANCE;
    }

    private PlayerManagerService() {
    }
}
