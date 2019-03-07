package me.xethh.libs.spring.web.security.toolkits.frontFilter.logging.springWeb.impl.imports;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({DefaultRawRequestLoggingConfig.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableSpringDefaultRawRequestLogging {
}
