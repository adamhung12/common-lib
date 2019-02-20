package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequestWrapper;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFactory;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.regex.Pattern;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class FirstFilter extends GenericFilterBean implements WithLogger {
    public static String TRANSACTION_HEADER = "CUST-TRANSACTION-ID";
    public static String TRANSACTION_LEVEL = "TRANSACTION_LEVEL";
    public interface RequestModifier{
        void operation(CachingRequestWrapper requestWrapper);
    }
    public interface ResponseModifier{
        void operation(CachingResponseWrapper responseWrapper);
    }

    private Logger logger = logger();
    private Logger firstFilterAccessLogger = LoggerFactory.getLogger("special-access-log");
    private Logger firstFilterRawLogger = LoggerFactory.getLogger("special-request-log");

    private List<AccessLogging> accessLoggingList = new ArrayList<>();
    private List<RawRequestLogging> rawRequestLoggingList = new ArrayList<>();
    private boolean enableAccessLogging=true;
    private boolean enableRawRequestLogging =true;
    private CachingResponseWrapper.LogOperation logOperation = response->{};
    private RequestModifier requestModifier = req->{};
    private ResponseModifier responseModifier = res->{};

    String timestamp = DateFactory.now().format(DateFormatBuilder.Format.NUMBER_DATETIME);
    AtomicLong longProvider = new AtomicLong(0);
    private Supplier<String> transactionIdProvider = ()->{
        return timestamp+"_"+ String.format("%09d", longProvider.incrementAndGet());
    };

    public void setTransactionIdProvider(Supplier<String> transactionIdProvider) {
        this.transactionIdProvider = transactionIdProvider;
    }

    public void setRequestHeader(RequestModifier modifier) {
        this.requestModifier = modifier;
    }
    public void setResponseHeader(ResponseModifier modifier) {
        this.responseModifier = modifier;
    }

    public void setAccessLoggingList(List<AccessLogging> accessLoggingList) {
        this.accessLoggingList = accessLoggingList;
    }

    public void setRawRequestLoggingList(List<RawRequestLogging> rawRequestLoggingList) {
        this.rawRequestLoggingList = rawRequestLoggingList;
    }

    public void setResponselogging(CachingResponseWrapper.LogOperation operation){
        this.logOperation = operation;
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

    Pattern numberPattern = Pattern.compile("\\d+");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(logger.isDebugEnabled())
            logger.debug("Start first filter");
        MDC.clear();
        ServletRequest newRequest = servletRequest;

        if(newRequest instanceof HttpServletRequest){
            newRequest = new CachingRequestWrapper((HttpServletRequest) newRequest);
            if(requestModifier !=null)
                requestModifier.operation((CachingRequestWrapper) newRequest);

            Object transactionId = ((HttpServletRequest)servletRequest).getHeader(TRANSACTION_HEADER);
            if(transactionId!=null && transactionId instanceof String && !transactionId.equals("")){
                MDC.put(TRANSACTION_HEADER, (String) transactionId);
                String transactionLevel = ((HttpServletRequest) servletRequest).getHeader(TRANSACTION_LEVEL);
                if(transactionLevel!=null && numberPattern.matcher(transactionLevel).matches())
                    MDC.put(TRANSACTION_LEVEL, String.valueOf(Integer.parseInt(transactionLevel))+1);
                else
                    throw new RuntimeException("Missing transaction level");


                if(((HttpServletResponse) servletResponse).containsHeader(TRANSACTION_HEADER))
                    ((HttpServletResponse) servletResponse).setHeader(TRANSACTION_HEADER, (String) transactionId);

                if(((HttpServletResponse) servletResponse).containsHeader(TRANSACTION_LEVEL))
                    ((HttpServletResponse) servletResponse).setHeader(TRANSACTION_LEVEL, transactionLevel);
            }
            else{
                String id = transactionIdProvider.get();
                String level = 0 + "";
                MDC.put(TRANSACTION_HEADER, id);
                MDC.put(TRANSACTION_LEVEL,level);
                // if(((HttpServletResponse) servletResponse).containsHeader(TRANSACTION_HEADER))
                //     ((HttpServletResponse) servletResponse).setHeader(TRANSACTION_HEADER, id);
                //
                // if(((HttpServletResponse) servletResponse).containsHeader(TRANSACTION_LEVEL))
                //     ((HttpServletResponse) servletResponse).setHeader(TRANSACTION_LEVEL, level);
            }
        }

        ServletResponse newResponse = servletResponse;
        if(newResponse instanceof HttpServletResponse){
            if(MDC.get(TRANSACTION_LEVEL).equals("0"))
                newResponse = new CachingResponseWrapper((HttpServletResponse) newResponse, logOperation);
            else
                newResponse = new CachingResponseWrapper((HttpServletResponse) newResponse, logOperation);
            if(responseModifier != null)
                responseModifier.operation((CachingResponseWrapper) newResponse);
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
