package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.model.dto.MatchRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.model.dto.PlayerRequestDto;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.service.OngoingMatchesService;
import com.github.ar4ik4ik.tennisscoreboard.util.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OngoingMatchesServiceTest {


    private Player playerA;
    private Player playerB;

    @BeforeEach
    void init() {
        playerA = Player.builder()
                .id(1)
                .name("A")
                .build();
        playerB = Player.builder()
                .id(2)
                .name("B")
                .build();
    }

    @Test
    void matchCreationTest() {
        SessionManager.getSession();
        var tx = SessionManager.getSession().getTransaction();
        var matchService = new OngoingMatchesService();
        String createdMatch = null;
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

        assertNotNull(matchService.getMatch(createdMatch).get());
    }

}
