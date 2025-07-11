package com.github.ar4ik4ik.tennisscoreboard.model.scoring;

public interface Score <S> {

    Score<S> incrementFirst();
    Score<S> incrementSecond();
    Score<S> increment(boolean isFirst);
    S first();
    S second();
}
