package com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules;

public interface GameRule {

    int pointsToWinGame();
    int deuceThreshold();
    int advantageOffset();
}
