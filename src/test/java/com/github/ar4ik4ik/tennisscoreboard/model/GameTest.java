package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.domain.Game;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicGameRules;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint.FIFTEEN;
import static com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint.ZERO;

public class GameTest {

    private static Game<Player> initGame() {
        var firstPlayer = Player.builder()
                .name("John")
                .id(1)
                .build();

        var secondPlayer = Player.builder()
                .name("Mike")
                .id(2)
                .build();

        var rules = new ClassicGameRules(4, 3, 1);

        return Game.<Player>builder()
                .gameRule(rules)
                .firstCompetitor(firstPlayer)
                .secondCompetitor(secondPlayer)
                .build();
    }

    @Test
    public void testGameInitialization() {

        var firstPlayer = Player.builder()
                .name("John")
                .id(1)
                .build();

        var secondPlayer = Player.builder()
                .name("Mike")
                .id(2)
                .build();

        var rules = new ClassicGameRules(4, 3, 1);
        var game = Game.<Player>builder()
                .gameRule(rules)
                .firstCompetitor(firstPlayer)
                .secondCompetitor(secondPlayer)
                .build();

        Assertions.assertEquals(firstPlayer, game.getFirstCompetitor());
        Assertions.assertEquals(game.getSecondCompetitor(), secondPlayer);
        Assertions.assertEquals(ZERO, game.getScore().second());
        Assertions.assertNull(game.getWinner());
        Assertions.assertEquals(rules,game.getRules());
        Assertions.assertFalse(game.isFinished());
        Assertions.assertEquals(0, game.getAdvantageScoreCounter());
        Assertions.assertNull(game.getCurrentAdvantageCompetitor());
    }

    @Test
    public void finishGameTest() {
        var game = initGame();
        Assertions.assertFalse(game.isFinished());

        game.finishCompetition(game.getFirstCompetitor());

        Assertions.assertEquals(game.getFirstCompetitor(), game.getWinner());
        Assertions.assertTrue(game.isFinished());
    }

    @Test
    public void pointIncrementTest() {
        var game = initGame();

        Assertions.assertEquals(ZERO, game.getScore().first());
        game.addPoint(game.getFirstCompetitor());
        Assertions.assertEquals(FIFTEEN, game.getScore().second());
    }

    @Test
    public void deuceAndAdvantageTest() {
        var game = initGame();
        Assertions.assertNull(game.getCurrentAdvantageCompetitor());

        for (int i = 0; i < 3; i++) {
            game.addPoint(game.getFirstCompetitor());
            game.addPoint(game.getSecondCompetitor());
        }

        Assertions.assertNull(game.getCurrentAdvantageCompetitor());
        game.addPoint(game.getFirstCompetitor());
        Assertions.assertEquals(game.getFirstCompetitor(), game.getCurrentAdvantageCompetitor());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    public void winWithoutDeuceTest() {
        var game = initGame();
        Assertions.assertFalse(game.isFinished());
        for (int i = 0; i < 2; i++) {
            game.addPoint(game.getFirstCompetitor());
            game.addPoint(game.getSecondCompetitor());
        }
        Assertions.assertFalse(game.isFinished());
        game.addPoint(game.getFirstCompetitor());
        Assertions.assertFalse(game.isFinished());
        game.addPoint(game.getFirstCompetitor());
        Assertions.assertTrue(game.isFinished());
        Assertions.assertEquals(game.getFirstCompetitor(), game.getWinner());
    }

    @Test
    public void advantageTest() {
        var game = initGame();
        for (int i = 0; i < 3; i++) {
            game.addPoint(game.getFirstCompetitor());
            game.addPoint(game.getSecondCompetitor());
        }

        Assertions.assertNull(game.getCurrentAdvantageCompetitor());
        Assertions.assertEquals(0, game.getAdvantageScoreCounter());
        game.addPoint(game.getFirstCompetitor());
        Assertions.assertEquals(game.getFirstCompetitor(), game.getCurrentAdvantageCompetitor());
        Assertions.assertEquals(1, game.getAdvantageScoreCounter());
        game.addPoint(game.getSecondCompetitor());
        Assertions.assertEquals(0, game.getAdvantageScoreCounter());
        Assertions.assertNull(game.getCurrentAdvantageCompetitor());
        game.addPoint(game.getSecondCompetitor());
        Assertions.assertEquals(1, game.getAdvantageScoreCounter());
        Assertions.assertEquals(game.getSecondCompetitor(), game.getCurrentAdvantageCompetitor());
        game.addPoint(game.getSecondCompetitor());
        Assertions.assertEquals(2, game.getAdvantageScoreCounter());
        Assertions.assertEquals(game.getSecondCompetitor(), game.getCurrentAdvantageCompetitor());
        Assertions.assertTrue(game.isFinished());
    }
}
