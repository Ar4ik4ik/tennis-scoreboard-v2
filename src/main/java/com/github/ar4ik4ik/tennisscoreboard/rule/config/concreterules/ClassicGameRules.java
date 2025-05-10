package com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;


public record ClassicGameRules(int pointsToWinGame, int deuceThreshold, int advantageOffset) implements GameRule {
}
