package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;



@ConfigurationProperties(prefix = "cust-web-security", ignoreInvalidFields = true)
public class CustomizedWebSecurityConfigProperties {
    CustomizedWebSecurityConfigProperties.ProviderType providerType;

    public enum MatchType{PERMIT_ALL,HAS_ROLE,IS_IP,AUTHENTICATED,ANONYMOUS}
    public enum ProviderType{MEMORY, CLASS}
    private List<Match> matches = new ArrayList<>();
    private List<Provider> providers = new ArrayList<>();


    public static class Provider{
        private ProviderType providerType;

        public ProviderType getProviderType() {
            return providerType;
        }

        public void setProviderType(ProviderType providerType) {
            this.providerType = providerType;
        }

        private List<AuthPair> authens;

        public List<AuthPair> getAuthens() {
            return authens;
        }

        public void setAuthens(List<AuthPair> authens) {
            this.authens = authens;
        }

        private String className;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
    public static class AuthPair{
        private String username;
        private String password;
        private List<String> roles = new ArrayList<>();

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
    public static class Match{
        private String url;
        private MatchType type;
        private String value;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public MatchType getType() {
            return type;
        }

        public void setType(MatchType type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }
}
