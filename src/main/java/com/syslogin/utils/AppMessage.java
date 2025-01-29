package com.syslogin.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppMessage {
    private final MessageSource messageSource;

    public String getMessage(String code){
        return messageSource.getMessage(code, null, null).replaceAll("\"", "");
    }
}
