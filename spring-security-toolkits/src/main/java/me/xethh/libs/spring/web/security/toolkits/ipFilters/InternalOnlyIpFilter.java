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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableConfigurationProperties({IPFilterConfiguration.class})
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class InternalOnlyIpFilter extends GenericFilterBean implements WithLogger {

    @Value("${ip-filters.enable-internal-filter}")
    private boolean enableLocalIpFilter;

    private long toIpInt(String[] ip){
        return toIpInt(Arrays.stream(ip).map(x->Integer.parseInt(x)).collect(Collectors.toList()));
    }
    private long toIpInt(List<Integer> ip){
        long ipNumbers = 0;
        for (int i = 0; i < 4; i++) {
            ipNumbers += ip.get(i) << (24 - (8 * i));
        }
        return ipNumbers;
    }
    private static String local = "127.0.0.1";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(enableLocalIpFilter){
            long address = toIpInt(request.getRemoteAddr().split("\\."));
            boolean internal = (((address >>> 24) & 0xFF) == 10)
                    || ((((address >>> 24) & 0xFF) == 172)
                    && (((address >>> 16) & 0xF0) == 16))
                    || ((((address >>> 24) & 0xFF) == 192)
                    && (((address >>> 16) & 0xFF) == 168));
            if(internal || local.equals(request.getRemoteAddr())){
                chain.doFilter(request,response);
            }
            else {
                throw new GeneralThrowable(new StatusBasesGeneralSSTExceptionModelFactory.UnAuthorize("Only accept internal IP"));
            }
        }
        else
            chain.doFilter(request,response);
    }
}
