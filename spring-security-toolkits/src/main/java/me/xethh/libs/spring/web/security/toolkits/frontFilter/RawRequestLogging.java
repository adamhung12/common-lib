package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import org.slf4j.Logger;

import javax.servlet.ServletRequest;

public interface RawRequestLogging {
    void log(Logger logger, ServletRequest servletRequest);
}
