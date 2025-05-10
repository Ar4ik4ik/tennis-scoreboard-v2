package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

public interface ScoringStrategy <S> {
    ScoringResult<S> onPoint(S firstScore, S secondScore, boolean isFirst);
}
