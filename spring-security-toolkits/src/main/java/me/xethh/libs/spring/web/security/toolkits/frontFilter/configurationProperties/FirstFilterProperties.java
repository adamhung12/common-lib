package me.xethh.libs.spring.web.security.toolkits.frontFilter.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "first-filter", ignoreInvalidFields = false)
public class FirstFilterProperties {
    private String appName;

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


    private Editing edit;
    private Log logging;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
