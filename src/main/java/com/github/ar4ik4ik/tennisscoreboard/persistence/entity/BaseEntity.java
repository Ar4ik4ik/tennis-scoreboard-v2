package com.github.ar4ik4ik.tennisscoreboard.persistence.entity;


import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public interface BaseEntity <K extends Serializable>{
    K getId();
    void setId(K id);
}
