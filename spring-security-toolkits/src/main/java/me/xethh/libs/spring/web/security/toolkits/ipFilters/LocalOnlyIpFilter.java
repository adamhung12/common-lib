package me.xethh.libs.spring.web.security.toolkits.ipFilters;

import me.xethh.libs.spring.web.security.toolkits.exceptionModels.StatusBasesGeneralSSTExceptionModelFactory;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.generalThrowables.GeneralThrowable;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@EnableConfigurationProperties({IPFilterConfiguration.class})
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class LocalOnlyIpFilter extends GenericFilterBean implements WithLogger {

    @Value("${ip-filters.enable-local-filter}")
    private boolean enableLocalIpFilter;

    private static String local = "127.0.0.1";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(enableLocalIpFilter){
            if(local.equals(request.getRemoteAddr())){
                chain.doFilter(request,response);
            }
            else throw new GeneralThrowable(new StatusBasesGeneralSSTExceptionModelFactory.UnAuthorize("Only accept local IP"));
        }
        else chain.doFilter(request,response);
    }
}
