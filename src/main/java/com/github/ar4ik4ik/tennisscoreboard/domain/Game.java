package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.*;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GameScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.GameScoringStrategy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
public class Game<T extends Competitor> implements Competition<T, GamePoint, GameRule> {

    @Getter(AccessLevel.PUBLIC)
    private final GameRule rules;

    private final GameScoringStrategy strategy;

    private final T firstCompetitor, secondCompetitor;

    private Score<GamePoint> score = new GameScore(GamePoint.ZERO, GamePoint.ZERO);

    private int advantageScoreCounter = 0;

    @Getter(AccessLevel.PUBLIC)
    private boolean isFinished = false;

    @Setter(AccessLevel.PRIVATE)
    private T currentAdvantageCompetitor;

    private T winner = null;

    @Builder
    private Game(GameRule gameRule, T firstCompetitor, T secondCompetitor, GameScoringStrategy strategy) {
        this.rules = gameRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.strategy = strategy;
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }

    public void addPoint(T competitor) {

        if (isFinished) {
            throw new IllegalStateException("Game is already finished");
        }
        boolean isFirst = competitor.equals(firstCompetitor);

        var scoringResult = strategy.onPoint(score.first(), score.second(), isFirst);
        this.score = new GameScore(scoringResult.first(), scoringResult.second());

        if (scoringResult.isFinished()) {
            finishCompetition(competitor);
            return;
        }

        if (strategy.isDeuce(score.first(), score.second())) {
            handleAdvantage(competitor);
        }

    }

    private void handleAdvantage(T competitor) {
        if (getCurrentAdvantageCompetitor() == null) {
            setCurrentAdvantageCompetitor(competitor);
            advantageScoreCounter++;
        } else if (getCurrentAdvantageCompetitor().equals(competitor)) {
            if (advantageScoreCounter >= rules.advantageOffset()) {
                advantageScoreCounter++;
                finishCompetition(competitor);
            } else {
                advantageScoreCounter++;
            }
        } else {
            advantageScoreCounter--;
            if (advantageScoreCounter == 0) {
                currentAdvantageCompetitor = null;
            }
        }
    }
}
