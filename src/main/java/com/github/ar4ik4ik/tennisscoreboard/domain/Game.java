package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GameScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.GameScoreStrategy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.github.ar4ik4ik.tennisscoreboard.model.State.FINISHED;
import static com.github.ar4ik4ik.tennisscoreboard.model.State.PLAYING;

@Getter(AccessLevel.PUBLIC)
public class Game<T extends Competitor> implements Competition<T, GamePoint, GameRule> {

    @Getter(AccessLevel.PUBLIC)
    private final GameRule rules;
    private final GameScoreStrategy<GamePoint> scoreUpdateStrategy;
    private State gameState = PLAYING;
    private final T firstCompetitor, secondCompetitor;
    private Score<GamePoint> currentGameScore = new GameScore(GamePoint.ZERO, GamePoint.ZERO);
    private int advantageScoreCounter = 0;

    @Setter(AccessLevel.PRIVATE)
    private T currentAdvantageCompetitor;
    private T winner;

    @Builder
    private Game(GameRule gameRule, T firstCompetitor, T secondCompetitor, GameScoreStrategy<GamePoint> scoreUpdateStrategy) {
        this.rules = gameRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.scoreUpdateStrategy = scoreUpdateStrategy;
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        gameState = FINISHED;
    }

    public void addPoint(T competitor) {
        validateAddPoint(competitor);

        if (!scoreUpdateStrategy.isDeuce(currentGameScore)) {
            processRegularPoint(competitor);
        } else {
            handleAdvantage(competitor);
        }
    }

    public String getGameScore() {
        if (!scoreUpdateStrategy.isDeuce(currentGameScore)) {
            return formatRegularScore();
        } else {
            return formatDeuceScore();
        }
    }

    private String formatRegularScore() {
       return String.format("%s-%s", currentGameScore.first().getDisplayValue(), currentGameScore.second().getDisplayValue());
    }

    private String formatDeuceScore() {
        if (currentAdvantageCompetitor == null) {
            return "DEUCE-DEUCE";
        }
        return currentAdvantageCompetitor.equals(firstCompetitor) ? "AD-40" : "40-AD";
    }

    private void processRegularPoint(T competitor) {
        boolean isFirst = competitor.equals(firstCompetitor);
        var scoringResult = scoreUpdateStrategy.onPoint(currentGameScore, isFirst);
        this.currentGameScore = scoringResult.score();

        if (scoringResult.isFinished()) {
            finishCompetition(competitor);
        }
    }

    private void validateAddPoint(T competitor) {
        if (gameState == FINISHED) {
            throw new IllegalStateException("Game is already finished");
        }
        if (!competitor.equals(firstCompetitor) && !competitor.equals(secondCompetitor)) {
            throw new IllegalArgumentException("Received competitor not from this game");
        }
    }

    private void handleAdvantage(T competitor) {
        if (getCurrentAdvantageCompetitor() == null) {
            startAdvantage(competitor);
        } else {
            processExistingAdvantage(competitor);
        }
    }

    private void startAdvantage(T competitor) {
        setCurrentAdvantageCompetitor(competitor);
        advantageScoreCounter++;
    }

    private boolean hasEnoughAdvantagePoints() {
        return advantageScoreCounter >= rules.advantageOffset();
    }

    private void processExistingAdvantage(T competitor) {
        if (getCurrentAdvantageCompetitor().equals(competitor)) {
            advantageScoreCounter++;
            if (hasEnoughAdvantagePoints()) {
                finishCompetition(competitor);
            }
        } else {
            advantageScoreCounter--;
            if (advantageScoreCounter == 0) {
                currentAdvantageCompetitor = null;
            }
        }
    }
}
