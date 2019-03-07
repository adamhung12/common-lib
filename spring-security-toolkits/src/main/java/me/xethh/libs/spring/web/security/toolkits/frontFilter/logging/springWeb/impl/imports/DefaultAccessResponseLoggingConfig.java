package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.DefaultResponseAccessLogging;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class DefaultAccessResponseLoggingConfig {
    @Bean
    public DefaultResponseAccessLogging defaultSpringAccessResponseLogging(
        @Value("${first-filter.webmvc.response-access-log.log-name}") String logName
    ){
        DefaultResponseAccessLogging logging = new DefaultResponseAccessLogging();
        logging.setLogger(LoggerFactory.getLogger(logName));
        return logging;
    }

}
