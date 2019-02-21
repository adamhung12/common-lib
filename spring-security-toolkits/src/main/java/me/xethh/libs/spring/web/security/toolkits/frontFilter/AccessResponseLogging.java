package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import org.slf4j.Logger;

public interface AccessResponseLogging {
    void log(Logger logger, CachingResponseWrapper responseWrapper);
}
