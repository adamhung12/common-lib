package me.xethh.libs.spring.web.security.toolkits;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawResponseLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CachingResponseWrapper extends HttpServletResponseWrapper implements WithLogger {
    public interface LogOperation{
        void log(CachingResponseWrapper responseWrapper);
    }
    HttpServletResponse response;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    List<RawResponseLogging> responseLoggings ;
    List<AccessResponseLogging> accessResponseLoggings;

    Supplier<Logger> accessLoggerProvider = this::logger;
    Supplier<Logger> rawLoggerProvider = this::logger;

    public void setAccessResponseLoggings(List<AccessResponseLogging> accessResponseLoggings) {
        this.accessResponseLoggings = accessResponseLoggings;
    }

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        this(response,new ArrayList<>(),new ArrayList<>(),null,null, false, false);
    }

    private boolean enableResponseAccessLog = false;
    private boolean enableResponseRawLog = false;

    public void setEnableResponseAccessLog(boolean enableResponseAccessLog) {
        this.enableResponseAccessLog = enableResponseAccessLog;
    }

    public void setEnableResponseRawLog(boolean enableResponseRawLog) {
        this.enableResponseRawLog = enableResponseRawLog;
    }

    public CachingResponseWrapper(
            HttpServletResponse response,
            List<RawResponseLogging> rawResponseLoggings,
            List<AccessResponseLogging> accessResponseLoggings,
            Supplier<Logger> accessLoggerProvider,
            Supplier<Logger> rawLoggerProvider,
            boolean enableResponseAccessLog,
            boolean enableResponseRawLog
    ) {
        super(response);
        this.response = response;
        this.accessResponseLoggings = accessResponseLoggings;
        this.responseLoggings = rawResponseLoggings;
        this.accessLoggerProvider = accessLoggerProvider==null?()->logger():accessLoggerProvider;
        this.rawLoggerProvider = rawLoggerProvider==null?()->logger():rawLoggerProvider;
        this.enableResponseAccessLog = enableResponseAccessLog;
        this.enableResponseRawLog = enableResponseRawLog;
    }


    public byte[] getOutputContent(){
        return outputStream.toByteArray();
    }
    public String getOutputStringContent(){
        try {
            return IOUtils.toString(getOutputContent(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public interface Log{
        void log();
    }

    @Override
    public void flushBuffer() throws IOException {
        CachingResponseWrapper rs = this;
        super.flushBuffer();

        //Serve case when flush with buff of servlet
        if(enableResponseRawLog && responseLoggings.size()>0)
            responseLoggings.forEach(x->x.log(rawLoggerProvider.get(),rs));
        if(enableResponseAccessLog && accessResponseLoggings.size()>0)
            accessResponseLoggings.stream().forEach(x->x.log(accessLoggerProvider.get(),rs));
    }

    ServletOutputStream _stream;
    boolean flushed = false;

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        CachingResponseWrapper rs = this;
        if(_stream==null)
            _stream =  new ServletOutputStream() {
                @Override
                public void write(int b) throws IOException {
                    response.getOutputStream().write(b);
                    outputStream.write(b);
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener listener) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void flush() throws IOException {
                    super.flush();
                    if(!flushed){
                        //Serve case when flush with output stream
                        if(enableResponseRawLog && responseLoggings.size()>0)
                            responseLoggings.forEach(x->x.log(rawLoggerProvider.get(),rs));
                        if(enableResponseAccessLog && accessResponseLoggings.size()>0)
                            accessResponseLoggings.stream().forEach(x->x.log(accessLoggerProvider.get(),rs));
                        flushed = true;
                    }
                }
            };
        return _stream;
    }
}
