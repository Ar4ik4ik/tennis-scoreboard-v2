package com.github.ar4ik4ik.tennisscoreboard.model;

import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.MatchRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.TieBreakRule;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Match<T extends Competitor> implements Competition<T, Integer, MatchRule> {

    private final MatchRule rules;
    private final SetRule setRule;
    private final GameRule gameRule;

    private final T firstCompetitor, secondCompetitor;

    private final Integer firstCompetitorScore = 0;
    private final Integer secondCompetitorScore = 0;

    private boolean isFinished = false;
    private T winner = null;

    private final List<Set<T>> sets = new ArrayList<>();

    @Builder
    private Match(MatchRule matchRule, T firstCompetitor, T secondCompetitor,
                  SetRule setRule, GameRule gameRule, TieBreakRule tieBreakRule) {
        this.rules = matchRule;
        this.setRule = setRule;
        this.gameRule = gameRule;
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
        this.winner = winner;
        this.isFinished = true;
    }

    @Override
    public void addPoint(T competitor) {

    }
}
