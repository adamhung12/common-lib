package me.xethh.libs.spring.web.security.toolkits.authenProvider;

import me.xethh.libs.toolkits.logging.WithLogger;
import me.xethh.utils.authUtils.authentication.impl.common.PureAuthUser;
import me.xethh.utils.authUtils.spring.SpringAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import java.util.*;

import static org.springframework.session.FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME;

public class JdbcAuthenProvider
        implements AuthenticationProvider, WithLogger {
    public static class JdbcAuthority implements GrantedAuthority {
        public static JdbcAuthority of(String authority){
            return new JdbcAuthority(authority);
        }
        private String authority;

        public JdbcAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
    public static class JdbcAuthentication extends AbstractAuthenticationToken {

        private String username, password;
        /**
         * Creates a token with the supplied array of authorities.
         *
         * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
         * represented by this authentication object.
         */
        public JdbcAuthentication(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(authorities);
            this.username = username;
            this.password = password;
            setAuthenticated(true);
        }

        @Override
        public Object getCredentials() {
            return password;
        }

        @Override
        public Object getPrincipal() {
            return username;
        }

        @Override
        public void eraseCredentials() {
        }
    }

    @Autowired
    SpringAuthenticator authenticator;

    @Autowired
    FindByIndexNameSessionRepository findByIndexNameSessionRepository;

    public String newToken(String username){
        int trail = 10;
        while(trail>0){
            Session newSession = findByIndexNameSessionRepository.createSession();
            if(newSession!=null && findByIndexNameSessionRepository.findById(newSession.getId())==null){
                newSession.setAttribute(PRINCIPAL_NAME_INDEX_NAME,username);
                findByIndexNameSessionRepository.save(newSession);
                return newSession.getId();
            }
            else {
                trail--;
                continue;
            }
        }
        throw new RuntimeException("Fail on generating token");
    }

    @Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        PureAuthUser user = new PureAuthUser(name, password);
        if(authenticator.isValid(user)){
            String token = newToken(name);
            return new JdbcAuthentication(name, token, Arrays.asList(JdbcAuthority.of("ROLE_login_only")));
        }
        else{
            logger().error("Fail login, unexpected return type");
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
