package com.github.ar4ik4ik.tennisscoreboard.model.rules.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.GameRule;


public record ClassicGameRules(int pointsToWinGame, int deuceThreshold, int advantageOffset) implements GameRule {
}
