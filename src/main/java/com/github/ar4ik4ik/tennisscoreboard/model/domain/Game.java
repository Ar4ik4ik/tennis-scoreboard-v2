package com.github.ar4ik4ik.tennisscoreboard.model.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.GameRule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
public class Game<T extends Competitor> implements Competition<T, GamePoint, GameRule> {

    @Getter(AccessLevel.PUBLIC)
    private final GameRule rules;

    private final T firstCompetitor, secondCompetitor;

    @Setter(AccessLevel.PRIVATE)
    private GamePoint firstCompetitorScore = GamePoint.ZERO;

    @Setter(AccessLevel.PRIVATE)
    private GamePoint secondCompetitorScore = GamePoint.ZERO;

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
        var firstCompetitorValue = firstCompetitorScore.getValue();
        var secondCompetitorValue = secondCompetitorScore.getValue();

        return firstCompetitorValue >= rules.deuceThreshold() &&
                secondCompetitorValue >= rules.deuceThreshold() &&
                firstCompetitorValue == secondCompetitorValue;
    }

    public void addPoint(T competitor) {

        GamePoint scorerScore = competitor.equals(firstCompetitor) ? firstCompetitorScore : secondCompetitorScore;
        GamePoint opponentScore = competitor.equals(firstCompetitor) ? secondCompetitorScore : firstCompetitorScore;

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

    private void incrementPoint(T competitor) {
        if (competitor.equals(firstCompetitor)) {
            setFirstCompetitorScore(getFirstCompetitorScore().getNextPoint());
        } else {
            setSecondCompetitorScore(getSecondCompetitorScore().getNextPoint());
        }
    }
}
