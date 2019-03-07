package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;

public interface ResponseAccessLogging {
    void log(CachingResponseWrapper responseWrapper);
}
