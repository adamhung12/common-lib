package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import org.slf4j.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface RawResponseLogging {
    void log(Logger logger, ServletResponse servletResponse);
}
