package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ScoringStrategy;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Getter(AccessLevel.PUBLIC)
public class TieBreakGame<T extends Competitor> implements Competition<T, Integer, TieBreakRule> {

    private final TieBreakRule rules;

    private final T firstCompetitor, secondCompetitor;

    private final ScoringStrategy<Integer> strategy;

    private Score<Integer> score = new IntScore(0, 0);

    @Getter(AccessLevel.PUBLIC)
    private boolean isFinished = false;

    private T winner = null;

    @Builder
    private TieBreakGame(TieBreakRule tieBreakRule, T firstCompetitor, T secondCompetitor, ScoringStrategy<Integer> strategy) {
        this.rules = tieBreakRule;
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
            throw new IllegalStateException("Tie-Break already finished");
        }

        var scoreResult = strategy.onPoint(score, isFirst(competitor));
        score = scoreResult.score();
        if (scoreResult.isFinished()) {
            finishCompetition(competitor);
        }
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

}
