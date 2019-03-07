package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.RequestTemplate;
import org.slf4j.MDC;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.*;

public class DefaultMDCInterceptor extends MDCInterceptor{
    @Override
    public void apply(RequestTemplate template) {
        template.header(TRANSACTION_HEADER, MDC.get(TRANSACTION_HEADER));
        template.header(TRANSACTION_LEVEL, MDC.get(TRANSACTION_LEVEL));
        template.header(TRANSACTION_AGENT, MDC.get(TRANSACTION_AGENT));
    }
}
