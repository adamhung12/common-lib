package me.xethh.libs.spring.web.security.toolkits.zuulFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_HEADER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

public class ZuulPostFilter extends ZuulFilter implements WithLogger {
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

    SimpleDateFormat sdf = DateFormatBuilder.ISO8601();
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse res = ctx.getResponse();
        Logger logger = logger();
        StringBuilder sb = new StringBuilder();
        String NewLine = "\r\n";
        sb
                .append("<<<ZR_V1<<")
                .append(sdf.format(new Date())).append("|")
                .append(System.nanoTime()).append("|")
                .append(ctx.getResponseStatusCode()).append("|")
                .append(res.getHeaders(TRANSACTION_HEADER)).append("|")
                .append("<<<||")
        ;
        logger.info(sb.toString());
        return null;
    }
}
