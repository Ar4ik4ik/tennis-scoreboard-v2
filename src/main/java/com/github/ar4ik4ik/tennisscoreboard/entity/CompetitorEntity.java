package com.github.ar4ik4ik.tennisscoreboard.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public interface CompetitorEntity <K extends Serializable> {

    K getId();
    void setId(K id);
    String getName();

}
