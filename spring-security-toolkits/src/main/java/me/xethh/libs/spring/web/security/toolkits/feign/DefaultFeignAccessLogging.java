package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Request;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.impl.PerformanceLog;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;

public class DefaultFeignAccessLogging implements AccessLogging {
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
    public void log(Logger logger, Request request) {
        String label = "FEI_REQ_ACC_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        //Prefix
        sb.append("||").append(label).append("|");

        //Type
        if(request==null)
            sb.append(RawLoggingType.Empty.name()).append("|");
        else
            sb.append(RawLoggingType.FeignReq).append("|");

        sb
                //Time
                .append(format.format(new Date())).append("|")
                //Nano Time
                .append(System.nanoTime()).append("|")
                //Separator
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                .append(request.url()).append("|")
        ;
        sb.append("###");

        for (CustomMessage messageBuild : messageList) {
            messageBuild.message(sb);
        }
        sb.append("###");
        logger.info(sb.toString());

        //Log end
        performanceLog.logEnd(label, logger);
    }
}
