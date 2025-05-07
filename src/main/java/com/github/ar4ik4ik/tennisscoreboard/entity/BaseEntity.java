package com.github.ar4ik4ik.tennisscoreboard.entity;


import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public interface BaseEntity <K extends Serializable>{
    Integer getId();
    void setId(K id);
}
