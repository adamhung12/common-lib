package me.xethh.libs.spring.web.security.toolkits.frontFilter.impl;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType;
import org.slf4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class DefaultAccessLogging implements AccessLogging {
    @Override
    public void log(Logger logger, ServletRequest servletRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>AL_V1>>");
        if(servletRequest==null){
            sb.append(RawLoggingType.Empty.name());
            sb.append("||");
            sb.append(System.nanoTime()).append("|");
            logger.info(RawLoggingType.Empty.name());
        }
        else if(servletRequest instanceof HttpServletRequest){
            sb.append(RawLoggingType.Http.name()).append("||");
            sb.append(((HttpServletRequest) servletRequest).getRequestedSessionId()).append("|");
            sb.append(System.nanoTime()).append("|");
            sb.append(((HttpServletRequest) servletRequest).getMethod()).append("|");
            sb.append(servletRequest.isSecure()).append("|");
            sb.append(((HttpServletRequest) servletRequest).getRequestURI()).append("|");
            sb.append(((HttpServletRequest) servletRequest).getRequestURI()).append("|");
            sb.append(servletRequest.getParameter("CUST-TRANSACTION-ID")).append("|");
        }
        else{
            sb.append(RawLoggingType.OtherServelet.name()).append("||");
            sb.append(System.nanoTime()).append("|");
            sb.append(servletRequest.isSecure()).append("|");
            sb.append(servletRequest.getClass().getName()).append("|");
        }
        sb.append(">>>||");
        logger.info(sb.toString());
    }
}
