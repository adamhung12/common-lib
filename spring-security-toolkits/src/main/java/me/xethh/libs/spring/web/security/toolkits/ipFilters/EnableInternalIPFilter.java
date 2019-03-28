package me.xethh.libs.spring.web.security.toolkits.ipFilters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({EnableInternalIPFilter.Config.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableInternalIPFilter {
    public static class Config {
        @Bean
        public InternalOnlyIpFilter internalOnlyIpFilter() {
            return new InternalOnlyIpFilter();
        }
    }
}
