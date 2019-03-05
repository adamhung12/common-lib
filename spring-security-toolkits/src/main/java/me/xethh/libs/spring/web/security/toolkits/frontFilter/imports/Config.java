package me.xethh.libs.spring.web.security.toolkits.frontFilter.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.*;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultAccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultRawRequestLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultRawResponseLogging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;
import java.util.List;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class Config {

    @Bean
    public DefaultAccessResponseLogging defaultAccessResponseLogging(){
        return new DefaultAccessResponseLogging();
    }

    @Bean
    public DefaultRawRequestLogging defaultRawRequestLogging(){
        return new DefaultRawRequestLogging();
    }
    @Bean
    public DefaultAccessLogging defaultAccessLogging(){
        return new DefaultAccessLogging();
    }
    @Bean
    public DefaultRawResponseLogging defaultRawResponseLogging(){
        return new DefaultRawResponseLogging();
    }

    @Value("${first-filter.app-name}")
    private String appName;

    @Bean
    public FirstFilterProperties firstFilterProperties(){
        return new FirstFilterProperties();
    }
    @Bean
    public FirstFilter firstFilter(
            @Autowired List<AccessLogging> accessLoggingList,
            @Autowired List<RawRequestLogging> rawRequestLoggingList,
            @Autowired List<RawResponseLogging> rawResponseLoggings,
            @Autowired List<AccessResponseLogging> accessResponseLoggings,
            @Autowired FirstFilterProperties firstFilterProperties

            // @Value("${log.access.logName}") String accessLogName,
            // @Value("${log.request.logName}") String requestLogName
    ){
        FirstFilter filter = new FirstFilter();
        filter.setAccessLoggingList(accessLoggingList);
        filter.setRawRequestLoggingList(rawRequestLoggingList);

        filter.setRawResponseLoggings(rawResponseLoggings);
        filter.setAccessResponseLoggings(accessResponseLoggings);

        filter.setEnableRequestAccessLog(true);
        filter.setEnableRequestRawLog(false);
        filter.setEnableResponseAccessLog(true);
        filter.setEnableResponseRawLog(false);
        filter.setAppInfo(()-> ManagementFactory.getRuntimeMXBean().getName()+":"+appName);
        return filter;
    }
}
