package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.*;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
public class Game<T extends Competitor> implements Competition<T, GamePoint, GameRule> {

    @Getter(AccessLevel.PUBLIC)
    private final GameRule rules;

    private final T firstCompetitor, secondCompetitor;

    private Score<GamePoint> score = new GameScore(GamePoint.ZERO, GamePoint.ZERO);

    private int advantageScoreCounter = 0;

    @Getter(AccessLevel.PUBLIC)
    private boolean isFinished = false;

    @Setter(AccessLevel.PRIVATE)
    private T currentAdvantageCompetitor;

    private T winner = null;

    @Builder
    private Game(GameRule gameRule, T firstCompetitor, T secondCompetitor) {
        this.rules = gameRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
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
                setCurrentAdvantageCompetitor(null);
            }
        }
    }

    private boolean isDeuce() {
        var firstCompetitorScore = score.first().getValue();
        var secondCompetitorScore = score.second().getValue();

        return firstCompetitorScore >= rules.deuceThreshold() &&
                secondCompetitorScore >= rules.deuceThreshold() &&
                firstCompetitorScore == secondCompetitorScore;
    }

    public void addPoint(T competitor) {

        GamePoint scorerScore = isFirst(competitor) ? score.first() : score.second();
        GamePoint opponentScore = isFirst(competitor) ? score.second() : score.first();

        if (isDeuce()) {
            handleAdvantage(competitor);
        } else if (canWinWithoutDeuce(scorerScore, opponentScore)) {
            finishCompetition(competitor);
        } else {
            incrementPoint(competitor);
        }
    }

    private boolean canWinWithoutDeuce(GamePoint currentScore, GamePoint otherScore) {
        return currentScore.getValue() + 1 >= rules.pointsToWinGame() &&
                otherScore.getValue() < rules.pointsToWinGame();
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private void incrementPoint(T competitor) {
        score = score.increment(isFirst(competitor));
    }
}
