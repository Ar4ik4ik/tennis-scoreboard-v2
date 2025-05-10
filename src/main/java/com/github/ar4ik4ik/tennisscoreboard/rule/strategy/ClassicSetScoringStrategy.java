package com.github.ar4ik4ik.tennisscoreboard.rule.strategy;

import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;

public class ClassicSetScoringStrategy implements SetScoringStrategy<Integer> {

    private final int gamesToWin;
    private final int winByGames;
    private final boolean useTieBreak;

    public ClassicSetScoringStrategy(SetRule rule) {
        this.gamesToWin = rule.gamesToWinSet();
        this.winByGames = rule.winByGames();
        this.useTieBreak = rule.useTieBreak();
    }


    @Override
    public ScoringResult<Integer> onGameWin(Score<Integer> previousScore, boolean isFirstPlayerWon) {

        var newSetScore = isFirstPlayerWon ? previousScore.incrementFirst()
                : previousScore.incrementSecond();

        if (canWinSet(newSetScore, isFirstPlayerWon)) {
            return new ScoringResult<>(newSetScore.first(), newSetScore.second(), true);
        }

        return new ScoringResult<>(newSetScore.first(), newSetScore.second(), false);
    }

    @Override
    public boolean shouldStartTieBreak(Score<Integer> score) {
        if (!useTieBreak) {
            return false;
        } else {
            return score.first() == gamesToWin && score.second() == gamesToWin;
        }
    }

    private boolean canWinSet(Score<Integer> score, boolean isFirstPlayerWon) {
        int scorerScore = isFirstPlayerWon ? score.first() : score.second();
        int opponentScore = isFirstPlayerWon ? score.second() : score.first();

        return scorerScore >= gamesToWin && scorerScore - opponentScore >= winByGames;

    }
}
