package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.GameScoreStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ScoringResult;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.SetScoringStrategy;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Set<T extends Competitor> implements Competition<T, Integer, SetRule> {

    private final SetRule rules;
    private final GameRule gameRule;
    private final TieBreakRule tieBreakRule;

    private final T firstCompetitor, secondCompetitor;

    private Score<Integer> score = new IntScore(0, 0);

    private final SetScoringStrategy<Integer> strategy;
    private final GameScoreStrategy<GamePoint> gameStrategy;

    private boolean inTieBreak = false;
    private TieBreakGame<T> tieBreakGame;

    private boolean isFinished = false;
    private T winner;

    private final List<Game<T>> games;

    @Builder
    private Set(SetRule setRule, T firstCompetitor, T secondCompetitor, GameRule gameRule, TieBreakRule tieBreakRule,
                SetScoringStrategy<Integer> strategy, GameScoreStrategy<GamePoint> gameStrategy) {
        this.rules = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.gameStrategy = gameStrategy;
        this.strategy = strategy;
        this.winner = null;
        this.games = new ArrayList<>();
        this.games.add(Game.<T>builder()
                .gameRule(gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .strategy(gameStrategy)
                .build());
    }

    public void addPoint(T competitor) {
        if (isFinished) {
            throw new IllegalStateException("Set is already finished");
        }

        if (!inTieBreak) {
            var currentGame = games.getLast();
            currentGame.addPoint(competitor);
            if (currentGame.isFinished()) {
                var refreshedScore = refreshScore(competitor);
                if (refreshedScore.isFinished()) {
                    finishCompetition(competitor);
                } else if (strategy.shouldStartTieBreak(this.score)) {
                    inTieBreak = true;
                    tieBreakGame = TieBreakGame.<T>builder()
                            .firstCompetitor(firstCompetitor)
                            .secondCompetitor(secondCompetitor)
                            .tieBreakRule(tieBreakRule)
                            .build();
                } else {
                    startNextGame();
                }
            }
        } else {
            tieBreakGame.addPoint(competitor);
            if (tieBreakGame.isFinished()) {
                refreshScore(competitor);
                finishCompetition(competitor);
            }
        }
    }

    private void startNextGame() {
        games.add(Game.<T>builder()
                .gameRule(this.gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .strategy(gameStrategy)
                .build());
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private ScoringResult<Score<Integer>> refreshScore(T competitor) {
        var scoringResult = strategy.onGameWin(score, isFirst(competitor));
        this.score = scoringResult.score();
        return scoringResult;
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }
}