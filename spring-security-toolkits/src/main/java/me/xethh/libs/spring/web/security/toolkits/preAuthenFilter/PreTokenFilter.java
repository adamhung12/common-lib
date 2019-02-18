package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import me.xethh.libs.spring.web.security.toolkits.authenticationModel.ApiTokenAuthenticate;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModelImpl;
import me.xethh.utils.dateManipulation.DateFactory;
import me.xethh.utils.wrapper.Tuple2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class PreTokenFilter extends OncePerRequestFilter {
    ExceptionSetter advice;
    public PreTokenFilter(ExceptionSetter advice){
        this.advice = advice;
    }

    TokenInfoGetter tokenInfoGetter = token -> null;

    public void setTokenInfoGetter(TokenInfoGetter tokenInfoGetter) {
        this.tokenInfoGetter = tokenInfoGetter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(header!=null && header.length()>7 && header.substring(0,7).toLowerCase().equals("bearer ")){
            String token = header.substring(7);
            Tuple2<String, Date> tokenValue = tokenInfoGetter.getTokenInfo(token);

            if(tokenValue==null){
                advice.setException(httpServletResponse, new GeneralExceptionModelImpl.TokenNotValid());
                return;
            }
            if(tokenValue.getV1()!=null && tokenValue.getV2()!=null && DateFactory.now().beforeEqual(tokenValue.getV2())){
                SecurityContextHolder.getContext().setAuthentication(new ApiTokenAuthenticate(tokenValue.getV1(),token,new ArrayList<>()));
            }
            else{
                advice.setException(httpServletResponse, new GeneralExceptionModelImpl.TokenNotValid());
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}

