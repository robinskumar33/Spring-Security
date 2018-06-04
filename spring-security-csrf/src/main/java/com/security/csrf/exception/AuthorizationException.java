package com.security.csrf.exception;

import org.springframework.util.StringUtils;

public class AuthorizationException extends Exception {

    public AuthorizationException(String clazz) {
        super(AuthorizationException.generateMessage(clazz));
    }

    private static String generateMessage(String entity) {
        return "Access denied by URL authorization policy on the Web server for " + StringUtils.capitalize(entity) + "! ";
    }

}
