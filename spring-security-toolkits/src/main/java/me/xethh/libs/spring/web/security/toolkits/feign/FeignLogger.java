package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import me.xethh.libs.spring.web.security.toolkits.feign.log.AccessLogging;
import me.xethh.libs.spring.web.security.toolkits.feign.log.AccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.feign.log.RawRequestLogging;
import me.xethh.libs.spring.web.security.toolkits.feign.log.RawResponseLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.apache.commons.codec.Charsets;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FeignLogger extends Logger implements WithLogger {
    List<AccessLogging> accessLoggingList = new ArrayList<>();
    List<RawRequestLogging> rawRequestLoggingList = new ArrayList<>();
    List<AccessResponseLogging> accessResponseLoggings = new ArrayList<>();
    List<RawResponseLogging> rawResponseLoggings = new ArrayList<>();


    private boolean enableRequestAccessLog = false;
    private boolean enableRequestRawLog = false;
    private boolean enableResponseAccessLog = false;
    private boolean enableResponseRawLog = false;

    public void setEnableRequestAccessLog(boolean enableRequestAccessLog) {
        this.enableRequestAccessLog = enableRequestAccessLog;
    }

    public void setEnableRequestRawLog(boolean enableRequestRawLog) {
        this.enableRequestRawLog = enableRequestRawLog;
    }

    public void setEnableResponseAccessLog(boolean enableResponseAccessLog) {
        this.enableResponseAccessLog = enableResponseAccessLog;
    }

    public void setEnableResponseRawLog(boolean enableResponseRawLog) {
        this.enableResponseRawLog = enableResponseRawLog;
    }

    public void setAccessResponseLoggings(List<AccessResponseLogging> accessResponseLoggings) {
        this.accessResponseLoggings = accessResponseLoggings;
    }

    public void setRawResponseLoggings(List<RawResponseLogging> rawResponseLoggings) {
        this.rawResponseLoggings = rawResponseLoggings;
    }

    public void setRawRequestLoggingList(List<RawRequestLogging> rawRequestLoggingList) {
        this.rawRequestLoggingList = rawRequestLoggingList;
    }

    public void setAccessLoggingList(List<AccessLogging> accessLoggingList) {
        this.accessLoggingList = accessLoggingList;
    }

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    private Supplier<org.slf4j.Logger> accessLogLoggerProvider = ()-> logger;
    private Supplier<org.slf4j.Logger> rawLogLoggerProvider = ()-> logger;


    public void setAccessLogLoggerProvider(Supplier<org.slf4j.Logger> loggerProvider) {
        this.accessLogLoggerProvider = loggerProvider;
    }

    public void setRawLogLoggerProvider(Supplier<org.slf4j.Logger> rawLogLoggerProvider) {
        this.rawLogLoggerProvider = rawLogLoggerProvider;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        logger.info(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if(enableRequestAccessLog && accessLoggingList.size()>0)
            accessLoggingList.stream().forEach(x->x.log(accessLogLoggerProvider.get(),request));
        if(enableRequestRawLog && rawRequestLoggingList.size()>0)
            rawRequestLoggingList.stream().forEach(x->x.log(rawLogLoggerProvider.get(),request));
        // StringBuilder sb = new StringBuilder();
        // sb.append("request to: "+request.url()).append("\r\n");
        // sb.append("request headers: "+"\r\n");
        // request.headers().entrySet().stream().forEach(x->{
        //     sb.append(String.format("%s=%s", x.getKey(),x.getValue().stream().collect(Collectors.joining(",")))).append("\r\n");
        // });
        // String body = request.requestBody().asString();
        // sb.append("request body: "+body).append("\r\n");
        // sb.append("request body length: "+body.length()).append("\r\n");
        // logger.info(sb.toString());
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        int status = response.status();
        // StringBuilder sb = new StringBuilder();
        // sb.append("response status: "+response.status()).append("\r\n");
        // response.headers().entrySet().stream().forEach(x->{
        //     sb.append(String.format("%s=%s", x.getKey(),x.getValue().stream().collect(Collectors.joining(",")))).append("\r\n");
        // });

        int bodyLength = 0;
        if (response.body() != null && !(status == 204 || status == 205)) {
            // HTTP 204 No Content "...response MUST NOT include a message-body"
            // HTTP 205 Reset Content "...response MUST NOT include an entity"
            if(enableResponseRawLog && rawResponseLoggings.size()>0){
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                rawResponseLoggings.stream().forEach(x->x.log(rawLogLoggerProvider.get(),response,new String(bodyData, Charsets.UTF_8)));

                if(enableResponseAccessLog && accessResponseLoggings.size()>0)
                    accessResponseLoggings.stream().forEach(x->x.log(accessLogLoggerProvider.get(),response));
                return response.toBuilder().body(bodyData).build();
            }
            else{
                if(enableResponseAccessLog && accessResponseLoggings.size()>0)
                    accessResponseLoggings.stream().forEach(x->x.log(accessLogLoggerProvider.get(),response));
                return response;
            }
        } else {
            if(enableResponseRawLog && rawResponseLoggings.size()>0){
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                rawResponseLoggings.stream().forEach(x->x.log(rawLogLoggerProvider.get(),response,new String(bodyData, Charsets.UTF_8)));

                if(enableResponseAccessLog && accessResponseLoggings.size()>0)
                    accessResponseLoggings.stream().forEach(x->x.log(accessLogLoggerProvider.get(),response));
                return response.toBuilder().body(bodyData).build();
            }
            else{
                if(enableResponseAccessLog && accessResponseLoggings.size()>0)
                    accessResponseLoggings.stream().forEach(x->x.log(accessLogLoggerProvider.get(),response));
                return response;
            }
        }
    }

    @Override
    public org.slf4j.Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}

