package me.xethh.libs.spring.web.security.toolkits.ipFilters;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ip-filters")
public class IPFilterConfiguration {
    private boolean enableLocalFilter;
    private boolean enableInternalFilter;

    public boolean isEnableLocalFilter() {
        return enableLocalFilter;
    }

    public void setEnableLocalFilter(boolean enableLocalFilter) {
        this.enableLocalFilter = enableLocalFilter;
    }

    public boolean isEnableInternalFilter() {
        return enableInternalFilter;
    }

    public void setEnableInternalFilter(boolean enableInternalFilter) {
        this.enableInternalFilter = enableInternalFilter;
    }
}
