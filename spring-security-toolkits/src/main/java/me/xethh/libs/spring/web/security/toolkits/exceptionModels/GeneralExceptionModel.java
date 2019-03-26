package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import org.springframework.http.HttpStatus;

import java.util.Date;

public interface GeneralExceptionModel {
    String getId();
    HttpStatus getStatus();
    Date getTimestamp();
    String getError();
}
