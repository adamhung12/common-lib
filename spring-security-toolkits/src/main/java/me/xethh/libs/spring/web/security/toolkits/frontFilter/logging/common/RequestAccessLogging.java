package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common;

import javax.servlet.ServletRequest;

public interface RequestAccessLogging {
    void log(ServletRequest servletRequest);
}
