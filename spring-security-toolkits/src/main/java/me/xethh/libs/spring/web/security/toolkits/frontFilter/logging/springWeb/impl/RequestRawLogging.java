package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl;

import javax.servlet.ServletRequest;

public interface RequestRawLogging extends me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.common.RequestRawLogging {
    void log(ServletRequest servletRequest);
}
