package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModel;

public class GeneralThrowable extends RuntimeException{
    private GeneralExceptionModel exception;
    public GeneralThrowable(GeneralExceptionModel exception){
        this.exception = exception;
    }

    public GeneralExceptionModel getException() {
        return exception;
    }

    public void setException(GeneralExceptionModel exception) {
        this.exception = exception;
    }
}
