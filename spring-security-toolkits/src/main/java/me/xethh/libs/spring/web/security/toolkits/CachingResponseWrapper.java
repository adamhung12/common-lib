package me.xethh.libs.spring.web.security.toolkits;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.ResponseAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.ResponseRawLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CachingResponseWrapper<AccessLogging extends ResponseAccessLogging, RawLogging extends ResponseRawLogging> extends HttpServletResponseWrapper implements WithLogger {
    @Override
    public Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    public interface LogOperation{
        void log(CachingResponseWrapper responseWrapper);
    }
    HttpServletResponse response;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    List<RawLogging> responseLoggings ;
    List<AccessLogging> responseAccessLoggings;


    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        this(response,false, new ArrayList<>(),new ArrayList<>(),false);
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
            boolean enableResponseRawLog,
            List<RawLogging> responseRawLoggings,
            List<AccessLogging> responseAccessLoggings,
            boolean enableResponseAccessLog
    ) {
        super(response);
        this.response = response;
        this.responseAccessLoggings = responseAccessLoggings;
        this.responseLoggings = responseRawLoggings;
        this.enableResponseAccessLog = enableResponseAccessLog;
        this.enableResponseRawLog = enableResponseRawLog;
    }


    public byte[] getOutputContent(){
        if(!enableResponseRawLog) throw new RuntimeException("Response cache is not enabled");
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

    @Override
    public void flushBuffer() throws IOException {
        CachingResponseWrapper rs = this;
        super.flushBuffer();

        //Serve case when flush with buff of servlet
        if(enableResponseRawLog && responseLoggings.size()>0)
            responseLoggings.forEach(x->x.log(rs));
        if(enableResponseAccessLog && responseAccessLoggings.size()>0)
            responseAccessLoggings.stream().forEach(x->x.log(rs));
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
                    if(enableResponseRawLog)
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
                            responseLoggings.forEach(x->x.log(rs));
                        if(enableResponseAccessLog && responseAccessLoggings.size()>0)
                            responseAccessLoggings.stream().forEach(x->x.log(rs));
                        flushed = true;
                    }
                }
            };
        return _stream;
    }
}
