package me.xethh.libs.spring.web.security.toolkits.feign.log.impl;

import feign.Response;
import me.xethh.libs.spring.web.security.toolkits.feign.log.RawResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;
import static me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType.FeignRes;

public class DefaultRawResponseLogging implements RawResponseLogging {
    private SimpleDateFormat format = DateFormatBuilder.ISO8601();
    PerformanceLog performanceLog = PerformanceLog.staticLog;

    @Override
    public void log(Logger logger, Response response, String body) {
        String label = "FEI_RES_RAW_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb
                .append("||").append(label).append("|")
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                .append(format.format(new Date())).append("|")
                .append(System.nanoTime()).append(NewLine)
        ;
        sb.append(FeignRes).append(NewLine);

        sb.append("Response status=").append(response.status()).append(NewLine);

        sb.append("Response Header: ").append(NewLine);
        Set<Map.Entry<String, Collection<String>>> headers = response.headers().entrySet();
        headers.stream().forEach(x->{
            sb.append(x.getKey()).append("=").append(x.getValue().stream().collect(Collectors.joining(",","[","]"))).append(NewLine);

        });
        sb.append(NewLine).append("Response Body: ").append(NewLine).append(body).append(NewLine);

        //Custom logging
        customInfoLogs.stream().forEach(x->{
            sb.append("Customized info: ").append(NewLine);
            x.log(sb,response);

        });

        logger.info(sb.toString());

        performanceLog.logEnd(label, logger);
    }


    public interface CustomInfoLog{
        void log(StringBuilder sb, Response response);
    }

    List<CustomInfoLog> customInfoLogs = Arrays.asList((sb, servletResponse) -> {});

    public void setCustomInfoLogs(List<CustomInfoLog> customInfoLogs) {
        this.customInfoLogs = customInfoLogs;
    }

    public Logger logger(){
        return LoggerFactory.getLogger("special-response-log");
    }

    SimpleDateFormat sdf = DateFormatBuilder.ISO8601();
}
