package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;

public interface ScoringStrategy<S> {

    ScoringResult<Score<S>> onPoint(Score<S> score, boolean isFirst);
}
