package com.security.csrf.exception;

import org.springframework.util.StringUtils;

public class DataFoundNullException extends Exception {

    public DataFoundNullException(String clazz) {
        super(DataFoundNullException.generateMessage(clazz));
    }

    private static String generateMessage(String entity) {
        return StringUtils.capitalize(entity) + " was not found Null/Empty, it's Should not be Empty! ";
    }

}
