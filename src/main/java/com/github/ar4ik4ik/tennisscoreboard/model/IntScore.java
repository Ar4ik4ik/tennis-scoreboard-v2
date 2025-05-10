package com.github.ar4ik4ik.tennisscoreboard.model;

public record IntScore(Integer first, Integer second) implements Score<Integer> {
    @Override
    public IntScore incrementFirst() {
        return new IntScore(first + 1, second);
    }

    @Override
    public IntScore incrementSecond() {
        return new IntScore(first, second + 1);
    }

    @Override
    public IntScore increment(boolean isFirst) {
        return isFirst ? incrementFirst() : incrementSecond();
    }
}
