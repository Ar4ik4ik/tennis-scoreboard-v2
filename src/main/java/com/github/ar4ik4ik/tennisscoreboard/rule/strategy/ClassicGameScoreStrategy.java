package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GameScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;


public class ClassicGameScoreStrategy implements GameScoreStrategy<GamePoint> {

    private final int pointsToWin;
    private final int deuceThreshold;

    public ClassicGameScoreStrategy(GameRule rules) {
        this.deuceThreshold = rules.deuceThreshold();
        this.pointsToWin  = rules.pointsToWinGame();
    }

    @Override
    public ScoringResult<Score<GamePoint>> onPoint(Score<GamePoint> score, boolean isFirst) {

        var firstScore = score.first();
        var secondScore = score.second();

        // Счет не будет обновлен так как игра закончена
        if (!isDeuce(score) && canWinWithoutDeuce(firstScore, secondScore, isFirst)) {
            return new ScoringResult<>(new GameScore(firstScore, secondScore), true);
        }

        var newFirstScore = isFirst ? firstScore.getNextPoint() : firstScore;
        var newSecondScore = isFirst ? secondScore : secondScore.getNextPoint();

        return new ScoringResult<>(new GameScore(newFirstScore, newSecondScore), false);
    }

    @Override
    public boolean isDeuce(Score<GamePoint> score) {
        var firstScore = score.first();
        var secondScore = score.second();

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
