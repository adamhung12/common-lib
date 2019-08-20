package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.context.RequestContext;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.springframework.beans.factory.annotation.Value;

import java.util.Base64;

public class DefaultRouteAuthenticationSetter implements RouteAuthenticationSetter, WithLogger {
    @Value("${first-filter.zuul.with-authen.authen-type:None}")
    private FirstFilterProperties.AuthenType authenType;
    @Value("${first-filter.zuul.with-authen.value:}")
    private String configValue;

    @Override
    public void set(RequestContext ctx) {
        logger().info("Start revise authentication information");
        if(authenType.equals(FirstFilterProperties.AuthenType.None)){
            return;
        }
        if(authenType.equals(FirstFilterProperties.AuthenType.Basic)){
            ctx.getZuulRequestHeaders().remove("Authorization");
            ctx.addZuulRequestHeader("Authorization", "Basic "+Base64.getEncoder().encodeToString(configValue.getBytes()));
        }
        else{
            ctx.addZuulRequestHeader("Authorization",configValue);
            ctx.addZuulRequestHeader("Authorization", "Bear "+configValue);
        }
    }
}
