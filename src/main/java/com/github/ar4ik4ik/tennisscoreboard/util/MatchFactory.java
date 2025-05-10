package com.github.ar4ik4ik.tennisscoreboard.util;

import com.github.ar4ik4ik.tennisscoreboard.model.domain.Match;
import com.github.ar4ik4ik.tennisscoreboard.model.domain.Player;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicGameRules;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicMatchRules;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules.ClassicSetRules;

public class MatchFactory {

    public static Match<Player> classicMatch(Player firstPlayer, Player secondPlayer) {
        var gameRule = new ClassicGameRules(4, 3, 1);
        var setRule = new ClassicSetRules(6, 2, true);
        var matchRule = new ClassicMatchRules(2);
        return Match.<Player>builder()
                .firstCompetitor(firstPlayer)
                .secondCompetitor(secondPlayer)
                .matchRule(matchRule)
                .setRule(setRule)
                .gameRule(gameRule)
                .build();
    }

}
