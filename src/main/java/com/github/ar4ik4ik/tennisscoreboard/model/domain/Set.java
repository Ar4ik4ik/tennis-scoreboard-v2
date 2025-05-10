package com.github.ar4ik4ik.tennisscoreboard.model.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competition;
import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.GameRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.SetRule;
import com.github.ar4ik4ik.tennisscoreboard.model.rules.abstractrules.TieBreakRule;
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

    private Integer firstCompetitorScore;
    private Integer secondCompetitorScore;


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
        this.firstCompetitorScore = 0;
        this.secondCompetitorScore = 0;
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
        if (competitor.equals(firstCompetitor)) {
            firstCompetitorScore++;
        } else {
            secondCompetitorScore++;
        }
    }

    private boolean isTieBreakMode() {
        return firstCompetitorScore >= rules.gamesToWinSet() && secondCompetitorScore >= rules.gamesToWinSet()
                && rules.useTieBreak();
    }

    private boolean canWinSet(T competitor) {
        int scorerScore = competitor.equals(firstCompetitor) ? firstCompetitorScore : secondCompetitorScore;
        int opponentScore = competitor.equals(firstCompetitor) ? secondCompetitorScore : firstCompetitorScore;

        return scorerScore >= rules.gamesToWinSet() && scorerScore - opponentScore >= rules.winByGames();

    }

    @Override
    public void finishCompetition(T winner) {
        this.winner = winner;
        isFinished = true;
    }
}