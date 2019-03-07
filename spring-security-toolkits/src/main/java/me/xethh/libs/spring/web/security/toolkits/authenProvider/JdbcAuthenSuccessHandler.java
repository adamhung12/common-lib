package me.xethh.libs.spring.web.security.toolkits.authenProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_CLIENT_ID;
import static me.xethh.libs.spring.web.security.toolkits.frontFilter.FirstFilter.TRANSACTION_SESSION_ID;
import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

public class JdbcAuthenSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements WithLogger {

    @Autowired
    FindByIndexNameSessionRepository findByIndexNameSessionRepository;
    private RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest
                = requestCache.getRequest(request, response);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authen = context.getAuthentication();

        if(authen!=null && authen instanceof JdbcAuthenProvider.JdbcAuthentication && authen.isAuthenticated()){
            Session session = findByIndexNameSessionRepository.findById((String) authen.getCredentials());
            session.setAttribute(TRANSACTION_CLIENT_ID, authen.getName());
            findByIndexNameSessionRepository.save(session);
            MDC.put(TRANSACTION_SESSION_ID,session.getId());
            MDC.put(TRANSACTION_CLIENT_ID,authen.getName());


            ObjectMapper mapper = new ObjectMapper();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getOutputStream().write(mapper.writeValueAsBytes(new TokenResponse((String) authen.getCredentials())));
            response.flushBuffer();
            clearAuthenticationAttributes(request);
        }
        return;
    }

    public static class TokenResponse {
        public TokenResponse(){
        }
        public TokenResponse(String token){
            this.token = token;
        }
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
