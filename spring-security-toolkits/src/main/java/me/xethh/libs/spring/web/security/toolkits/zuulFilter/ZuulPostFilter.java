package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.ResponseAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.ResponseRawLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_FORWARD_FILTER_ORDER;

public class ZuulPostFilter extends ZuulFilter implements WithLogger {
    @Autowired
    private List<ResponseRawLogging> responseRawLoggings = new ArrayList<>();
    @Autowired
    private List<ResponseAccessLogging> responseAccessLoggings = new ArrayList<>();
    @Value("${first-filter.zuul.response-access-log.enabled}")
    private boolean enableResponseAccessLog = false;
    @Value("${first-filter.zuul.response-raw-log.enabled}")
    private boolean enableResponseRawLog = false;

    public void setEnableResponseAccessLog(boolean enableResponseAccessLog) {
        this.enableResponseAccessLog = enableResponseAccessLog;
    }

    public void setEnableResponseRawLog(boolean enableResponseRawLog) {
        this.enableResponseRawLog = enableResponseRawLog;
    }

    public void setResponseRawLoggings(List<ResponseRawLogging> responseRawLoggings) {
        this.responseRawLoggings = responseRawLoggings;
    }

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_FORWARD_FILTER_ORDER+1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    SimpleDateFormat sdf = DateFormatBuilder.Format.ISO8601.getFormatter();
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse res = ctx.getResponse();
        if(!(res instanceof CachingResponseWrapper)){
            CachingResponseWrapper response = new CachingResponseWrapper(res, enableResponseRawLog, responseRawLoggings
                    , responseAccessLoggings,enableResponseAccessLog);
            ctx.setResponse(response);
        }
        return null;
    }

    @Override
    public Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
