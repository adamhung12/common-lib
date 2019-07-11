package me.xethh.libs.spring.web.security.toolkits.exceptionModels.generalThrowables;

import me.xethh.libs.spring.web.security.toolkits.exceptionModels.CustomExceptionHandler;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.GeneralExceptionModel;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.StatusBasesGeneralSSTExceptionModelFactory;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.libs.toolkits.logging.WithLoggerImpl;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class GeneralThrowableHandler extends WithLoggerImpl implements CustomExceptionHandler, WithLogger {
    @Override
    public Optional<GeneralExceptionModel> dispatch(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        if(((GeneralThrowable)ex).getException() instanceof StatusBasesGeneralSSTExceptionModelFactory.AuthorizationFail){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        if(((GeneralThrowable)ex).getException() instanceof StatusBasesGeneralSSTExceptionModelFactory.TokenNotValid){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return Optional.of(((GeneralThrowable)ex).getException());
    }

    @Override
    public boolean isSupported(Throwable ex) {
        if(ex instanceof GeneralThrowable)
            return true;
        return false;
    }
}
