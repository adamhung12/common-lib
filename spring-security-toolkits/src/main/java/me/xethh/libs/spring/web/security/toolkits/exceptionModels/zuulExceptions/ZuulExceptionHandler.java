package me.xethh.libs.spring.web.security.toolkits.exceptionModels.zuulExceptions;

import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.CustomExceptionHandler;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.GeneralExceptionModel;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.GeneralSSTExceptionModel;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class ZuulExceptionHandler implements CustomExceptionHandler, WithLogger {
    @Override
    public Optional<GeneralExceptionModel> dispatch(Throwable ex, HttpServletRequest request) {
        logger().error("Zuul error: "+ex.getMessage(), ex);
        if(ex.getMessage().equals("Forwarding Error")){
            return Optional.of((GeneralExceptionModel) new ZuulError("Internal Forwarding error", extractStatus(request)));
        }
        return Optional.of((GeneralExceptionModel) new ZuulError(null, extractStatus(request)));
    }

    @Override
    public boolean isSupported(Throwable ex) {
        if(ex instanceof ZuulException)
            return true;
        return false;
    }

    public static class ZuulError extends GeneralSSTExceptionModel {
        private String message = "Internal routing error";
        private HttpStatus status;
        public ZuulError(String error, HttpStatus status){
            super(status,error);
            this.status = status;
        }

        public String getMessage() {
            return message;
        }
        @Override
        public HttpStatus getStatus() {
            return status;
        }

    }
}
