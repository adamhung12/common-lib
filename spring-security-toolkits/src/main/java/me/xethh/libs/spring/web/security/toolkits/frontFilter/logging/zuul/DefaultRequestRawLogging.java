package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.TracingSystemConst.TRANSACTION_HEADER;

public class DefaultRequestRawLogging implements RequestRawLogging {
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private boolean passwordProtection = true;
    private SimpleDateFormat format = DateFormatBuilder.Format.ISO8601.getFormatter();
    PerformanceLog performanceLog = PerformanceLog.staticLog;
    @Override
    public void log(ServletRequest servletRequest) {
        String label = "ZUU_REQ_RAW_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb
                .append("||").append(label).append("|")
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                .append(format.format(new Date())).append("|")
                .append(System.nanoTime()).append(NewLine)
        ;

        if(servletRequest!=null && servletRequest instanceof HttpServletRequest){
            printUrlLogger(sb, (HttpServletRequest) servletRequest);
            sb.append(NewLine).append("Request Header: ").append(NewLine);
            getHeaderInfo(sb, (HttpServletRequest) servletRequest);
            sb.append(NewLine).append("Request Params: ").append(NewLine);
            getParameters(sb, servletRequest);
            if (servletRequest instanceof CachingRequestWrapper) {
                sb.append(NewLine).append("Request Body: ").append(NewLine);
                sb.append(((CachingRequestWrapper) servletRequest).getCachedStringContent());
            }
        }

        logger.info(sb.toString());
        performanceLog.logEnd(label, logger);
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
                sb.append(headerValue);
                if (headers.hasMoreElements()) {
                    sb.append(",");
                }
            }
            sb.append("\"").append(NewLine);
        }
    }
}
