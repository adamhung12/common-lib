package me.xethh.libs.spring.web.security.toolkits.frontFilter.impl;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;
import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_LEVEL;

public class DefaultResponseLogging implements CachingResponseWrapper.LogOperation {
    public interface CustomInfoLog{
        void log(StringBuilder sb);
    }

    public CustomInfoLog customInfoLog(){
        return sb->{};
    }

    public Logger logger(){
        return LoggerFactory.getLogger("special-response-log");
    }

    SimpleDateFormat sdf = DateFormatBuilder.ISO8601();
    @Override
    public void log(CachingResponseWrapper responseWrapper) {
        Logger logger = logger();
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb.append("<<RE_V1<<").append(NewLine);
        sb.append("CUST-TRANSACTION-ID: "+responseWrapper.getHeaders(TRANSACTION_HEADER)).append(NewLine);

        //Custom logging
        sb.append("Cust info: ").append(NewLine);
        customInfoLog().log(sb);

        sb.append("Time info: ").append(NewLine);
        sb.append("Datetime="+sdf.format(new Date())).append(NewLine);
        sb.append("Nano="+System.nanoTime()).append(NewLine);
        sb.append("Response status=").append(responseWrapper.getStatus()).append(NewLine);

        sb.append("Response Header: ").append(NewLine);
        Collection<String> headers = responseWrapper.getHeaderNames();
        for(String header : headers){
            sb.append(header).append("=[");
            sb.append(responseWrapper.getHeaders(header).stream().collect(Collectors.joining(","))).append("]").append(NewLine);
        }
        sb.append(NewLine).append("Response Body: ").append(NewLine).append(responseWrapper.getOutputStringContent()).append(NewLine);
        sb.append("<<<||");
        logger.info(sb.toString());
    }
}
