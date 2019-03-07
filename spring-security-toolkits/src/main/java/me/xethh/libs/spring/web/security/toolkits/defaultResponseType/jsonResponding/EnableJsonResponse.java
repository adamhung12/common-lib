package me.xethh.libs.spring.web.security.toolkits.defaultResponseType.jsonResponding;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Import({EnableJsonResponse.Config.class})
public @interface EnableJsonResponse{
    public static class Config implements WebMvcConfigurer {
        @Override
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            configurer.defaultContentType(MediaType.APPLICATION_JSON_UTF8);
        }
    }
}
