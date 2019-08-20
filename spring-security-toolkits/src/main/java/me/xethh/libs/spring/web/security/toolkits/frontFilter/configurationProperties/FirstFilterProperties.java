package me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "first-filter", ignoreInvalidFields = true)
public class FirstFilterProperties {

    private String serviceId;
    private Editing edit;
    private TransactionIdConfig transactionId;
    private AppNameConfig appNameProvider;
    private WebMvcConfig webmvc;
    private ZuulConfig zuul;

    public static class WebMvcConfig{
        private Log requestAccessLog;
        private Log requestRawLog;
        private Log responseAccessLog;
        private Log responseRawLog;
        private RequestResponseModification requestModification;

        public Log getRequestAccessLog() {
            return requestAccessLog;
        }

        public void setRequestAccessLog(Log requestAccessLog) {
            this.requestAccessLog = requestAccessLog;
        }

        public Log getRequestRawLog() {
            return requestRawLog;
        }

        public void setRequestRawLog(Log requestRawLog) {
            this.requestRawLog = requestRawLog;
        }


        public RequestResponseModification getRequestModification() {
            return requestModification;
        }

        public void setRequestModification(RequestResponseModification requestModification) {
            this.requestModification = requestModification;
        }

        public Log getResponseAccessLog() {
            return responseAccessLog;
        }

        public void setResponseAccessLog(Log responseAccessLog) {
            this.responseAccessLog = responseAccessLog;
        }

        public Log getResponseRawLog() {
            return responseRawLog;
        }

        public void setResponseRawLog(Log responseRawLog) {
            this.responseRawLog = responseRawLog;
        }

    }
    public static class ZuulConfig{
        private Log requestAccessLog;
        private Log requestRawLog;
        private Log responseAccessLog;
        private Log responseRawLog;
        private RouteWithAuthen withAuthen;

        public RouteWithAuthen getWithAuthen() {
            return withAuthen;
        }

        public void setWithAuthen(RouteWithAuthen withAuthen) {
            this.withAuthen = withAuthen;
        }

        public Log getRequestAccessLog() {
            return requestAccessLog;
        }

        public void setRequestAccessLog(Log requestAccessLog) {
            this.requestAccessLog = requestAccessLog;
        }

        public Log getRequestRawLog() {
            return requestRawLog;
        }

        public void setRequestRawLog(Log requestRawLog) {
            this.requestRawLog = requestRawLog;
        }

        public Log getResponseAccessLog() {
            return responseAccessLog;
        }

        public void setResponseAccessLog(Log responseAccessLog) {
            this.responseAccessLog = responseAccessLog;
        }

        public Log getResponseRawLog() {
            return responseRawLog;
        }

        public void setResponseRawLog(Log responseRawLog) {
            this.responseRawLog = responseRawLog;
        }

    }
    public static class AppNameConfig{
        public static AppNameConfig getDefault(){
            AppNameConfig conf = new AppNameConfig();
            conf.type = BuildType.Default;
            return conf;
        }
        public enum BuildType{
           Default, None, Custom
        }

        private BuildType type;

        public BuildType getType() {
            return type;
        }

        public void setType(BuildType type) {
            this.type = type;
        }
    }
    public static class TransactionIdConfig{
        public enum BuildType{
            Time_Base, Machine_Time_Based, Custom
        }
        private String staticName;
        private BuildType type;

        public BuildType getType() {
            return type;
        }

        public void setType(BuildType type) {
            this.type = type;
        }

        public String getStaticName() {
            return staticName;
        }

        public void setStaticName(String staticName) {
            this.staticName = staticName;
        }
    }
    public static class Editing{
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    public static class RequestResponseModification{
        private boolean enabled;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
    public static enum AuthenType{
        None, Basic, Bear
    }
    public static class RouteWithAuthen{
        private AuthenType authenType;
        private String value;

        public AuthenType getAuthenType() {
            return authenType;
        }

        public void setAuthenType(AuthenType authenType) {
            this.authenType = authenType;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    public static class Log{
        private boolean enabled;
        private String logName;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getLogName() {
            return logName;
        }

        public void setLogName(String logName) {
            this.logName = logName;
        }
    }

    public TransactionIdConfig getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionIdConfig transactionId) {
        this.transactionId = transactionId;
    }

    public Editing getEdit() {
        return edit;
    }

    public void setEdit(Editing edit) {
        this.edit = edit;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public AppNameConfig getAppNameProvider() {
        return appNameProvider;
    }

    public void setAppNameProvider(AppNameConfig appNameProvider) {
        this.appNameProvider = appNameProvider;
    }
    public ZuulConfig getZuul() {
        return zuul;
    }

    public void setZuul(ZuulConfig zuul) {
        this.zuul = zuul;
    }

    public WebMvcConfig getWebmvc() {
        return webmvc;
    }

    public void setWebmvc(WebMvcConfig webmvc) {
        this.webmvc = webmvc;
    }
}
