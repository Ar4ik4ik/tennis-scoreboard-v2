package com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;

public record ClassicTieBreakRules(int winBy, int pointsToWin) implements TieBreakRule {
}
