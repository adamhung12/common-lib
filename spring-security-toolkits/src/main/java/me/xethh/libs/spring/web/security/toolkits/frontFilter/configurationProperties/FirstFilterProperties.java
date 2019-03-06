package me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "first-filter", ignoreInvalidFields = true)
public class FirstFilterProperties {

    private String serviceId;
    private Editing edit;
    private Log logging;
    private TransactionIdConfig transactionId;
    private AppNameConfig appNameProvider;

    public static class AppNameConfig{
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
    public static class Log{
        private String accessLogName;
        private String rawLogName;

        public String getAccessLogName() {
            return accessLogName;
        }

        public void setAccessLogName(String accessLogName) {
            this.accessLogName = accessLogName;
        }

        public String getRawLogName() {
            return rawLogName;
        }

        public void setRawLogName(String rawLogName) {
            this.rawLogName = rawLogName;
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

    public Log getLogging() {
        return logging;
    }

    public void setLogging(Log logging) {
        this.logging = logging;
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
}
