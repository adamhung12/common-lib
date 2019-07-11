package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.libs.toolkits.logging.WithLoggerImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
public class ErrorHandlerController extends WithLoggerImpl implements ErrorController, WithLogger {
    @PostConstruct
    public void init(){
        logger.info("Initialized ErrorHandlerController.");
    }

    @Autowired
    List<CustomExceptionHandler> customExceptionHandlerList;

    @Autowired
    StatusBasesGeneralSSTExceptionModelFactory statusBasesGeneralSSTExceptionModelFactory;

    Logger logger = logger();

    ObjectMapper mapper = new ObjectMapper();
    @RequestMapping("/error")
    @ResponseBody
    public Object handleError(HttpServletRequest request, HttpServletResponse response) {
        Object exception = request.getAttribute("javax.servlet.error.exception");
        if(exception!=null && exception instanceof Throwable){
            for(CustomExceptionHandler exceptionHandler : customExceptionHandlerList){
                if(exceptionHandler.isSupported((Throwable) exception)){
                    Optional<GeneralExceptionModel> object = exceptionHandler.dispatch((Throwable) exception, request, response);
                    if(object.isPresent()){
                        request.removeAttribute("javax.servlet.error.exception");
                        return object.get();
                        // throw new GeneralThrowable((me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModel) object.get());
                        // try {
                            // response.getOutputStream().write(mapper.writeValueAsString(object.get()).getBytes());
                            // response.flushBuffer();
                            // return null;
                        // } catch (IOException e) {
                        //     e.printStackTrace();
                        // }

                    }
                }
            }

            logger.error(((Throwable) exception).getMessage(),exception);
            return new StatusBasesGeneralSSTExceptionModelFactory.UnknownError(null, "Unkown error E-9999999999993");
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            HttpStatus statusObject = HttpStatus.valueOf(statusCode);
            if(statusObject  != null){
                GeneralSSTExceptionModel returnObject = statusBasesGeneralSSTExceptionModelFactory.dispatch(statusObject);
                if(returnObject!=null)
                    return returnObject;
            }
            return new StatusBasesGeneralSSTExceptionModelFactory.UnknownError(null, "Unkown error E-9999999999991");
        }
        return new StatusBasesGeneralSSTExceptionModelFactory.UnknownError(null, "Unkown error E-9999999999992");
    }

    public List<CustomExceptionHandler> getCustomExceptionHandlerList() {
        return customExceptionHandlerList;
    }

    public void setCustomExceptionHandlerList(List<CustomExceptionHandler> customExceptionHandlerList) {
        this.customExceptionHandlerList = customExceptionHandlerList;
    }

    public StatusBasesGeneralSSTExceptionModelFactory getStatusBasesGeneralSSTExceptionModelFactory() {
        return statusBasesGeneralSSTExceptionModelFactory;
    }

    public void setStatusBasesGeneralSSTExceptionModelFactory(StatusBasesGeneralSSTExceptionModelFactory statusBasesGeneralSSTExceptionModelFactory) {
        this.statusBasesGeneralSSTExceptionModelFactory = statusBasesGeneralSSTExceptionModelFactory;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

}
