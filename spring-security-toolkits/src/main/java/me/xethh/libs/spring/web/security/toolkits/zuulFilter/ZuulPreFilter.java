package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawRequestLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.*;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_FORWARD_FILTER_ORDER;

public class ZuulPreFilter extends ZuulFilter implements WithLogger {
    List<AccessLogging> loggingList = new ArrayList<>();
    List<RawRequestLogging> rawRequestLoggings = new ArrayList<>();

    private boolean enableRequestAccessLog = false;
    private boolean enableRequestRawLog = false;

    public void setEnableRequestAccessLog(boolean enableRequestAccessLog) {
        this.enableRequestAccessLog = enableRequestAccessLog;
    }

    public void setEnableRequestRawLog(boolean enableRequestRawLog) {
        this.enableRequestRawLog = enableRequestRawLog;
    }

    public void setLoggingList(List<AccessLogging> loggingList) {
        this.loggingList = loggingList;
    }

    public void setRawRequestLoggings(List<RawRequestLogging> rawRequestLoggings) {
        this.rawRequestLoggings = rawRequestLoggings;
    }

    Supplier<Logger> accessLogProvider = () -> logger();
    Supplier<Logger> rawLogProvider = () -> logger();

    public void setAccessLogProvider(Supplier<Logger> accessLogProvider) {
        this.accessLogProvider = accessLogProvider;
    }

    public void setRawLogProvider(Supplier<Logger> rawLogProvider) {
        this.rawLogProvider = rawLogProvider;
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
        // ctx.addZuulRequestHeader(TRANSACTION_HEADER, MDC.get(TRANSACTION_HEADER));
        // ctx.addZuulRequestHeader(TRANSACTION_LEVEL, MDC.get(TRANSACTION_LEVEL));
        // ctx.addZuulRequestHeader(TRANSACTION_AGENT, "ZUUL");
        // ctx.addZuulRequestHeader(TRANSACTION_SESSION_ID, MDC.get(TRANSACTION_SESSION_ID));

        HttpServletRequest req = ctx.getRequest();
        if (enableRequestAccessLog && loggingList.size() > 0)
            loggingList.stream().forEach(x -> x.log(accessLogProvider.get(), req));
        if (enableRequestRawLog && rawRequestLoggings.size() > 0)
            rawRequestLoggings.stream().forEach(x -> x.log(rawLogProvider.get(), req));

        if (req instanceof CachingRequestWrapper) {
            logger().info("Replacing the authentication");
            req.removeAttribute("Authorization");
            req.setAttribute("Authorization", Base64.getEncoder().encodeToString("CITIC_CPAAS_API:SAAPC_CITIC_@#21".getBytes()));
        }

        // Logger logger = logger();
        // StringBuilder sb = new StringBuilder();
        // String NewLine = "\r\n";
        // sb
        //         .append(">>>ZR_V1>>")
        //         .append(sdf.format(new Date())).append("|")
        //         .append(System.nanoTime()).append("|")
        //         .append(ctx.getZuulRequestHeaders().get(TRANSACTION_HEADER)).append("|")
        //         .append(">>>||")
        // ;
        // logger.info(sb.toString());
        return null;
    }
}
