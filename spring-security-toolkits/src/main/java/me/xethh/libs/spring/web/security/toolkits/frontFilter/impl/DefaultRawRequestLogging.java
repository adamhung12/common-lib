package me.xethh.libs.spring.web.security.toolkits.frontFilter.impl;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawRequestLogging;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

public class DefaultRawRequestLogging implements RawRequestLogging {
    private boolean passwordProtection = true;
    private SimpleDateFormat format = DateFormatBuilder.ISO8601();
    @Override
    public void log(Logger logger, ServletRequest servletRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>RR_V1>>").append(NewLine);
        sb
            .append("Receive Date: ").append(format.format(new Date())).append("\r\n")
            .append("Nano time").append(System.nanoTime()).append("\r\n");
        if(servletRequest!=null && servletRequest instanceof HttpServletRequest){
            printUrlLogger(sb, (HttpServletRequest) servletRequest);
            sb.append("Session Id: " + ((HttpServletRequest) servletRequest).getRequestedSessionId()).append(NewLine);
            if(servletRequest instanceof HttpServletRequest){
                sb.append(NewLine).append("Request Header:").append(NewLine);
                getHeaderInfo(sb, (HttpServletRequest) servletRequest);
                sb.append(NewLine).append("Request Params:").append(NewLine);
                getParameters(sb, servletRequest);
            }
            if (servletRequest instanceof CachingRequestWrapper) {
                sb.append(NewLine).append("Request Body:").append(NewLine);
                sb.append(((CachingRequestWrapper) servletRequest).getCachedStringContent());
            }
        }
        sb.append(">>>||").append(NewLine);
        logger.info(sb.toString());
    }


    private static String NewLine = "\r\n";
    private void printUrlLogger(StringBuilder sb, HttpServletRequest request) {
        sb.append("Method: "+ request.getMethod()).append(NewLine);
        sb.append("Request URI: "+ request.getRequestURI()).append(NewLine);
        sb.append("Request context path: "+ request.getContextPath()).append(NewLine);
        sb.append("Request servlet path: "+ request.getServletPath()).append(NewLine);
        sb.append("Request path info: "+ request.getPathInfo()).append(NewLine);
    }

    public static void getParameters(StringBuilder sb, ServletRequest req){
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            sb.append("\"").append(paramName).append("\" => \"");
            String param = req.getParameter(paramName);
            if(paramName!=null & paramName.equalsIgnoreCase("password")){
                for(char x:param.toCharArray())
                    sb.append("*");
            }
            else{
              sb.append(param);
            }
            sb.append("\"").append(NewLine);
        }
    }
    public static void getHeaderInfo(StringBuilder sb, HttpServletRequest req) {
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("\"").append(headerName).append("\" => \"");
            Enumeration<String> headers = req.getHeaders(headerName);
            while (headers.hasMoreElements()) {
                String headerValue = headers.nextElement();
                // if(headerName.equalsIgnoreCase("password")){
                //     for(char x : headerValue.toCharArray())
                //         sb.append("*");
                // }
                // else
                //     sb.append(headerValue);
                sb.append(headerValue);
                if (headers.hasMoreElements()) {
                    sb.append(",");
                }
            }
            sb.append("\"").append(NewLine);
        }
    }
}
