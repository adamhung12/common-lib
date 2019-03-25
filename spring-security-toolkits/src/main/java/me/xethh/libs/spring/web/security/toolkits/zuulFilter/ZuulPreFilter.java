package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.RequestAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.RequestRawLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.TracingSystemConst.TRANSFERRING_MESSAGES;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_FORWARD_FILTER_ORDER;

@EnableConfigurationProperties(FirstFilterProperties.class)
public class ZuulPreFilter extends ZuulFilter implements WithLogger {
    @Value("${first-filter.zuul.request-access-log.enabled}")
    private boolean enableRequestAccessLog;
    @Value("${first-filter.zuul.request-raw-log.enabled}")
    private boolean enableRequestRawLog;

    @Autowired
    private RouteAuthenticationSetter routeAuthenticationSetter;


    @Autowired
    private List<RequestAccessLogging> accessRequestLoggingList = new ArrayList<>();
    @Autowired
    private List<RequestRawLogging> requestRawLoggingList = new ArrayList<>();

    public boolean isEnableRequestAccessLog() {
        return enableRequestAccessLog;
    }

    public void setEnableRequestAccessLog(boolean enableRequestAccessLog) {
        this.enableRequestAccessLog = enableRequestAccessLog;
    }

    public boolean isEnableRequestRawLog() {
        return enableRequestRawLog;
    }

    public void setEnableRequestRawLog(boolean enableRequestRawLog) {
        this.enableRequestRawLog = enableRequestRawLog;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_FORWARD_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    SimpleDateFormat sdf = DateFormatBuilder.Format.ISO8601.getFormatter();

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        for (Map.Entry<String, String> keySet : MDC.getCopyOfContextMap().entrySet()) {
            if (TRANSFERRING_MESSAGES.contains(keySet.getKey())) {
                ctx.addZuulRequestHeader(keySet.getKey(), keySet.getValue());
            }

        }

        HttpServletRequest req = ctx.getRequest();
        if (enableRequestAccessLog && accessRequestLoggingList.size() > 0)
            accessRequestLoggingList.stream().forEach(x -> x.log(req));
        if (enableRequestRawLog && requestRawLoggingList.size() > 0)
            requestRawLoggingList.stream().forEach(x -> x.log(req));

        routeAuthenticationSetter.set(ctx);
        if (req instanceof CachingRequestWrapper) {
            logger().info("Replacing the authentication");
            req.removeAttribute("Authorization");
            req.setAttribute("Authorization", Base64.getEncoder().encodeToString("CITIC_CPAAS_API:SAAPC_CITIC_@#21".getBytes()));
        }
        return null;
    }
}
