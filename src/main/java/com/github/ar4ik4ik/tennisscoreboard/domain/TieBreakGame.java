package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;


@Getter(AccessLevel.PUBLIC)
public class TieBreakGame<T extends Competitor> implements Competition<T, Integer, TieBreakRule> {

    private final TieBreakRule rules;

    private final T firstCompetitor, secondCompetitor;

    private Score<Integer> score = new IntScore(0, 0);

    @Getter(AccessLevel.PUBLIC)
    private boolean isFinished = false;

    private T winner = null;

    @Builder
    private TieBreakGame(TieBreakRule tieBreakRule, T firstCompetitor, T secondCompetitor) {
        this.rules = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }

    public void addPoint(T competitor) {
        incrementPoint(competitor);

        if (canWin(competitor)) {
            finishCompetition(competitor);
        }
    }

    private boolean canWin(T competitor) {
        int scorerScore = isFirst(competitor) ? score.first() : score.second();
        int opponentScore = isFirst(competitor) ? score.second() : score.first();

        return scorerScore >= rules.winBy() && (scorerScore - opponentScore) >= rules.pointsToWin();
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private void incrementPoint(T competitor) {
        score = score.increment(isFirst(competitor));
    }
}
