package com.github.ar4ik4ik.tennisscoreboard.model;


public interface Competition<T extends Competitor, S, R> {

    T getFirstCompetitor();
    T getSecondCompetitor();
    S getFirstCompetitorScore();
    S getSecondCompetitorScore();
    boolean isFinished();
    R getRules();
    void finishCompetition(T winner);
    void addPoint(T competitor);
    T getWinner();

}
