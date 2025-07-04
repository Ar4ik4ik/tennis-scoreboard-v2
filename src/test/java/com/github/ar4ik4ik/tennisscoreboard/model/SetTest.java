package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.domain.Set;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicGameRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicSetRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicTieBreakRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetTest {

    private Player playerA;
    private Player playerB;
    private GameRule gameRule;
    private SetRule setRule;
    private TieBreakRule tieBreakRule;
    private SetScoringStrategy<Integer> setStrategy;
    private GameScoreStrategy<GamePoint> gameStrategy;
    private ScoringStrategy<Integer> tieBreakStrategy;

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
        gameRule = new ClassicGameRules(4, 3, 1);
        setRule = new ClassicSetRules(6,2,true);
        tieBreakRule = new ClassicTieBreakRules(2,7);
        setStrategy = new ClassicSetScoringStrategy(setRule);
        gameStrategy = new ClassicGameScoreStrategy(gameRule);
        tieBreakStrategy = new TieBreakScoringStrategy(tieBreakRule);
    }

    private void winGame(Set<Player> set, Player winner) {
        for (int i = 0; i < gameRule.pointsToWinGame(); i++) {
            set.addPoint(winner);
        }
    }

    @Test
    void testSetUnfinishedAfterFiveGames() {
        var set = Set.<Player>builder()
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .strategy(setStrategy)
                .setRule(setRule)
                .gameRule(gameRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        // A выигрывает 5 геймов подряд
        for (int i = 0; i < 5; i++) {
            winGame(set, playerA);
        }

        assertEquals(State.PLAYING, set.getState(), "Сет не должен быть завершён при 5:0");
        assertEquals(5, set.getScore().first(), "Счёт по первым геймам у A должен быть 5");
        assertEquals(0, set.getScore().second(), "Счёт у B должен оставаться 0");
    }

    @Test
    void testSetFinishedSixToZero() {
        var set = Set.<Player>builder()
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .strategy(setStrategy)
                .setRule(setRule)
                .gameRule(gameRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        // A выигрывает 6 геймов подряд
        for (int i = 0; i < 6; i++) {
            winGame(set, playerA);
        }

        assertEquals(State.FINISHED, set.getState(), "Сет должен завершиться при 6:0");
        assertEquals(playerA, set.getWinner(), "Победителем сета должен стать A");
    }

    @Test
    void testTieBreakModeAtSixAll() {
        var set = Set.<Player>builder()
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .strategy(setStrategy)
                .setRule(setRule)
                .gameRule(gameRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        // Доводим до 6:6
        for (int i = 0; i < 6; i++) {
            winGame(set, playerA);
            winGame(set, playerB);
        }

        assertEquals(State.TIEBREAK, set.getState(), "При 6:6 должен включиться режим тай-брейка");
        // Последующий вызов addPoint создаст TieBreakGame
        set.addPoint(playerA);
        assertEquals(State.TIEBREAK, set.getState());
        assertNotNull(set.getTieBreakGame());
    }

    @Test
    void testSetCompletionAfterTieBreak() {
        var set = Set.<Player>builder()
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .strategy(setStrategy)
                .setRule(setRule)
                .gameRule(gameRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        // Доводим до 6:6
        for (int i = 0; i < 6; i++) {
            winGame(set, playerA);
            winGame(set, playerB);
        }

        // 4 + 3
        winGame(set, playerA);
        set.addPoint(playerA);
        set.addPoint(playerA);
        assertEquals(State.TIEBREAK, set.getState());
        assertNull(set.getWinner());
        assertEquals(6, set.getScore().first());
        set.addPoint(playerA);
        assertEquals(State.FINISHED, set.getState());
        assertEquals(set.getFirstCompetitor(), set.getWinner());
        assertEquals(7, set.getScore().first());
    }

    @Test
    void testPlayingAfterMaxTieBreakPoints() {
        var set = Set.<Player>builder()
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .strategy(setStrategy)
                .setRule(setRule)
                .gameRule(gameRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        // Доводим до 6:6
        for (int i = 0; i < 6; i++) {
            winGame(set, playerA);
            winGame(set, playerB);
        }

        winGame(set, playerB);
        winGame(set, playerA);
        set.addPoint(playerA);
        set.addPoint(playerA);
        // PlayerA - 6
        set.addPoint(playerB);
        set.addPoint(playerB);
        set.addPoint(playerA);
        // PlayerA - 7; PlayerB - 6
        assertEquals(State.TIEBREAK, set.getState());
        assertFalse(set.getTieBreakGame().isFinished());
        set.addPoint(playerB);
        set.addPoint(playerB);
        set.addPoint(playerB);
        assertTrue(set.getTieBreakGame().isFinished());
        assertEquals(State.FINISHED, set.getState());
        assertEquals(playerB, set.getWinner());
    }
}
