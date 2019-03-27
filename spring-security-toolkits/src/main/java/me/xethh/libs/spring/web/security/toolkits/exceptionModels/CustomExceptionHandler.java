package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import me.xethh.libs.toolkits.logging.WithLogger;
import org.springframework.http.HttpStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface CustomExceptionHandler extends WithLogger {
    default HttpStatus extractStatus(HttpServletRequest request){
        Object oStatus = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(oStatus!=null && oStatus instanceof Integer){
            return HttpStatus.valueOf((Integer) oStatus);
        }
        logger().info(String.format("Status is not integer or null[%s]", oStatus));
        return null;
    }
    Optional<GeneralExceptionModel> dispatch(Throwable ex, HttpServletRequest response);
    boolean isSupported(Throwable ex);
}
