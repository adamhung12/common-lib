package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import org.springframework.http.HttpStatus;

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
}
