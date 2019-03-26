package me.xethh.libs.spring.web.security.toolkits.exceptionModels.imports;

import me.xethh.libs.spring.web.security.toolkits.exceptionModels.StatusBasesGeneralSSTExceptionModelFactory;
import org.springframework.context.annotation.Bean;

public class Config2 {

    @Bean
    public StatusBasesGeneralSSTExceptionModelFactory statusBasesGeneralSSTExceptionModelFactory(){
        return new StatusBasesGeneralSSTExceptionModelFactory();
    }
}
