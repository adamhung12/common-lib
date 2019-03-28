package me.xethh.libs.spring.web.security.toolkits.ipFilters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({EnableLocalIPFilter.Config.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableLocalIPFilter {
    public static class Config{
        @Bean
        public LocalOnlyIpFilter localOnlyIpFilter(){
            return new LocalOnlyIpFilter();
        }

    }
}
