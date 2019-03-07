package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;

public interface ResponseAccessLogging extends me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.ResponseAccessLogging {
    void log(CachingResponseWrapper responseWrapper);
}
