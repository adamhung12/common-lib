package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequestWrapper;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFactory;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class FirstFilter extends GenericFilterBean implements WithLogger {
    private Logger logger = logger();
    private Logger firstFilterAccessLogger = LoggerFactory.getLogger("special-access-log");
    private Logger firstFilterRawLogger = LoggerFactory.getLogger("special-request-log");

    private List<AccessLogging> accessLoggingList = new ArrayList<>();
    private List<RawRequestLogging> rawRequestLoggingList = new ArrayList<>();
    private boolean cacheResponse=false;
    private boolean cacheRequest=false;
    private boolean enableRoutingMarker=false;
    private boolean enableAccessLogging=true;
    private boolean enableRawRequestLogging =true;
    private Supplier<String> markerProvider;
    private Supplier<String> markerHeaderProvider;

    public void setEnableRoutingMarker(boolean enableRoutingMarker, Supplier<String> markerHeaderProvider, Supplier<String> markerProvider) {
        this.enableRoutingMarker = enableRoutingMarker;
        this.markerHeaderProvider = markerHeaderProvider;
        this.markerProvider = markerProvider;
    }

    public void setAccessLoggingList(List<AccessLogging> accessLoggingList) {
        this.accessLoggingList = accessLoggingList;
    }

    public void setRawRequestLoggingList(List<RawRequestLogging> rawRequestLoggingList) {
        this.rawRequestLoggingList = rawRequestLoggingList;
    }

    public void setCacheResponse(boolean cacheResponse) {
        this.cacheResponse = cacheResponse;
    }

    public void setCacheRequest(boolean cacheRequest) {
        this.cacheRequest = cacheRequest;
    }

    public void setEnableAccessLogging(boolean enableAccessLogging) {
        this.enableAccessLogging = enableAccessLogging;
    }

    public void setEnableRawRequestLogging(boolean enableRawRequestLogging) {
        this.enableRawRequestLogging = enableRawRequestLogging;
    }

    public void setAccessLogName(String accessLogName) {
        if(accessLogName!=null && !accessLogName.equalsIgnoreCase(""))
            this.firstFilterAccessLogger = LoggerFactory.getLogger(accessLogName);
    }

    public void setRawRequestLog(String rawRequestLog) {
        if(rawRequestLog!=null && !rawRequestLog.equalsIgnoreCase(""))
            this.firstFilterRawLogger = LoggerFactory.getLogger(rawRequestLog);
    }

    public Logger getFirstFilterAccessLogger() {
        return firstFilterAccessLogger;
    }

    public Logger getFirstFilterRawLogger() {
        return firstFilterRawLogger;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(logger.isDebugEnabled())
            logger.debug("Start first filter");
        ServletRequest newRequest = servletRequest;
        if(enableRoutingMarker && newRequest instanceof HttpServletRequest){
            newRequest = new MutableHttpRequestWrapper((HttpServletRequest) newRequest);
        }
        if(cacheRequest && newRequest instanceof HttpServletRequest){
            newRequest = new CachingRequestWrapper((HttpServletRequest) newRequest);
            ((CachingRequestWrapper) newRequest).putHeader(markerHeaderProvider.get(),markerProvider.get());
        }
        ServletResponse newResponse = servletResponse;
        if(cacheResponse && newResponse instanceof HttpServletResponse){
            newResponse = new CachingResponseWrapper((HttpServletResponse) newResponse);
        }

        if(enableAccessLogging && accessLoggingList.size()>0){
            ServletRequest finalNewRequest = newRequest;
            accessLoggingList.stream().forEach(x->x.log(getFirstFilterAccessLogger(), finalNewRequest));
        }
        if(enableRawRequestLogging && rawRequestLoggingList.size()>0){
            ServletRequest finalNewRequest1 = newRequest;
            rawRequestLoggingList.stream().forEach(x->x.log(getFirstFilterRawLogger(), finalNewRequest1));
        }

        filterChain.doFilter(newRequest,newResponse);
    }
}
