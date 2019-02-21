package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Response;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.impl.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;

public class DefaultAccessResponseLogging implements AccessResponseLogging {
    private SimpleDateFormat format = DateFormatBuilder.ISO8601();
    PerformanceLog performanceLog = PerformanceLog.staticLog;


    public interface CustomMessage{
        void message(StringBuilder sb);
    }

    List<CustomMessage> messageList = new ArrayList<>();

    public void setMessageList(List<CustomMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void log(Logger logger, Response response) {
        String label = "FEI_RES_ACC_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        sb
                //Prefix
                .append("||").append(label).append("|")
                //Time
                .append(format.format(new Date())).append("|")
                //Nano Time
                .append(System.nanoTime()).append("|")
                //Separator
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                //Status
                .append(response.status()).append("|")
        ;
        logger.info(sb.toString());

        //Log end
        performanceLog.logStart(label,logger);
    }
}
