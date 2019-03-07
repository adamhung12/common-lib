package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.DefaultRequestAccessLogging;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class DefaultAccessLoggingConfig {
    @Bean
    public DefaultRequestAccessLogging defaultZuulAccessLogging(
        @Value("${first-filter.zuul.request-access-log.log-name}") String logName
    ){
        DefaultRequestAccessLogging logging = new DefaultRequestAccessLogging();
        logging.setLogger(LoggerFactory.getLogger(logName));
        return logging;
    }
}
