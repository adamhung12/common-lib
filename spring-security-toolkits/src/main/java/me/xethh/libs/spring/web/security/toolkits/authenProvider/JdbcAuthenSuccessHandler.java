package me.xethh.libs.spring.web.security.toolkits.authenProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.xethh.libs.spring.web.security.toolkits.authenProvider.entity.AuthenOperation;
import me.xethh.libs.toolkits.logging.WithLogger;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.session.FindByIndexNameSessionRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
            Map session = findByIndexNameSessionRepository.findByPrincipalName((String) authen.getCredentials());
            MDC.put("clientId", (String) session.get("clientId"));

            String user = authen.getName();
            logger().info(String.format("Start generating token for user[%s]", user));
            AuthenOperation.AuthenRequest authenRequest = new AuthenOperation.AuthenRequest();
            // StaticService1.tokenFactory.saveTokenValue((String) authen.getCredentials(), Tuple2.of(user, DateFactory.now().addMins(10).asDate()));
            ObjectMapper mapper = new ObjectMapper();
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
