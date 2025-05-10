package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;

public interface SetScoringStrategy<S> {

    ScoringResult<S> onGameWin(Score<S> previousScore, boolean isFirstPlayerWon);

    boolean shouldStartTieBreak(Score<S> score);
}
