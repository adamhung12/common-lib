package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.imports;

import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.CustomizedWebSecurityConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({CustomizedWebSecurityConfig.class,CustomizedWebSecurityConfig.Config.class})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCustomSecurityConfig {
}
