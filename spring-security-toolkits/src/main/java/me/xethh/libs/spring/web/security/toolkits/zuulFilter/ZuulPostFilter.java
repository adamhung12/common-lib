package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.AccessResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.RawResponseLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.ResponsePreFlushLogging;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_FORWARD_FILTER_ORDER;

public class ZuulPostFilter extends ZuulFilter implements WithLogger {
    List<AccessResponseLogging> accessResponseLoggingList = new ArrayList<>();
    List<RawResponseLogging> rawResponseLoggings = new ArrayList<>();
    List<ResponsePreFlushLogging> responsePreFlushLoggings = new ArrayList<>();

    public void setAccessResponseLoggingList(List<AccessResponseLogging> accessResponseLoggingList) {
        this.accessResponseLoggingList = accessResponseLoggingList;
    }

    public void setRawResponseLoggings(List<RawResponseLogging> rawResponseLoggings) {
        this.rawResponseLoggings = rawResponseLoggings;
    }

    public void setResponsePreFlushLoggings(List<ResponsePreFlushLogging> responsePreFlushLoggings) {
        this.responsePreFlushLoggings = responsePreFlushLoggings;
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

    Supplier<Logger> accessLogProvider = ()->logger();
    Supplier<Logger> rawLogProvider = ()->logger();

    public void setAccessLogProvider(Supplier<Logger> accessLogProvider) {
        this.accessLogProvider = accessLogProvider;
    }

    public void setRawLogProvider(Supplier<Logger> rawLogProvider) {
        this.rawLogProvider = rawLogProvider;
    }

    SimpleDateFormat sdf = DateFormatBuilder.ISO8601();
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse res = ctx.getResponse();
        if(!(res instanceof CachingResponseWrapper)){
            CachingResponseWrapper response = new CachingResponseWrapper(res, rawResponseLoggings, accessResponseLoggingList, responsePreFlushLoggings);
            ctx.setResponse(response);
        }
        // Logger logger = logger();
        // StringBuilder sb = new StringBuilder();
        // String NewLine = "\r\n";
        // sb
        //         .append("<<<ZR_V1<<")
        //         .append(sdf.format(new Date())).append("|")
        //         .append(System.nanoTime()).append("|")
        //         .append(ctx.getResponseStatusCode()).append("|")
        //         .append(res.getHeaders(TRANSACTION_HEADER)).append("|")
        //         .append(res.getHeaders(TRANSACTION_LEVEL)).append("|")
        //         .append("<<<||")
        // ;
        // logger.info(sb.toString());
        return null;
    }
}
