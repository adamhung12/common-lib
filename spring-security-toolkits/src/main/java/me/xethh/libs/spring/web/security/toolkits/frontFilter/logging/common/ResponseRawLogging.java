package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common;

import javax.servlet.ServletResponse;

public interface ResponseRawLogging {
    void log(ServletResponse servletResponse);
}
