package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.context.RequestContext;

public interface RouteAuthenticationSetter {
    void set(RequestContext request);
}
