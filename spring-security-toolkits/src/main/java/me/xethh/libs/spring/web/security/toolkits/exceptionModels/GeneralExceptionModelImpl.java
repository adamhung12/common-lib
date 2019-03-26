package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import org.springframework.http.HttpStatus;

import java.util.Date;

public abstract class GeneralExceptionModelImpl implements GeneralExceptionModel {
    protected GeneralExceptionModelImpl(){
        this(null,null,null);
    }
    protected GeneralExceptionModelImpl(String id){
        this(id,null,null);
    }
    protected GeneralExceptionModelImpl(String id, String error){
        this(id,error,null);
    }
    protected GeneralExceptionModelImpl(String id, String error, HttpStatus status){
       this.id = id;
       this.error = error;
       this.status = status;
    }
    private Date timestamp = new Date();
    private HttpStatus status;
    private String error;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

}
