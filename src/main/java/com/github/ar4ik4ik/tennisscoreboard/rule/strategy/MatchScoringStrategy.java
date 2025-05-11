package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.MatchRule;

public class MatchScoringStrategy implements ScoringStrategy<Integer> {

    private final int setsToWinMatch;

    public MatchScoringStrategy(MatchRule rule) {
        this.setsToWinMatch = rule.setsToWinMatch();
    }

    @Override
    public ScoringResult<Score<Integer>> onPoint(Score<Integer> score, boolean isFirst) {

        var newScore = score.increment(isFirst);
        if (canFinishMatch(newScore)) {
            return new ScoringResult<>(newScore, true);
        } else {
            return new ScoringResult<>(newScore, false);
        }
    }

    private boolean canFinishMatch(Score<Integer> score) {
        return score.first() >= setsToWinMatch || score.second() >= setsToWinMatch;
    }
}
