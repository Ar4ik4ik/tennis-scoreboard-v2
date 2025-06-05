package com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.TieBreakRule;

/**
 * @param winBy сколько подряд очков (разница) нужно, чтобы выиграть (обычно 2)
 * @param pointsToWin минимальное количество очков в тай-брейке (обычно 7)
 */
public record ClassicTieBreakRules(int winBy, int pointsToWin) implements TieBreakRule {
}
