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

public class CachingRequestWrapper extends HttpServletRequestWrapper implements MutableHttpRequest {
    /**
     * Constructs a mutableRequest object wrapping the given mutableRequest.
     *
     * @param mutableRequest The mutableRequest to wrap
     * @throws IllegalArgumentException if the mutableRequest is null
     */
    private byte[] cachedContent;
    private MutableHttpRequestWrapper mutableRequest;
    private HttpServletRequest rawRequest;
    private ResettableServletInputStream servletStream;

    private boolean isCacheable = false;
    private boolean isEditable = false;

    public CachingRequestWrapper(HttpServletRequest rawRequest) {
        super(rawRequest);
        this.mutableRequest = rawRequest instanceof MutableHttpRequestWrapper? (MutableHttpRequestWrapper) rawRequest :new MutableHttpRequestWrapper(rawRequest);
        this.rawRequest = rawRequest;
        servletStream = new ResettableServletInputStream();
    }
    public void putHeader(String name, String value){
        if(isEditable)
            mutableRequest.putHeader(name,value);
        else
            throw new RuntimeException("Editing header is not enabled");
    }

    public String getHeader(String name) {
        return isEditable?mutableRequest.getHeader(name):rawRequest.getHeader(name);
    }

    public Enumeration<String> getHeaderNames() {
        return isEditable?mutableRequest.getHeaderNames():rawRequest.getHeaderNames();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(isCacheable){
            if(cachedContent==null){
                cachedContent = IOUtils.toByteArray(super.getInputStream());
                servletStream.stream = new ByteArrayInputStream(cachedContent);
            }
            return servletStream;
        }
        else
            return super.getInputStream();
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
        if(!isCacheable) throw new RuntimeException("Request is not cacheable");
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
        return isEditable?mutableRequest.getHeaders(name):rawRequest.getHeaders(name);
    }

    public boolean isCacheable() {
        return isCacheable;
    }

    public void setCacheable(boolean cacheable) {
        isCacheable = cacheable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
