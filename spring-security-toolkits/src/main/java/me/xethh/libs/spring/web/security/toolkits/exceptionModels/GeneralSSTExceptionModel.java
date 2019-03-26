package me.xethh.libs.spring.web.security.toolkits.exceptionModels;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.TracingSystemConst;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

public class GeneralSSTExceptionModel extends GeneralExceptionModelImpl{
    private String systemId;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    protected GeneralSSTExceptionModel(HttpStatus status, String error){
        super(MDC.get(TracingSystemConst.TRANSACTION_HEADER),error,status);
        this.systemId = MDC.get(TracingSystemConst.APP_NAME);
    }
}
