package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.zuul.imports;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({DefaultRawResponseLoggingConfig.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableZuulDefaultRawResponseLogging {
}
