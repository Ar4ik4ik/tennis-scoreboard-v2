package com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules;

public interface SetRule {

    int gamesToWinSet();
    int winByGames();
    boolean useTieBreak();
}
