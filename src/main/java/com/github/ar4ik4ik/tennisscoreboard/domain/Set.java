package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.GameScoreStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ScoringResult;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ScoringStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.SetScoringStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;

import static com.github.ar4ik4ik.tennisscoreboard.model.State.*;

@Log4j2
@Getter
public class Set<T extends Competitor> implements Competition<T, Integer, SetRule> {

    private final SetRule rules;
    private final GameRule gameRule;
    private final TieBreakRule tieBreakRule;

    private final T firstCompetitor, secondCompetitor;

    private Score<Integer> score = new IntScore(0, 0);

    private final SetScoringStrategy<Integer> strategy;
    private final GameScoreStrategy<GamePoint> gameStrategy;
    private final ScoringStrategy<Integer> tieBreakStrategy;

    private TieBreakGame<T> tieBreakGame;
    private State state = PLAYING;

    private T winner;

    private final LinkedList<Game<T>> gameHistory;

    @Builder
    private Set(SetRule setRule, T firstCompetitor, T secondCompetitor, GameRule gameRule, TieBreakRule tieBreakRule,
                SetScoringStrategy<Integer> strategy, GameScoreStrategy<GamePoint> gameStrategy, ScoringStrategy<Integer> tieBreakStrategy) {
        this.rules = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.gameStrategy = gameStrategy;
        this.strategy = strategy;
        this.tieBreakStrategy = tieBreakStrategy;
        this.winner = null;
        this.gameHistory = new LinkedList<>();
        startNewGame();
    }

    @Override
    public void finishCompetition(T winner) {
        state = FINISHED;
        this.winner = winner;
    }

    public Game<T> getCurrentGame() {
        return gameHistory.getLast();
    }

    public void addPoint(T competitor) {
        validateAddPoint(competitor);

        if (state == PLAYING) {
            var currentGame = gameHistory.getLast();
            currentGame.addPoint(competitor);
            if (currentGame.getGameState() == FINISHED) {
                handleSetCompletion(competitor);
            }
        } else {
            handleTieBreakPoint(competitor);
        }
    }

    private void handleSetCompletion(T competitor) {
        var refreshedScore = refreshScore(competitor);
        if (refreshedScore.isFinished()) {
            finishCompetition(competitor);
        } else if (strategy.shouldStartTieBreak(this.score)) {
            startTieBreak();
        } else {
            beginNextGameRound();
        }
    }

    private void handleTieBreakPoint(T competitor) {
        tieBreakGame.addPoint(competitor);
        if (tieBreakGame.isFinished()) {
            refreshScore(competitor);
            finishCompetition(competitor);
        }
    }

    private void startTieBreak() {
        state = TIEBREAK;
        tieBreakGame = TieBreakGame.<T>builder()
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .tieBreakRule(tieBreakRule)
                .strategy(tieBreakStrategy)
                .build();
    }

    private void beginNextGameRound() {
        startNewGame();
    }

    private void validateAddPoint(T competitor) {
        if (state == FINISHED) {
            throw new IllegalStateException("Set is already finished");
        }
        if (!competitor.equals(firstCompetitor) && !competitor.equals(secondCompetitor)) {
            throw new IllegalArgumentException("Received competitor not from this set");
        }
    }

    private void startNewGame() {
        this.gameHistory.add(Game.<T>builder()
                .gameRule(gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .scoreUpdateStrategy(gameStrategy)
                .build());
    }

    private boolean isFirstCompetitor(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private ScoringResult<Score<Integer>> refreshScore(T competitor) {
        var scoringResult = strategy.onGameWin(score, isFirstCompetitor(competitor));
        this.score = scoringResult.score();
        return scoringResult;
    }
}