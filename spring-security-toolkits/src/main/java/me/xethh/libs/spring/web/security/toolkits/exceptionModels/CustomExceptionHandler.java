package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import java.util.Optional;

public interface CustomExceptionHandler {
    Optional<GeneralExceptionModel> dispatch(Throwable ex);
    boolean isSupported(Throwable ex);
}
