package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import org.springframework.http.HttpStatus;

public class GeneralSSTExceptionModelImpl extends GeneralSSTExceptionModel{
    protected GeneralSSTExceptionModelImpl(HttpStatus status, String error) {
        super(status, error);
    }

}
