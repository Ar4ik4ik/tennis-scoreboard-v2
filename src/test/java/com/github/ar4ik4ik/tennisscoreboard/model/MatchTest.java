package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.MatchRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicGameRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicMatchRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicSetRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicTieBreakRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MatchTest {

    private Player playerA;
    private Player playerB;
    private GameRule gameRule;
    private SetRule setRule;
    private TieBreakRule tieBreakRule;
    private MatchRule matchRule;
    private SetScoringStrategy<Integer> setStrategy;
    private GameScoreStrategy<GamePoint> gameStrategy;
    private ScoringStrategy<Integer> matchStrategy;
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
        matchRule = new ClassicMatchRules(2);
        tieBreakRule = new ClassicTieBreakRules(7,2);
        setStrategy = new ClassicSetScoringStrategy(setRule);
        gameStrategy = new ClassicGameScoreStrategy(gameRule);
        tieBreakStrategy = new TieBreakScoringStrategy(tieBreakRule);
        matchStrategy = new MatchScoringStrategy(matchRule);
    }

    private void winSet(Match<Player> match, Player winner) {
        for (int j = 0; j < gameRule.pointsToWinGame(); j++) {
            for (int i = 0; i < setRule.gamesToWinSet(); i++) {
                match.addPoint(winner);
            }
        }
    }

    @Test
    // Инкремент поинтов после победы в сете
    void testPointsIncrementAfterSetWin() {
        var match = Match.<Player>builder()
                .matchRule(matchRule)
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .strategy(matchStrategy)
                .setStrategy(setStrategy)
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        assertEquals(0, match.getScore().first(), "Начальный счет не может быть > 0");
        winSet(match, playerA);
        assertEquals(1, match.getScore().first(), "Счет после инкремента должен быть == 1");
        assertEquals(State.PLAYING, match.getState(), "Матч не может быть завершен, так как не соблюдено правило завершения");
        winSet(match, playerA);
        assertEquals(2, match.getScore().first(), "Счет должен быть == 2");
        assertEquals(State.FINISHED, match.getState(), "Матч должен быть завершен, так как соблюдено правило завершения");
    }

    @Test
    // Завершение после двух побед (в зависимости от Rules)
    void testMatchFinishing() {
        var match = Match.<Player>builder()
                .matchRule(matchRule)
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .strategy(matchStrategy)
                .setStrategy(setStrategy)
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        winSet(match, playerA);
        assertEquals(State.PLAYING, match.getState(), "Матч не может быть завершен, так как не соблюдено правило завершения");
        winSet(match, playerB);
        assertEquals(State.PLAYING, match.getState(), "Матч не может быть завершен, так как не соблюдено правило завершения");

        winSet(match, playerA);
        assertEquals(State.FINISHED, match.getState(), "Матч должен быть завершен, так как соблюдено правило завершения");

    }


}
