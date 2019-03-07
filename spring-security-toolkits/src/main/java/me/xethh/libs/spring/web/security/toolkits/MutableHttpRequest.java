package me.xethh.libs.spring.web.security.toolkits;

import java.util.Enumeration;

public interface MutableHttpRequest {

    void putHeader(String name, String value);

    String getHeader(String name);

    Enumeration<String> getHeaderNames();
    Enumeration<String> getHeaders(String name);
}
