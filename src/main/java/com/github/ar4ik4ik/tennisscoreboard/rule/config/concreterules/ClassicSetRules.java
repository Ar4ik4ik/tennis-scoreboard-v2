package com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;

public record ClassicSetRules(int gamesToWinSet, int winByGames, boolean useTieBreak) implements SetRule {
}
