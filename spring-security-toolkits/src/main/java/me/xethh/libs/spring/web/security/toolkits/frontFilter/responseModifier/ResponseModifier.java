package me.xethh.libs.spring.web.security.toolkits.frontFilter.responseModifier;

import me.xethh.libs.spring.web.security.toolkits.CachingResponseWrapper;

public interface ResponseModifier{
    void operation(CachingResponseWrapper responseWrapper);
}

