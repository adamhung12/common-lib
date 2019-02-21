package me.xethh.libs.spring.web.security.toolkits.frontFilter.impl;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;

public class DefaultAccessLogging implements AccessLogging {
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
    public void log(Logger logger, ServletRequest servletRequest) {
        String label = "REQ_ACC_V1";
        performanceLog.logStart(label,logger);
        StringBuilder sb = new StringBuilder();
        //Prefix
        sb.append("||").append(label).append("|");

        //Type
        if(servletRequest==null)
            sb.append(RawLoggingType.Empty.name()).append("|");
        else if(servletRequest instanceof HttpServletRequest)
            sb.append(RawLoggingType.Http.name()).append("|");
        else
            sb.append(RawLoggingType.OtherServlet.name()).append("|");

        sb
                //Time
                .append(format.format(new Date())).append("|")
                //Nano Time
                .append(System.nanoTime()).append("|")
                //Separator
                .append(MDC.get(TRANSACTION_HEADER)).append("|")
                //Remote Host
                .append(servletRequest.getRemoteHost()).append("|")
                //Remote IP
                .append(servletRequest.getRemoteAddr()).append("|")
                //Is Secure
                .append(servletRequest.isSecure()).append("|")
        ;
        sb.append("###");

        if(servletRequest != null && servletRequest instanceof HttpServletRequest) {
            if (servletRequest != null) {
                if (servletRequest instanceof HttpServletRequest) {
                    sb
                            //Method
                            .append(((HttpServletRequest) servletRequest).getMethod()).append("|")
                            //URI
                            .append(((HttpServletRequest) servletRequest).getRequestURI()).append("|");
                } else
                    sb
                            //Method
                            .append("|")
                            //URI
                            .append("|")
                            ;
                //Servlet Name
                sb.append(servletRequest.getClass().getName()).append("|");
            }
            sb.append("###");

            for (CustomMessage messageBuild : messageList) {
                messageBuild.message(sb);
            }
            sb.append("###");
        }
        logger.info(sb.toString());

        //Log end
        performanceLog.logEnd(label, logger);
    }
}
