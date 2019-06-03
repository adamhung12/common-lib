package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

public class AlwaysTrueAuthenticationProvider implements AuthenticationProvider {
    public static class SysRoutingAuthority implements GrantedAuthority {
        public static SysRoutingAuthority of(String authority){
            return new SysRoutingAuthority(authority);
        }
        private String authority;

        public SysRoutingAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), Arrays.asList(new SysRoutingAuthority("SysAnonymous")));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
