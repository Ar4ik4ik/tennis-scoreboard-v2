package com.github.ar4ik4ik.tennisscoreboard.util;

public class ParameterValidator {

    public static boolean isEmpty(String parameter) {
        return parameter == null || parameter.isBlank();
    }
}
