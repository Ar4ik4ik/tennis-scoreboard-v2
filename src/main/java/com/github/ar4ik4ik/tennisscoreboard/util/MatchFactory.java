package com.github.ar4ik4ik.tennisscoreboard.util;

import com.github.ar4ik4ik.tennisscoreboard.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicGameRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicMatchRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicSetRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules.ClassicTieBreakRules;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ClassicGameScoreStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ClassicSetScoringStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.MatchScoringStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.TieBreakScoringStrategy;

public class MatchFactory {

    public static Match<Player> classicMatch(Player firstPlayer, Player secondPlayer) {
        var gameRule = new ClassicGameRules(4, 3, 1);
        var setRule = new ClassicSetRules(6, 2, true);
        var matchRule = new ClassicMatchRules(2);
        var tieBreakRule = new ClassicTieBreakRules(2, 7);
        var setStrategy = new ClassicSetScoringStrategy(setRule);
        var gameStrategy = new ClassicGameScoreStrategy(gameRule);
        var tieBreakStrategy = new TieBreakScoringStrategy(tieBreakRule);
        var matchStrategy = new MatchScoringStrategy(matchRule);

        return Match.<Player>builder()
                .firstCompetitor(firstPlayer)
                .secondCompetitor(secondPlayer)
                .matchRule(matchRule)
                .setRule(setRule)
                .gameRule(gameRule)
                .strategy(matchStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .gameStrategy(gameStrategy)
                .setStrategy(setStrategy)
                .build();
    }

}
