package com.github.ar4ik4ik.tennisscoreboard.model;

public record GameScore(GamePoint first, GamePoint second) implements Score<GamePoint> {

    @Override
    public GameScore incrementFirst() {
        return new GameScore(first.getNextPoint(), second);
    }

    @Override
    public GameScore incrementSecond() {
        return new GameScore(first, second.getNextPoint());
    }

    @Override
    public GameScore increment(boolean isFirst) {
        return isFirst ? incrementFirst() : incrementSecond();
    }
}
