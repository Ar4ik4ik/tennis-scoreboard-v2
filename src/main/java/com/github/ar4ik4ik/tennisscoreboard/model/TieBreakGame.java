package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.TieBreakRule;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
public class TieBreakGame<T extends Competitor> implements Competition<T, Integer, TieBreakRule> {

    private final TieBreakRule rules;

    private final T firstCompetitor, secondCompetitor;

    @Setter(AccessLevel.PRIVATE)
    private Integer firstCompetitorScore = 0;

    @Setter(AccessLevel.PRIVATE)
    private Integer secondCompetitorScore = 0;

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
        int scorerScore = competitor.equals(firstCompetitor) ? firstCompetitorScore : secondCompetitorScore;
        int opponentScore = competitor.equals(firstCompetitor) ? secondCompetitorScore : firstCompetitorScore;

        return scorerScore >= rules.pointsToWin() && (scorerScore - opponentScore) >= rules.winBy();
    }

    private void incrementPoint(T competitor) {
        if (competitor.equals(firstCompetitor)) {
            firstCompetitorScore++;
        } else {
            secondCompetitorScore++;
        }
    }
}
