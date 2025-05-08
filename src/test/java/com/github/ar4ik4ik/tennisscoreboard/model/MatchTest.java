package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.MatchRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicGameRules;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicMatchRules;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicSetRules;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicTieBreakRules;
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
    }

    private void winSet(Match<Player> match, Player winner) {
        for (int j = 0; j < gameRule.pointsToWinGame(); j++) {
            for (int i = 0; i < setRule.gamesToWinSet(); i++) {
                match.addPoint(winner);
            }
        }
    }

    @Test
    void testPointsIncrementAfterSetWin() {
        var match = Match.<Player>builder()
                .matchRule(matchRule)
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        assertEquals(0, match.getFirstCompetitorScore());
        winSet(match, playerA);
        assertEquals(1, match.getFirstCompetitorScore());
        assertFalse(match.isFinished());
        winSet(match, playerA);
        assertEquals(2, match.getFirstCompetitorScore());
        assertTrue(match.isFinished());
    }

    @Test
    void testMatchFinishing() {
        var match = Match.<Player>builder()
                .matchRule(matchRule)
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(playerA)
                .secondCompetitor(playerB)
                .build();

        winSet(match, playerA);
        assertFalse(match.isFinished());
        winSet(match, playerB);
        assertFalse(match.isFinished());
        winSet(match, playerA);
        assertTrue(match.isFinished());
    }


}
