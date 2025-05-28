package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;

public class TieBreakScoringStrategy implements ScoringStrategy<Integer> {

    private final int pointsToWin, winBy;

    public TieBreakScoringStrategy(TieBreakRule rule) {
        this.pointsToWin = rule.pointsToWin();
        this.winBy = rule.winBy();
    }

    @Override
    public ScoringResult<Score<Integer>> onPoint(Score<Integer> score, boolean isFirst) {

        var newScore = score.increment(isFirst);
        if (canWin(newScore, isFirst)) {
            return new ScoringResult<>(newScore, true);
        } else {
            return new ScoringResult<>(newScore, false);
        }
    }

    private boolean canWin(Score<Integer> score, boolean isFirst) {
        int scorerScore = isFirst ? score.first() : score.second();
        int opponentScore = isFirst ? score.second() : score.first();

        return scorerScore >= pointsToWin && (scorerScore - opponentScore) >= winBy;
    }
}
