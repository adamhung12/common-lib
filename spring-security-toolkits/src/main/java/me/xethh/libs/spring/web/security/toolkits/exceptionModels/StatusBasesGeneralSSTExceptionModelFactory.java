package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusBasesGeneralSSTExceptionModelFactory {
    public GeneralSSTExceptionModel dispatch(HttpStatus status){
        switch (status){
            case NOT_FOUND:
                return new NotFoundError(null);
        }
        return new OtherError(status, null);
    }

    public static class NotFoundError extends GeneralSSTExceptionModel {
        private String message = "Resource not found";

        public NotFoundError(String error){
            super(HttpStatus.NOT_FOUND,error);
        }

        public String getMessage() {
            return message;
        }
        @Override
        public HttpStatus getStatus() {
            return HttpStatus.NOT_FOUND;
        }
    }
    public static class OtherError extends GeneralSSTExceptionModel {
        private String message = "Other error";
        private HttpStatus status;
        public OtherError(HttpStatus status, String error){
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

    public static class MethodArgumentNotValid extends GeneralSSTExceptionModel {

        @JsonIgnore
        private BindingResult result;
        public MethodArgumentNotValid(HttpStatus status, BindingResult result ){
            super(HttpStatus.BAD_REQUEST,"Parameter not correct");
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
    }

    public static class UnAuthorize extends GeneralSSTExceptionModel {
        private String message;
        public UnAuthorize(String message){
            super(HttpStatus.FORBIDDEN,null);
            this.message = message;
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

    public static class AuthorizationFail extends GeneralSSTExceptionModel {
        private String message = "Authorization failed";
        public AuthorizationFail(){
            super(HttpStatus.FORBIDDEN,null);
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

    public static class UnknownError extends GeneralSSTExceptionModel {
        private String message = "Unknown error";
        private HttpStatus status;

        protected UnknownError(HttpStatus status, String error) {
            super(status, error);
        }

        public UnknownError setMessage(String message) {
            this.message = message;
            return this;
        }

        public String getMessage() {
            return message;
        }
        @Override
        public HttpStatus getStatus() {
            return status;
        }
    }
    public static class TokenNotValid extends GeneralSSTExceptionModel {
        private String message = "Token not valid";
        public TokenNotValid(){
            super(HttpStatus.UNAUTHORIZED,null);
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
}
