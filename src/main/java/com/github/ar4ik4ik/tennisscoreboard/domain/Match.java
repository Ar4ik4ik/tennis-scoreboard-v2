package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.State;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.GamePoint;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.scoring.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.MatchRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.GameScoreStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.ScoringStrategy;
import com.github.ar4ik4ik.tennisscoreboard.rule.strategy.SetScoringStrategy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

import static com.github.ar4ik4ik.tennisscoreboard.model.State.*;

@Getter
public class Match<T extends Competitor> implements Competition<T, Integer, MatchRule> {

    private final MatchRule rules;
    private final SetRule setRule;
    private final GameRule gameRule;
    private final TieBreakRule tieBreakRule;

    private State state = PLAYING;

    private final ScoringStrategy<Integer> strategy;
    private final SetScoringStrategy<Integer> setStrategy;
    private final GameScoreStrategy<GamePoint> gameStrategy;
    private final ScoringStrategy<Integer> tieBreakStrategy;

    private final T firstCompetitor, secondCompetitor;

    @Getter(AccessLevel.PUBLIC)
    private Score<Integer> score = new IntScore(0, 0);

    private T winner = null;

    private final List<Set<T>> sets = new LinkedList<>();

    @Builder
    private Match(MatchRule matchRule, T firstCompetitor, T secondCompetitor,
                  SetRule setRule, GameRule gameRule, TieBreakRule tieBreakRule, ScoringStrategy<Integer> strategy, SetScoringStrategy<Integer> setStrategy, GameScoreStrategy<GamePoint> gameStrategy, ScoringStrategy<Integer> tieBreakStrategy) {
        this.rules = matchRule;
        this.setRule = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.strategy = strategy;
        this.setStrategy = setStrategy;
        this.gameStrategy = gameStrategy;
        this.tieBreakStrategy = tieBreakStrategy;
        startNewSet();
    }

    @Override
    public void finishCompetition(T winner) {
        state = FINISHED;
        this.winner = winner;
    }

    @Override
    public void addPoint(T competitor) {
        validateAddPoint(competitor);
        processCurrentSetPoint(competitor);
    }


    public Set<T> getCurrentSet() {
        return sets.getLast();
    }

    private void processCurrentSetPoint(T competitor) {
        var currentSet = sets.getLast();
        currentSet.addPoint(competitor);
        if (currentSet.getState() == PLAYING || currentSet.getState() == TIEBREAK) {
            return;
        }
        handleSetCompletion(competitor);
    }

    private void handleSetCompletion(T competitor) {
        var scoringResult = strategy.onPoint(score, isFirstCompetitor(competitor));
        score = scoringResult.score();
        if (scoringResult.isFinished()) {
            finishCompetition(competitor);
        } else {
            startNewSet();
        }
    }

    private void startNewSet() {
        Set<T> last = sets.getLast();
        if (last.getState() != State.FINISHED) {
            throw new IllegalStateException("Previous set not finished");
        }
        this.sets.add(Set.<T>builder()
                .gameRule(gameRule)
                .setRule(setRule)
                .strategy(setStrategy)
                .gameStrategy(gameStrategy)
                .tieBreakStrategy(tieBreakStrategy)
                .tieBreakRule(tieBreakRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    private void validateAddPoint(T competitor) {
        if (state == FINISHED) {
            throw new IllegalStateException("Set is already finished");
        }
        if (!competitor.equals(firstCompetitor) && !competitor.equals(secondCompetitor)) {
            throw new IllegalArgumentException("Received competitor not from this game");
        }
    }


    private boolean isFirstCompetitor(T competitor) {
        return competitor.equals(firstCompetitor);
    }
}
