package me.xethh.libs.spring.web.security.toolkits.frontFilter.imports;

import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequest;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.*;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.AppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.DefaultAppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.NoneAppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.RequestAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.RequestRawLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.ResponseAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.ResponseRawLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.requestModifier.RequestModifier;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.IdProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.MachineBasedProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.TimeBasedProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class Config {


    @Autowired FirstFilterProperties firstFilterProperties;

    @Bean
    public RequestModifier defaultRequestModifier(){
        return request->{};
    }

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
    ){
        FirstFilter filter = new FirstFilter();
        return filter;
    }
}
