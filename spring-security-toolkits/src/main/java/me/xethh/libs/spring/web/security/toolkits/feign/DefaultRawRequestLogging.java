package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Request;
import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.impl.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;
import static me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType.FeignReq;

public class DefaultRawRequestLogging implements RawRequestLogging {
    private boolean passwordProtection = true;
    private SimpleDateFormat format = DateFormatBuilder.ISO8601();
    PerformanceLog performanceLog = PerformanceLog.staticLog;
    @Override
    public void log(Logger logger, Request request) {
        String label = "FEI_REQ_RAW_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb
                .append("||").append(label).append("|")
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                .append(format.format(new Date())).append("|")
                .append(System.nanoTime()).append(NewLine)
        ;
        sb.append(FeignReq).append(NewLine);

        printUrlLogger(sb, request);
        sb.append(NewLine).append("Request Header: ").append(NewLine);
        getHeaderInfo(sb, request);
        sb.append(NewLine).append("Request Params: ").append(NewLine);
        sb.append(NewLine).append("Request Body: ").append(NewLine);
        sb.append(request.requestBody().asString());

        logger.info(sb.toString());
        performanceLog.logEnd(label, logger);
    }


    private static String NewLine = "\r\n";
    private void printUrlLogger(StringBuilder sb, Request request) {
        sb.append("Method: "+ request.httpMethod().name()).append(NewLine);
        sb.append("Request URI: "+ request.url()).append(NewLine);
    }

    public static void getHeaderInfo(StringBuilder sb, Request req) {
        Set<Map.Entry<String, Collection<String>>> headerNames = req.headers().entrySet();
        headerNames.stream().forEach(x->{
            String headerName = x.getKey();
            sb
                    .append(headerName).append("=")
                    .append(x.getValue().stream().collect(Collectors.joining(",","[","]")))
                    .append(NewLine);
        });
    }

}
