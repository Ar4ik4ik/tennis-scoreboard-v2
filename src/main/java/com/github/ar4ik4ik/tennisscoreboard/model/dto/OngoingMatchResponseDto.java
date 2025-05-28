package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import com.github.ar4ik4ik.tennisscoreboard.model.State;
import lombok.Builder;


@Builder
public record OngoingMatchResponseDto(String matchId,
                                      PlayerResponseDto player1,
                                      PlayerResponseDto player2,
                                      int player1Sets,
                                      int player2Sets,
                                      int player1GamesInCurrentSet,
                                      int player2GamesInCurrentSet,
                                      String player1Points,
                                      String player2Points,
                                      State matchState,
                                      boolean isTieBreak) {
}
