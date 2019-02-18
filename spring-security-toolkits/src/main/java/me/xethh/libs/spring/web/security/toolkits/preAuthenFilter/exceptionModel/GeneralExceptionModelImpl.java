package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class GeneralExceptionModelImpl implements GeneralExceptionModel {
    private Date timestamp = new Date();
    private HttpStatus status;
    private String error;

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class MethodArgumentNotValid extends GeneralExceptionModelImpl {

        @JsonIgnore
        private BindingResult result;
        public MethodArgumentNotValid(BindingResult result ){
            super();
            this.result=result;
        }

        @JsonIgnore
        public String getMessage() {
            List<FieldError> errors = result.getFieldErrors();
            String message = "";
            for(FieldError error : errors){
                message+=String.format("Field error[%s] ==> %s;", error.getField(),error.getDefaultMessage());
            }
            return message;
        }

        public List<String> getMessages(){
            List<FieldError> errors = result.getFieldErrors();
            List<String> message = new ArrayList<>();
            for(FieldError error : errors){
                message.add(String.format("Field error[%s] ==> %s", error.getField(),error.getDefaultMessage()));
            }
            Collections.sort(message);
            return message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.BAD_REQUEST;
        }

    }

    public static class InternalError extends GeneralExceptionModelImpl {
        private String message = "Internal Error";

        public InternalError(){
            super();
        }

        public String getMessage() {
            return message;
        }
        @Override
        public HttpStatus getStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
    public static class NotFoundError extends GeneralExceptionModelImpl {
        private String message = "Resource not found";

        public NotFoundError(){
            super();
        }

        public String getMessage() {
            return message;
        }
        @Override
        public HttpStatus getStatus() {
            return HttpStatus.NOT_FOUND;
        }

    }
    public static class OtherError extends GeneralExceptionModelImpl {
        private String message = "Other error";
        private HttpStatus status;
        public OtherError(HttpStatus status){
            super();
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
    public static class TokenNotValid extends GeneralExceptionModelImpl {
        private String message = "Token not valid";
        public TokenNotValid(){
            super();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.UNAUTHORIZED;
        }

    }
    public static class AuthorizationFail extends GeneralExceptionModelImpl {
        private String message = "Authorization failed";
        public AuthorizationFail(){
            super();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.UNAUTHORIZED;
        }

    }
    public static class TemplateNotValid extends GeneralExceptionModelImpl {
        private String message = "Template[%s] not valid";
        private String templateName;
        public TemplateNotValid(String templateName){
            super();
            this.templateName = templateName;
        }

        public String getMessage() {
            return String.format(message, templateName);
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
    public static class OperationParamNotValid extends GeneralExceptionModelImpl {
        private String message;
        public OperationParamNotValid(String message){
            super();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.BAD_REQUEST;
        }

    }
    public static class CustomerNotValid extends GeneralExceptionModelImpl {
        private String message = "Customer[%s] not valid";
        private String customer;
        public CustomerNotValid(String customer){
            super();
            this.customer = customer;
        }

        public String getMessage() {
            return String.format(message, customer);
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public HttpStatus getStatus() {
            return HttpStatus.BAD_REQUEST;
        }

    }

}
