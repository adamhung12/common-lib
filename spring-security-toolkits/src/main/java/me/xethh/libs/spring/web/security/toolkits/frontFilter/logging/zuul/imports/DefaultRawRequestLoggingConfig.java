package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.DefaultRequestRawLogging;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class DefaultRawRequestLoggingConfig {
    @Bean
    public DefaultRequestRawLogging defaultZuulRawRequestLogging(
        @Value("${first-filter.zuul.request-raw-log.log-name}") String logName
    ){
        DefaultRequestRawLogging logging = new DefaultRequestRawLogging();
        logging.setLogger(LoggerFactory.getLogger(logName));
        return logging;
    }
}
