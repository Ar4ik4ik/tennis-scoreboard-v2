package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;

public class GameScoringStrategy implements ScoringStrategy<GamePoint> {

    private final int deuceThreshold, pointsToWin;

    public GameScoringStrategy(GameRule rules) {
        this.deuceThreshold = rules.deuceThreshold();
        this.pointsToWin  = rules.pointsToWinGame();
    }

    @Override
    public ScoringResult<GamePoint> onPoint(GamePoint firstScore, GamePoint secondScore, boolean isFirst) {

        if (!isDeuce(firstScore, secondScore) && canWinWithoutDeuce(firstScore, secondScore, isFirst)) {
            return new ScoringResult<>(firstScore, secondScore, true);
        }

        var newFirstScore = isFirst ? firstScore.getNextPoint() : firstScore;
        var newSecondScore = isFirst ? secondScore : secondScore.getNextPoint();

        return new ScoringResult<>(newFirstScore, newSecondScore, false);
    }

    public boolean isDeuce(GamePoint firstScore, GamePoint secondScore) {
        return firstScore.getValue() >= deuceThreshold &&
                secondScore.getValue() >= deuceThreshold &&
                firstScore.getValue() == secondScore.getValue();
    }

    private boolean canWinWithoutDeuce(GamePoint firstScore, GamePoint secondScore, boolean isFirst) {
        int current = isFirst ? firstScore.getValue() : secondScore.getValue();
        int opponent = isFirst ? secondScore.getValue() : firstScore.getValue();
        return current + 1 >= pointsToWin && opponent < pointsToWin;
    }
}
