package me.xethh.libs.spring.web.security.toolkits.feign.log;

import feign.Response;
import org.slf4j.Logger;

public interface AccessResponseLogging {
    void log(Logger logger, Response response);
}
