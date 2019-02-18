package me.xethh.libs.spring.web.security.toolkits;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CachingRequestWrapper extends HttpServletRequestWrapper implements MutableRequest {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    private byte[] cachedContent;
    private MutableHttpRequestWrapper request;
    private ResettableServletInputStream servletStream;

    public CachingRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request instanceof MutableHttpRequestWrapper? (MutableHttpRequestWrapper) request :new MutableHttpRequestWrapper(request);
        servletStream = new ResettableServletInputStream();
    }
    public void putHeader(String name, String value){
        request.putHeader(name,value);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(cachedContent==null){
            cachedContent = IOUtils.toByteArray(super.getInputStream());
            servletStream.stream = new ByteArrayInputStream(cachedContent);
        }
        return servletStream;
    }

    private class ResettableServletInputStream extends ServletInputStream {
        private ByteArrayInputStream stream;
        private boolean finished = false;

        @Override
        public int read() throws IOException {
            int data = this.stream.read();
            if (data == -1) {
                this.finished = true;
            }
            return data;
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }
    }

    public byte[] getCachedContent() {
        try {
            getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cachedContent;
    }

    public String getCachedStringContent(){
        try {
            return IOUtils.toString(getCachedContent(), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return request.getHeaders(name);
    }
}
