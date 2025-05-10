package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.MatchRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Match<T extends Competitor> implements Competition<T, IntScore, MatchRule> {

    private final MatchRule rules;
    private final SetRule setRule;
    private final GameRule gameRule;
    private final TieBreakRule tieBreakRule;

    private final T firstCompetitor, secondCompetitor;

    private Score<Integer> score = new IntScore(0, 0);

    private boolean isFinished = false;
    private T winner = null;

    private final List<Set<T>> sets = new ArrayList<>();

    @Builder
    private Match(MatchRule matchRule, T firstCompetitor, T secondCompetitor,
                  SetRule setRule, GameRule gameRule, TieBreakRule tieBreakRule) {
        this.rules = matchRule;
        this.setRule = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.sets.add(Set.<T>builder()
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    @Override
    public void finishCompetition(T winner) {
        isFinished = true;
        this.winner = winner;
    }

    @Override
    public void addPoint(T competitor) {
        var currentSet = sets.getLast();
        currentSet.addPoint(competitor);
        if (!currentSet.isFinished()) {
            return;
        }
        incrementSets(competitor);
        if (canFinishMatch()) {
            finishCompetition(chooseWinner());
        } else {
            startNewSet();
        }
    }

    private void incrementSets(T competitor) {
        score = score.increment(isFirst(competitor));
    }

    private void startNewSet() {
        this.sets.add(Set.<T>builder()
                .gameRule(gameRule)
                .setRule(setRule)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private T chooseWinner() {
        return score.first() > score.second() ? firstCompetitor : secondCompetitor;
    }

    private boolean canFinishMatch() {
        return score.first() >= rules.setsToWinMatch() || score.second() >= rules.setsToWinMatch();
    }
}
