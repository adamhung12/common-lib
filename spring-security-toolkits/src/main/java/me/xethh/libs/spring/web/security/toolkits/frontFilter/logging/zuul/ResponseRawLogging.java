package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul;

import javax.servlet.ServletResponse;

public interface ResponseRawLogging extends me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.ResponseRawLogging {
    void log(ServletResponse servletResponse);
}
