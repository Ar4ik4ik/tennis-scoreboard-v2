package com.github.ar4ik4ik.tennisscoreboard.model.scoring;

import lombok.Getter;

@Getter
public enum GamePoint {
    ZERO(0), FIFTEEN(1), THIRTY(2), FORTY(3);

    private final int value;
    GamePoint(int value) {
        this.value = value;
    }

    public GamePoint getNextPoint() {
        return switch (this) {
            case ZERO -> FIFTEEN;
            case FIFTEEN -> THIRTY;
            case THIRTY -> FORTY;
            case FORTY -> throw new IllegalStateException("Cannot advance beyond FORTY without winning.");
        };
    }

    public int getDisplayValue() {
        return switch (this) {
            case ZERO -> 0;
            case FIFTEEN -> 15;
            case THIRTY -> 30;
            case FORTY -> 40;
        };
    }
}
