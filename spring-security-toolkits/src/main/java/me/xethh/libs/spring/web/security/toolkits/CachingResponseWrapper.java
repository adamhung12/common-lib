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
import java.util.List;
import java.util.function.Supplier;

public class CachingResponseWrapper extends HttpServletResponseWrapper implements WithLogger {
    public interface LogOperation{
        void log(CachingResponseWrapper responseWrapper);
    }
    HttpServletResponse response;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    LogOperation logOperation;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        this(response,resp->{});
    }

    public CachingResponseWrapper(HttpServletResponse response, LogOperation log) {
        super(response);
        this.response = response;
        this.logOperation = log;
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
                if(logOperation!=null) logOperation.log(rs);
            }

        };

    }
}
