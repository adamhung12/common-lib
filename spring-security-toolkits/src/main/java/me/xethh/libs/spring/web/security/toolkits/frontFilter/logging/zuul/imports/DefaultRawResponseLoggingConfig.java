package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.DefaultResponseRawLogging;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class DefaultRawResponseLoggingConfig {
    @Bean
    public DefaultResponseRawLogging defaultZuulRawResponseLogging(
        @Value("${first-filter.zuul.response-access-log.log-name}") String logName
    ){
        DefaultResponseRawLogging logging = new DefaultResponseRawLogging();
        logging.setLogger(LoggerFactory.getLogger(logName));
        return logging;
    }

}
