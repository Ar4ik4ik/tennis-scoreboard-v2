package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

public interface GameScoreStrategy<S> {
    ScoringResult<S> onPoint(S firstScore, S secondScore, boolean isFirst);
    boolean isDeuce(S firstScore, S secondScore);
}
