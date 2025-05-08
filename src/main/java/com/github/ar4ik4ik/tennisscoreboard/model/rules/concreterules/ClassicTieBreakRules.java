package com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.TieBreakRule;

public record ClassicTieBreakRules(int winBy, int pointsToWin) implements TieBreakRule {
}
