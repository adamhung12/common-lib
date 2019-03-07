package me.xethh.libs.spring.web.security.toolkits.feign.log;

import feign.Response;
import org.slf4j.Logger;

import javax.servlet.ServletResponse;

public interface RawResponseLogging {
    void log(Logger logger, Response response, String body);
}
