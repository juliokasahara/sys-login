package com.syslogin.presenter;

import com.syslogin.presenter.dto.EmptyJsonBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static <T> ResponseEntity<T> generateResponse(String message, HttpStatus status, T responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj != null ? responseObj : new EmptyJsonBody());

        return new ResponseEntity<T>((T) map,status);
    }
}