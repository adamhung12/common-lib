package me.xethh.libs.spring.web.security.toolkits.frontFilter.imports;

import me.xethh.libs.spring.web.security.toolkits.frontFilter.*;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.AppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.DefaultAppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.NoneAppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultAccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultRawRequestLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.log.impl.DefaultRawResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.IdProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.MachineBasedProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.TimeBasedProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired FirstFilterProperties firstFilterProperties;

    @Bean
    public AppNameProvider appNameProvider(){
        switch (firstFilterProperties.getAppNameProvider().getType()){
            case Default:
                return new DefaultAppNameProvider();
            case None:
                return new NoneAppNameProvider();
            case Custom:
                throw new RuntimeException("app name provider not supported");
        }
        throw new RuntimeException("Fail to app name provider");
    }

    @Bean
    public IdProvider idBuilder(){
        switch (firstFilterProperties.getTransactionId().getType()){
            case Time_Base:
                return new TimeBasedProvider();
            case Machine_Time_Based:
                MachineBasedProvider mb = new MachineBasedProvider();
                mb.setServiceId(firstFilterProperties.getServiceId());
                return mb;
            case Custom:
                throw new RuntimeException("Custom id provider not supported");
        }
        throw new RuntimeException("Fail to create id provider");
    }
    @Bean
    public FirstFilter firstFilter(
            @Autowired List<AccessLogging> accessLoggingList,
            @Autowired List<RawRequestLogging> rawRequestLoggingList,
            @Autowired List<RawResponseLogging> rawResponseLoggings,
            @Autowired List<AccessResponseLogging> accessResponseLoggings

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
        return filter;
    }
}
