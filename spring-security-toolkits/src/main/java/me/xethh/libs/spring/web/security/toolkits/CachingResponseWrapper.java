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
        this(response,new ArrayList<>(),new ArrayList<>(),null,null);
    }

    public CachingResponseWrapper(
            HttpServletResponse response,
            List<RawResponseLogging> rawResponseLoggings,
            List<AccessResponseLogging> accessResponseLoggings,
            Supplier<Logger> accessLoggerProvider,
            Supplier<Logger> rawLoggerProvider
    ) {
        super(response);
        this.response = response;
        this.accessResponseLoggings = accessResponseLoggings;
        this.responseLoggings = rawResponseLoggings;
        this.accessLoggerProvider = accessLoggerProvider==null?()->logger():accessLoggerProvider;
        this.rawLoggerProvider = rawLoggerProvider==null?()->logger():rawLoggerProvider;
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
        responseLoggings.stream().forEach(x->x.log(rawLoggerProvider.get(),rs));
        accessResponseLoggings.forEach(x->x.log(accessLoggerProvider.get(),rs));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        CachingResponseWrapper rs = this;
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

            @Override
            public void flush() throws IOException {
                super.flush();
                //Serve case when flush with output stream
                responseLoggings.stream().forEach(x->x.log(rawLoggerProvider.get(),rs));
                accessResponseLoggings.forEach(x->x.log(accessLoggerProvider.get(),rs));
            }



        };

    }
}
