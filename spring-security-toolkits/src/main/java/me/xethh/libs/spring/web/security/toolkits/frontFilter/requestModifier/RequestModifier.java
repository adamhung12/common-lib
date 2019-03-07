package me.xethh.libs.spring.web.security.toolkits.frontFilter.requestModifier;

import me.xethh.libs.spring.web.security.toolkits.MutableHttpRequest;

public interface RequestModifier{
    void operation(MutableHttpRequest requestWrapper);
}

