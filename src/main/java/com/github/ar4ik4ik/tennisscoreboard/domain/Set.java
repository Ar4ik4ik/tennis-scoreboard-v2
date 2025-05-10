package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.IntScore;
import com.github.ar4ik4ik.tennisscoreboard.model.Score;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Set<T extends Competitor> implements Competition<T, IntScore, SetRule> {

    private final SetRule rules;
    private final GameRule gameRule;
    private final TieBreakRule tieBreakRule;

    private final T firstCompetitor, secondCompetitor;

    private Score<Integer> score = new IntScore(0, 0);


    private boolean isTieBreakGameStarted = false;

    private boolean isFinished = false;
    private T winner;

    private final List<Competition<T, ?, ?>> games;

    @Builder
    private Set(SetRule setRule, T firstCompetitor, T secondCompetitor, GameRule gameRule, TieBreakRule tieBreakRule) {
        this.rules = setRule;
        this.gameRule = gameRule;
        this.tieBreakRule = tieBreakRule;
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.winner = null;
        this.games = new ArrayList<>();
        this.games.add(Game.<T>builder()
                .gameRule(gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    public void addPoint(T competitor) {
        var currentGame = games.getLast();
        currentGame.addPoint(competitor);

        if (!currentGame.isFinished()) {
            return;
        }

        incrementGames(competitor);

        if (isTieBreakMode()) {
            handleTieBreakCompletion(competitor);
        } else {
            if (canWinSet(competitor)) {
                finishCompetition(competitor);
            } else {
                startNextGame();
            }
        }

        if (canWinSet(competitor)) {
            this.winner = competitor;
            this.isFinished = true;
        }
    }

    private void startNextGame() {
        games.add(Game.<T>builder()
                .gameRule(this.gameRule)
                .firstCompetitor(firstCompetitor)
                .secondCompetitor(secondCompetitor)
                .build());
    }

    private void handleTieBreakCompletion(T competitor) {
        if (!isTieBreakGameStarted) {
            games.add(TieBreakGame.<T>builder()
                    .tieBreakRule(this.tieBreakRule)
                    .firstCompetitor(this.firstCompetitor)
                    .secondCompetitor(this.secondCompetitor)
                    .build());
            isTieBreakGameStarted = true;
        } else {
            isTieBreakGameStarted = false;
            finishCompetition(competitor);
        }
    }

    private void incrementGames(T competitor) {
        score = score.increment(isFirst(competitor));
    }

    private boolean isFirst(T competitor) {
        return competitor.equals(firstCompetitor);
    }

    private boolean isTieBreakMode() {
        return score.first() >= rules.gamesToWinSet() && score.second() >= rules.gamesToWinSet()
                && rules.useTieBreak();
    }

    private boolean canWinSet(T competitor) {
        int scorerScore = isFirst(competitor) ? score.first() : score.second();
        int opponentScore = isFirst(competitor) ? score.second() : score.first();

        return scorerScore >= rules.gamesToWinSet() && scorerScore - opponentScore >= rules.winByGames();

    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }
}