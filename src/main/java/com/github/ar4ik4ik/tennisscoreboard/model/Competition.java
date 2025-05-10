package com.github.ar4ik4ik.tennisscoreboard.model;


public interface Competition<T extends Competitor, S , R> {

    T getFirstCompetitor();
    T getSecondCompetitor();
    Score<S> getScore();
    boolean isFinished();
    R getRules();
    void finishCompetition(T winner);
    void addPoint(T competitor);
    T getWinner();

}
