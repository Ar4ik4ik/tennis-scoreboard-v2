package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.util.MatchFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TennisGameTest {

    @Test
    public void gameInitializationTest() {

        var firstPlayer = Player.builder().id(1).name("John").build();
        var secondPlayer = Player.builder().id(2).name("Mike").build();
        var match = MatchFactory.classicMatch(firstPlayer, secondPlayer);
        Assertions.assertFalse(match.getSets().isEmpty());
        Assertions.assertFalse(match.getSets().getFirst().getGames().isEmpty());


    }

}
