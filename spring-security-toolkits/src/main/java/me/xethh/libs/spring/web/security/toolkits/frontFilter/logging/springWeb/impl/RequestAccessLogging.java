package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl;

import javax.servlet.ServletRequest;

public interface RequestAccessLogging extends me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.RequestAccessLogging {
    void log(ServletRequest servletRequest);
}
