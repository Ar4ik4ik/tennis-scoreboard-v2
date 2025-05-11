package com.github.ar4ik4ik.tennisscoreboard.model.dto;

import lombok.Builder;

@Builder
public record PlayerResponseDto(Integer id, String name) {
}
