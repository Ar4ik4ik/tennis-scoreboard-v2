package com.github.ar4ik4ik.tennisscoreboard.model;


import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;

public interface Competition<T extends Competitor, S , R> {

    T getFirstCompetitor();
    T getSecondCompetitor();
    Score<S> getScore();
    /**
     * Generic type R means Competition need to impl one of rules (Game, Match, etc.)
     * Rules need to calc points and make decision finish or not
     * */
    R getRules();
    void finishCompetition(T winner);
    void addPoint(T competitor);
    T getWinner();

}
