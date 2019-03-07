package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common;

import javax.servlet.ServletRequest;

public interface RequestRawLogging {
    void log(ServletRequest servletRequest);
}
