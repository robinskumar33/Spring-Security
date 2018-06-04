package com.security.csrf.utils;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ResponseJsonUtil {

    private static final String STATUS = "status";

    public static Map<String, String> getSuccessResponseJson() {
        Map<String, String> responseBuilder = new HashMap<>();
        responseBuilder.put(STATUS, "success");
        return responseBuilder;
    }

    public static Map<String, Object> getSuccessResponseJson(Object data) {
        Map<String, Object> responseBuilder = new HashMap<>();
        responseBuilder.put(STATUS, "success");
        responseBuilder.put("data", data);
        return responseBuilder;
    }

    public static Map<String, Object> getFailedResponseJson(Object apiError) {
        Map<String, Object> responseBuilder = new HashMap<>();
        responseBuilder.put(STATUS, "failure");
        responseBuilder.put("error", apiError);
        return responseBuilder;
    }

    public static Map getSuccessResponseJsonForId(Long id) {
        Map data = new HashMap();
        data.put("id", id);
        return getSuccessResponseJson(data);
    }

    public static Map getFailedResponseJson(String errorCode, String error) {
        Map responseBuilder = new HashMap();
        responseBuilder.put("status", "failure");
        Map errors = new HashMap();
        errors.put(errorCode, error);
        responseBuilder.put("errors", errors);
        return responseBuilder;
    }
}
