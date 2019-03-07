package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import me.xethh.libs.spring.web.security.toolkits.CachingRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;
import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequest;
import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequestWrapper;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.AppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.appNameProvider.NoneAppNameProvider;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties.FirstFilterProperties;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.RequestAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.RequestRawLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.ResponseAccessLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.ResponseRawLogging;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.requestModifier.RequestModifier;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.responseModifier.ResponseModifier;
import me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider.IdProvider;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @Autowired
    FirstFilterProperties properties;

    @Autowired
    IdProvider idProvider;

    public IdProvider getIdProvider() {
        return idProvider;
    }

    public void setIdProvider(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Value("${first-filter.webmvc.request-access-log.log-name}")
    private String requestAccessLogName;
    @Value("${first-filter.webmvc.request-access-log.enabled}")
    private boolean enableRequestAccessLog ;
    @Value("${first-filter.webmvc.request-raw-log.log-name}")
    private String requestRawLogName;
    @Value("${first-filter.webmvc.request-raw-log.enabled}")
    private boolean enableRequestRawLog ;

    @Value("${first-filter.webmvc.response-access-log.log-name}")
    private String responseAccessLogName;
    @Value("${first-filter.webmvc.response-access-log.enabled}")
    private boolean enableResponseAccessLog ;
    @Value("${first-filter.webmvc.response-raw-log.log-name}")
    private String responseRawLogName;
    @Value("${first-filter.webmvc.response-raw-log.enabled}")
    private boolean enableResponseRawLog ;


    @Value("${first-filter.webmvc.request-modification.enabled}")
    private boolean enableRequestModification;

    public String getResponseAccessLogName() {
        return responseAccessLogName;
    }

    public void setResponseAccessLogName(String responseAccessLogName) {
        this.responseAccessLogName = responseAccessLogName;
    }

    public boolean isEnableResponseAccessLog() {
        return enableResponseAccessLog;
    }

    public void setEnableResponseAccessLog(boolean enableResponseAccessLog) {
        this.enableResponseAccessLog = enableResponseAccessLog;
    }

    public String getResponseRawLogName() {
        return responseRawLogName;
    }

    public void setResponseRawLogName(String responseRawLogName) {
        this.responseRawLogName = responseRawLogName;
    }

    public boolean isEnableResponseRawLog() {
        return enableResponseRawLog;
    }

    public void setEnableResponseRawLog(boolean enableResponseRawLog) {
        this.enableResponseRawLog = enableResponseRawLog;
    }

    public boolean isEnableRequestModification() {
        return enableRequestModification;
    }

    public void setEnableRequestModification(boolean enableRequestModification) {
        this.enableRequestModification = enableRequestModification;
    }

    public String getRequestAccessLogName() {
        return requestAccessLogName;
    }

    public void setRequestAccessLogName(String requestAccessLogName) {
        this.requestAccessLogName = requestAccessLogName;
    }

    public boolean isEnableRequestAccessLog() {
        return enableRequestAccessLog;
    }

    public String getRequestRawLogName() {
        return requestRawLogName;
    }

    public void setRequestRawLogName(String requestRawLogName) {
        this.requestRawLogName = requestRawLogName;
    }

    public boolean isEnableRequestRawLog() {
        return enableRequestRawLog;
    }

    public void setEnableRequestAccessLog(boolean enableRequestAccessLog) {
        this.enableRequestAccessLog = enableRequestAccessLog;
    }

    public void setEnableRequestRawLog(boolean enableRequestRawLog) {
        this.enableRequestRawLog = enableRequestRawLog;
    }



    private Logger logger = logger();
    private Supplier<String> accessLogName = ()->DEFAULT_LOGGER_ACCESS;
    private Supplier<String> rawLogName = ()->DEFAULT_LOGGER_RAW;

    @Autowired
    private List<RequestAccessLogging> accessRequestLoggingList = new ArrayList<>();
    @Autowired
    private List<RequestRawLogging> requestRawLoggingList = new ArrayList<>();
    @Autowired
    private List<ResponseRawLogging> responseRawLoggings = new ArrayList<>();
    @Autowired
    private List<ResponseAccessLogging> responseAccessLoggings = new ArrayList<>();

    @Autowired
    private List<RequestModifier> requestModifier = new ArrayList<>();
    @Autowired
    private List<ResponseModifier> responseModifiers = new ArrayList<>();

    @Autowired
    private AppNameProvider appNameProvider = new NoneAppNameProvider();

    public List<ResponseModifier> getResponseModifiers() {
        return responseModifiers;
    }

    public void setResponseModifiers(List<ResponseModifier> responseModifiers) {
        this.responseModifiers = responseModifiers;
    }

    public AppNameProvider getAppNameProvider() {
        return appNameProvider;
    }

    public void setAppNameProvider(AppNameProvider appNameProvider) {
        this.appNameProvider = appNameProvider;
    }


    public void setResponseRawLoggings(List<ResponseRawLogging> responseRawLoggings) {
        this.responseRawLoggings = responseRawLoggings;
    }

    public void setResponseAccessLoggings(List<ResponseAccessLogging> responseAccessLoggings) {
        this.responseAccessLoggings = responseAccessLoggings;
    }

    public List<RequestModifier> getRequestModifier() {
        return requestModifier;
    }

    public void setRequestModifier(List<RequestModifier> requestModifier) {
        this.requestModifier = requestModifier;
    }

    public void setAccessRequestLoggingList(List<RequestAccessLogging> accessRequestLoggingList) {
        this.accessRequestLoggingList = accessRequestLoggingList;
    }

    public void setRequestRawLoggingList(List<RequestRawLogging> requestRawLoggingList) {
        this.requestRawLoggingList = requestRawLoggingList;
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

    @PostConstruct
    public void init(){
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(logger.isDebugEnabled())
            logger.debug("Start first filter");

        MDC.clear();

        MDC.put(APP_NAME,appNameProvider.gen());

        ServletRequest newRequest = servletRequest;

        if(newRequest instanceof HttpServletRequest){
            //If raw log request, the request should cached
            if(enableRequestRawLog)
                newRequest = new CachingRequestWrapper((HttpServletRequest) newRequest);
            // If request modification enabled, the request should be mutable
            else if(enableRequestModification){
                newRequest = new MutableHttpRequestWrapper((HttpServletRequest) newRequest);
            }

            //Modified the request based on initialized beans of request modifiers

            if(enableRequestModification && requestModifier !=null && requestModifier.size()>0){
                ServletRequest tempRequest = newRequest;
                requestModifier.forEach(m->m.operation((MutableHttpRequest) tempRequest));
            }

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
                MDC.put(TRANSACTION_HEADER, idProvider.gen());
                MDC.put(TRANSACTION_LEVEL, 0 + "");
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
            newResponse = new CachingResponseWrapper((HttpServletResponse) newResponse, enableResponseRawLog, responseRawLoggings
                    , responseAccessLoggings ,enableResponseAccessLog
            );
            if(responseModifiers != null && responseModifiers.size()>0){
                ServletResponse tempResponse = newResponse;
                responseModifiers.forEach(m->m.operation((CachingResponseWrapper) tempResponse));
            }
        }

        //Start logging of access request
        if(enableRequestAccessLog && accessRequestLoggingList.size()>0){
            ServletRequest finalNewRequest = newRequest;
            accessRequestLoggingList.forEach(x->x.log(finalNewRequest));
        }
        //Start logging of raw request
        if(enableRequestRawLog && requestRawLoggingList.size()>0){
            ServletRequest finalNewRequest1 = newRequest;
            requestRawLoggingList.forEach(x->x.log(finalNewRequest1));
        }

        filterChain.doFilter(newRequest,newResponse);
    }
}
