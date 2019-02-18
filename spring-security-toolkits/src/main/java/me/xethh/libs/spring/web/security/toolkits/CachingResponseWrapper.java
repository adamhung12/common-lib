package me.xethh.libs.spring.web.security.toolkits;

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
import java.util.Collection;

public class CachingResponseWrapper extends HttpServletResponseWrapper implements WithLogger {
    HttpServletResponse response;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    boolean enableLog;
    String logName;
    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        this(response,false);
    }

    public CachingResponseWrapper(HttpServletResponse response, boolean enableLog) {
        this(response,enableLog,"special-response-log");
    }
    public CachingResponseWrapper(HttpServletResponse response, boolean enableLog, String logName) {
        super(response);
        this.response = response;
        this.enableLog = enableLog;
        this.logName = logName;
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
    public ServletOutputStream getOutputStream() throws IOException {
        Log log = enableLog ? () -> {
            Logger logger = LoggerFactory.getLogger(logName);
            Collection<String> hs = getHeaderNames();
            logger.info("Logging response: ");
            for(String h: hs){
                logger.info("Header: "+h);
                for(String s : getHeaders(h)){
                    logger.info("Value: "+s);
                }
            }
        }
        : ()->{}
        ;
        log.log();
        return new ServletOutputStream() {
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
        };
    }
}
