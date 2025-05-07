package com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.SetRule;

public record ClassicSetRules(int gamesToWinSet, int winByGames, boolean useTieBreak) implements SetRule {
}
