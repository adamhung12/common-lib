package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.ExceptionSetter;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModel;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.exceptions.GeneralThrowable;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@org.springframework.web.bind.annotation.ControllerAdvice("me.xethh.libs.spring.web.security.toolkits")
public class ControllerAdvice extends ResponseEntityExceptionHandler implements ExceptionSetter,  WithLogger {
    @PostConstruct
    public void Init() {
        logger().info("started controller advice");
    }

    @ExceptionHandler(Throwable.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object handleResourceNotFoundException(Exception ex) {
        if(ex instanceof me.xethh.libs.spring.web.security.toolkits.exceptionModels.generalThrowables.GeneralThrowable){
            logger.error("System throwing general throwable");
            logger().error(ex.getMessage(),ex);
            return ((GeneralThrowable)ex).getException();
        }
        logger.error("System throwing unknown error");
        logger().error(ex.getMessage(),ex);
        return new StatusBasesGeneralSSTExceptionModelFactory.UnknownError(HttpStatus.INTERNAL_SERVER_ERROR,"Unkown error E-9999999999994");
    }

    ObjectMapper maper = new ObjectMapper();

    @Override
    public void setException(HttpServletResponse httpServletResponse, GeneralExceptionModel operation) {
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setStatus(operation.getStatus().value());
        String response = "{\"Error\":\"\"}";
        try {
            response = maper.writeValueAsString(operation);
            PrintWriter out = httpServletResponse.getWriter();
            out.print(response);
            out.flush();
            return;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // httpServletResponse.setContentLength(response.length());
    }

    @Override
    public Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    public interface CpaasExceptionModel {
        HttpStatus statusObject();
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        BindingResult result = ex.getBindingResult();
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.MethodArgumentNotValid(status, result),status);

    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex);
        return new ResponseEntity<Object>(new StatusBasesGeneralSSTExceptionModelFactory.OtherError(status,null),status);
    }

}
