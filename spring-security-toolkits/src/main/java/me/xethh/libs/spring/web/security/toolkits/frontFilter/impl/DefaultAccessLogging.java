package me.xethh.libs.spring.web.security.toolkits.frontFilter.impl;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawLoggingType;
import me.xethh.utils.dateManipulation.DateFactory;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DefaultAccessLogging implements AccessLogging {
    private SimpleDateFormat format = DateFormatBuilder.ISO8601();
    public interface CustomMessage{
        void message(StringBuilder sb);
    }

    List<CustomMessage> messageList = new ArrayList<>();

    public void setMessageList(List<CustomMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public void log(Logger logger, ServletRequest servletRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(">>>AL_V1>>");
        if(servletRequest==null)
            sb.append(RawLoggingType.Empty.name()).append("||");
        else if(servletRequest instanceof HttpServletRequest)
            sb.append(RawLoggingType.Http.name()).append("||");
        else
            sb.append(RawLoggingType.OtherServelet.name()).append("||");
        sb.append(format.format(new Date())).append("|").append(System.nanoTime()).append("|");
        sb.append("###");

        if(servletRequest != null && servletRequest instanceof HttpServletRequest){
            sb.append(System.nanoTime()).append("|");
            sb.append(servletRequest.getRemoteHost()).append("|");
            sb.append(servletRequest.getRemoteAddr()).append("|");
            sb.append(servletRequest.getRemotePort()).append("|");
            sb.append(servletRequest.isSecure()).append("|");
        }
        sb.append("###");

        if(servletRequest!=null){
            if(servletRequest instanceof HttpServletRequest){
                sb.append(((HttpServletRequest) servletRequest).getRequestedSessionId()).append("|");
                sb.append(((HttpServletRequest) servletRequest).getMethod()).append("|");
                sb.append(((HttpServletRequest) servletRequest).getRequestURI()).append("|");
                sb.append(((HttpServletRequest) servletRequest).getRequestURI()).append("|");
            }
            else{
                sb.append(servletRequest.getClass().getName()).append("|");
            }
        }
        sb.append("###");

        for(CustomMessage messageBuild : messageList){
            messageBuild.message(sb);
            sb.append("###");
        }

        sb.append(">>>||");
        logger.info(sb.toString());
    }
}
