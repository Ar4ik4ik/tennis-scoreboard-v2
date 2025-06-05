package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.persistence.repository.PlayerRepository;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import com.github.ar4ik4ik.tennisscoreboard.service.PlayerManagerService;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OngoingMatchesServiceTest {


    private OngoingMatchesService matchService;

    @BeforeEach
    void init() {
        Player playerA = Player.builder()
                .id(1)
                .name("A")
                .build();
        Player playerB = Player.builder()
                .id(2)
                .name("B")
                .build();
        matchService = new OngoingMatchesService(
                new PlayerManagerService(
                        new PlayerRepository()), new MatchFactory());
    }

    @Test
    void matchCreationTest() {
        SessionManager.getSession();
        var tx = SessionManager.getSession().getTransaction();
        String createdMatch;
        try {
            tx.begin();
            createdMatch = matchService.createNewMatch(MatchRequestDto.builder()
                    .firstPlayer(PlayerRequestDto.builder().name("A").build())
                    .secondPlayer(PlayerRequestDto.builder().name("B").build())
                    .build());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException(e);
        }

        assertNotNull(matchService.getMatch(createdMatch));
    }

}
