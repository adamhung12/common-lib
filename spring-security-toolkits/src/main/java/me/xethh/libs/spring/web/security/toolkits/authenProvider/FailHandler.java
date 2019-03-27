package me.xethh.libs.spring.web.security.toolkits.authenProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.GeneralThrowable;
import me.xethh.libs.spring.web.security.toolkits.exceptionModels.StatusBasesGeneralSSTExceptionModelFactory;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.ExceptionSetter;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModel;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.exceptionModel.GeneralExceptionModelImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FailHandler extends SimpleUrlAuthenticationFailureHandler {
    ObjectMapper mapper = new ObjectMapper();
    private ExceptionSetter exceptionSetter;
    public FailHandler(ExceptionSetter exceptionSetter){
        this.exceptionSetter = exceptionSetter;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // byte[] bytss = mapper.writeValueAsBytes(new StatusBasesGeneralSSTExceptionModelFactory.AuthorizationFail());
        // response.setStatus(HttpStatus.FORBIDDEN.value());
        // response.setContentLength(bytss.length);
        // response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        // response.getOutputStream().write(bytss);
        // exceptionSetter.setException(response, new GeneralExceptionModelImpl.AuthorizationFail());
        throw new GeneralThrowable((GeneralExceptionModel) new StatusBasesGeneralSSTExceptionModelFactory.AuthorizationFail());
        // return;
    }
}

