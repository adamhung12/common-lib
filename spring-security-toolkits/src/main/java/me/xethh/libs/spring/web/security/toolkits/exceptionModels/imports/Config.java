package me.xethh.libs.spring.web.security.toolkits.exceptionModels.imports;

import me.xethh.libs.spring.web.security.toolkits.exceptionModels.ControllerAdvice;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.CustomExceptionHandler;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.ErrorHandlerController;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.GeneralExceptionModel;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.generalThrowables.GeneralThrowableHandler;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.zuulExceptions.ZuulExceptionHandler;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class Config {

    @Bean
    public ControllerAdvice controllerAdvice(){
        return new ControllerAdvice();
    }

    @Bean
    public ZuulExceptionHandler zuulExceptionHandler(){
        return new ZuulExceptionHandler();
    }

    @Bean
    public CustomExceptionHandler defaultCustomExceptionHandler(){
        return new CustomExceptionHandler(){
            @Override
            public Optional<GeneralExceptionModel> dispatch(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
                return Optional.empty();
            }

            @Override
            public boolean isSupported(Throwable ex) {
                return false;
            }
        };
    }
    @Bean
    public ErrorController customExceptionHandler(){
        return new ErrorHandlerController();
    }

    @Bean
    public GeneralThrowableHandler generalThrowableHandler(){
        return new GeneralThrowableHandler();
    }
}
