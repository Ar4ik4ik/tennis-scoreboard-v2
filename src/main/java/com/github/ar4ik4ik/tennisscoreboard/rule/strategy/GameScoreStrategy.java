package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;

public interface GameScoreStrategy<S> extends ScoringStrategy<S> {

    boolean isDeuce(Score<S> score);
}
