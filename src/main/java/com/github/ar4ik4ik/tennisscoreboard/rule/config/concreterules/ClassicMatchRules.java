package com.github.ar4ik4ik.tennisscoreboard.rule.config.concreterules;

import com.github.ar4ik4ik.tennisscoreboard.rule.config.abstractrules.MatchRule;

/**
 * Класс правил матча «best-of-(2×setsToWinMatch−1)».
 * Например, setsToWinMatch = 2 → best-of-3.
 */
public record ClassicMatchRules(int setsToWinMatch) implements MatchRule {
}
