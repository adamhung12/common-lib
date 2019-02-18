package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel;

import org.springframework.http.HttpStatus;

import java.util.Date;

public interface GeneralExceptionModel {
    HttpStatus getStatus();
    Date getTimestamp();
    String getError();
}
