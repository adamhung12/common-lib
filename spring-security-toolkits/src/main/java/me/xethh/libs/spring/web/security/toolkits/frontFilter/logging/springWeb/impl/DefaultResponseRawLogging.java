package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.PerformanceLog;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.TracingSystemConst.TRANSACTION_HEADER;

public class DefaultResponseRawLogging implements ResponseRawLogging {
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    private SimpleDateFormat format = DateFormatBuilder.Format.ISO8601.getFormatter();
    PerformanceLog performanceLog = PerformanceLog.staticLog;

    List<CustomInfoLog> customInfoLogs = new ArrayList<>();

    @Override
    public void log(ServletResponse servletResponse) {
        String label = "SPR_RES_RAW_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb
                .append("||").append(label).append("|")
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                .append(format.format(new Date())).append("|")
                .append(System.nanoTime()).append(NewLine)
        ;
        if(servletResponse==null)
            sb.append(RawLoggingType.Empty);
        else if(servletResponse instanceof HttpServletResponse)
            sb.append(RawLoggingType.Http);
        else
            sb.append(RawLoggingType.OtherServlet);
        sb.append("Class name=").append(servletResponse==null?"":servletResponse.getClass().getName());


        if(servletResponse == null){
            sb.append("Logging Type= ").append(RawLoggingType.Empty);

        }
        else if(servletResponse instanceof HttpServletResponse){
            sb.append("Logging Type= ").append(RawLoggingType.Http);
            sb.append("Response status=").append(((HttpServletResponse) servletResponse).getStatus()).append(NewLine);

            sb.append("Response Header: ").append(NewLine);
            Collection<String> headers = ((HttpServletResponse) servletResponse).getHeaderNames();
            for(String header : headers){
                sb.append(header).append("=[");
                sb.append(((HttpServletResponse) servletResponse).getHeaders(header).stream().collect(Collectors.joining(","))).append("]").append(NewLine);
            }
            if(servletResponse instanceof CachingResponseWrapper){
                sb.append(NewLine).append("Response Body: ").append(NewLine).append(((CachingResponseWrapper) servletResponse).getOutputStringContent()).append(NewLine);
            }
        }
        else{
            sb.append("Logging Type= ").append(RawLoggingType.OtherServlet);
        }

        //Custom logging
        customInfoLogs.stream().forEach(x->{
            sb.append("Customized info: ").append(NewLine);
            x.log(sb,servletResponse);

        });

        logger.info(sb.toString());

        performanceLog.logEnd(label, logger);
    }

    public interface CustomInfoLog{
        void log(StringBuilder sb, ServletResponse servletResponse);
    }

    public void setCustomInfoLogs(List<CustomInfoLog> customInfoLogs) {
        this.customInfoLogs = customInfoLogs;
    }

    public Logger logger(){
        return LoggerFactory.getLogger("special-response-log");
    }

    SimpleDateFormat sdf = DateFormatBuilder.Format.ISO8601.getFormatter();
}
