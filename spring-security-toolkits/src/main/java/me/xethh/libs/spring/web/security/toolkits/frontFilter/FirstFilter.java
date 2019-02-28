package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.dateManipulation.DateFactory;
import me.xethh.utils.dateManipulation.DateFormatBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.regex.Pattern;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class FirstFilter extends GenericFilterBean implements WithLogger {
    public static String APP_NAME = "APP-NAME";
    public static String TRANSACTION_HEADER = "CUST-TRANSACTION-ID";
    public static String TRANSACTION_LEVEL = "CUST-TRANSACTION-LEVEL";
    public static String TRANSACTION_AGENT = "CUST-TRANSACTION-AGENT";
    public static String TRANSACTION_SESSION_ID = "CUST-TRANSACTION-SESSION-ID";
    public static String TRANSACTION_CLIENT_ID = "CUST-CLIENT-ID";
    public static List<String> TRANSFERRING_MESSAGES = Arrays.asList(
            APP_NAME,TRANSACTION_HEADER,TRANSACTION_LEVEL,
            TRANSACTION_AGENT,TRANSACTION_SESSION_ID
    );

    public static String DEFAULT_LOGGER_ACCESS="special-access-log";
    public static String DEFAULT_LOGGER_RAW="special-raw-log";

    private boolean enableRequestAccessLog = false;
    private boolean enableRequestRawLog = false;
    private boolean enableResponseAccessLog = false;
    private boolean enableResponseRawLog = false;

    public void setEnableRequestAccessLog(boolean enableRequestAccessLog) {
        this.enableRequestAccessLog = enableRequestAccessLog;
    }

    public void setEnableRequestRawLog(boolean enableRequestRawLog) {
        this.enableRequestRawLog = enableRequestRawLog;
    }

    public void setEnableResponseAccessLog(boolean enableResponseAccessLog) {
        this.enableResponseAccessLog = enableResponseAccessLog;
    }

    public void setEnableResponseRawLog(boolean enableResponseRawLog) {
        this.enableResponseRawLog = enableResponseRawLog;
    }

    public interface RequestModifier{
        void operation(CachingRequestWrapper requestWrapper);
    }
    public interface ResponseModifier{
        void operation(CachingResponseWrapper responseWrapper);
    }

    private Logger logger = logger();
    private Supplier<String> accessLogName = ()->DEFAULT_LOGGER_ACCESS;
    private Supplier<String> rawLogName = ()->DEFAULT_LOGGER_RAW;

    private List<AccessLogging> accessLoggingList = new ArrayList<>();
    private List<RawRequestLogging> rawRequestLoggingList = new ArrayList<>();
    private List<RawResponseLogging> rawResponseLoggings = new ArrayList<>();
    private List<AccessResponseLogging> accessResponseLoggings = new ArrayList<>();
    private RequestModifier requestModifier = req->{};
    private ResponseModifier responseModifier = res->{};
    private Supplier<String> appInfo = ()-> String.format(ManagementFactory.getRuntimeMXBean().getName());

    public void setRawResponseLoggings(List<RawResponseLogging> rawResponseLoggings) {
        this.rawResponseLoggings = rawResponseLoggings;
    }

    public void setAccessResponseLoggings(List<AccessResponseLogging> accessResponseLoggings) {
        this.accessResponseLoggings = accessResponseLoggings;
    }

    public void setAppInfo(Supplier<String> appInfo) {
        this.appInfo = appInfo;
    }

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

    private Logger firstFilterAccessLogger = LoggerFactory.getLogger(DEFAULT_LOGGER_ACCESS);
    private Logger firstFilterRawLogger = LoggerFactory.getLogger(DEFAULT_LOGGER_RAW);
    public void setAccessLogName(Supplier<String> accessLogName) {
        String name = accessLogName.get();
        if(name.equals(DEFAULT_LOGGER_ACCESS))
            return;
        if(name!=null && !name.equalsIgnoreCase(""))
            this.firstFilterAccessLogger = LoggerFactory.getLogger(name);
    }

    public void setRawRequestLog(Supplier<String> rawRequestLog) {
        String name = accessLogName.get();
        if(name.equals(DEFAULT_LOGGER_RAW))
            return;
        if(name!=null && !name.equalsIgnoreCase(""))
            this.firstFilterRawLogger = LoggerFactory.getLogger(name);
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

        MDC.put(APP_NAME,appInfo.get());

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
                    MDC.put(TRANSACTION_LEVEL, String.valueOf(Integer.parseInt(transactionLevel)+1));
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
            }
            Object transactionAgent = ((HttpServletRequest)servletRequest).getHeader(TRANSACTION_AGENT);
            if(transactionAgent!=null && transactionAgent instanceof String && !transactionAgent.equals("")){
                MDC.put(TRANSACTION_AGENT, (String) transactionAgent);
            }
            else {
                MDC.put(TRANSACTION_AGENT, (String) "Client");
            }
            Object transactionSession = ((HttpServletRequest)servletRequest).getHeader(TRANSACTION_SESSION_ID);
            if(transactionSession!=null && transactionSession instanceof String && !transactionSession.equals("")){
                MDC.put(TRANSACTION_SESSION_ID, (String) transactionSession);
            }
            else {
                MDC.put(TRANSACTION_SESSION_ID, (String) "");
            }

        }

        ServletResponse newResponse = servletResponse;
        if(newResponse instanceof HttpServletResponse){
            newResponse = new CachingResponseWrapper((HttpServletResponse) newResponse, rawResponseLoggings
                    ,accessResponseLoggings,this::getFirstFilterAccessLogger,this::getFirstFilterRawLogger
                    ,enableResponseAccessLog,enableResponseRawLog
            );
            if(responseModifier != null)
                responseModifier.operation((CachingResponseWrapper) newResponse);
        }

        if(enableRequestAccessLog && accessLoggingList.size()>0){
            ServletRequest finalNewRequest = newRequest;
            accessLoggingList.stream().forEach(x->x.log(getFirstFilterAccessLogger(), finalNewRequest));
        }
        if(enableRequestRawLog && rawRequestLoggingList.size()>0){
            ServletRequest finalNewRequest1 = newRequest;
            rawRequestLoggingList.stream().forEach(x->x.log(getFirstFilterRawLogger(), finalNewRequest1));
        }

        filterChain.doFilter(newRequest,newResponse);
    }
}
