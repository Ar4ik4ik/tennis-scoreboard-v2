package com.github.ar4ik4ik.tennisscoreboard.domain;

import com.github.ar4ik4ik.tennisscoreboard.model.Competitor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Player implements Competitor {

    private final Integer id;
    private final String name;

}
