package me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider;

import java.lang.management.ManagementFactory;

public class DefaultAppNameProvider implements AppNameProvider{
    String appInfo = String.format(ManagementFactory.getRuntimeMXBean().getName());

    @Override
    public String gen() {
        return appInfo;
    }
}
