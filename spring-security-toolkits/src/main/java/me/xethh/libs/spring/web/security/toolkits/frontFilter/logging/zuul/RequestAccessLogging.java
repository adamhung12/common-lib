package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul;

import javax.servlet.ServletRequest;

public interface RequestAccessLogging extends me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.RequestAccessLogging {
    void log(ServletRequest servletRequest);
}
