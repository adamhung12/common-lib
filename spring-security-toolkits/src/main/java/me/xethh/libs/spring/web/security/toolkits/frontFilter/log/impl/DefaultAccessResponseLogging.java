package me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;

public class DefaultAccessResponseLogging implements AccessResponseLogging {
    private SimpleDateFormat format = DateFormatBuilder.Format.ISO8601.getFormatter();
    PerformanceLog performanceLog = PerformanceLog.staticLog;
    public interface CustomMessage{
        void message(StringBuilder sb);
    }

    List<CustomMessage> messageList = new ArrayList<>();

    public void setMessageList(List<CustomMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void log(Logger logger, CachingResponseWrapper responseWrapper) {
        String label = "SPR_RES_ACC_V1";
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
                .append(responseWrapper.getStatus()).append("|")
        ;
        logger.info(sb.toString());

        //Log end
        performanceLog.logStart(label,logger);
    }
}
