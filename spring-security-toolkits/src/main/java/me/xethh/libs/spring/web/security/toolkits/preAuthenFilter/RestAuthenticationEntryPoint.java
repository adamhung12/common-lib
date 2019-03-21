package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModelImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {
    private ExceptionSetter exceptionSetter;
    public RestAuthenticationEntryPoint(ExceptionSetter exceptionSetter) {
        this.exceptionSetter = exceptionSetter;
    }

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        exceptionSetter.setException(response, new GeneralExceptionModelImpl.AuthorizationFail());
    }
}

