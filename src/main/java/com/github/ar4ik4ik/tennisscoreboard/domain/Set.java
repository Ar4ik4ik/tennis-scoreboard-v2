package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
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

    private boolean inTieBreak = false;
    private TieBreakGame<T> tieBreakGame;

    private boolean isFinished = false;
    private T winner;

    private final List<Game<T>> games;

    @Builder
    private Set(SetRule setRule, T firstCompetitor, T secondCompetitor, GameRule gameRule, TieBreakRule tieBreakRule,
                SetScoringStrategy<Integer> strategy) {
        this.rules = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.strategy = strategy;
        this.winner = null;
        this.games = new ArrayList<>();
        this.games.add(Game.<T>builder()
                .gameRule(gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    public void addPoint(T competitor) {
        if (isFinished) {
            throw new IllegalStateException("Set is already finished");
        }

        if (!inTieBreak) {
            var scoringResult = strategy.onGameWin(score, isFirst(competitor));
            this.score = new IntScore(scoringResult.first(), scoringResult.second());
            if (scoringResult.isFinished()) {
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
        } else {
            tieBreakGame.addPoint(competitor);
            if (tieBreakGame.isFinished()) {
                finishCompetition(competitor);
            }
        }
    }

    private void startNextGame() {
        games.add(Game.<T>builder()
                .gameRule(this.gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }
}