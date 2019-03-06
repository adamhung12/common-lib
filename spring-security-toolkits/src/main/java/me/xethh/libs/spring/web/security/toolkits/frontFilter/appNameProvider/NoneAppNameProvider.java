package me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider;

public class NoneAppNameProvider implements AppNameProvider{
    @Override
    public String gen() {
        return "";
    }
}
