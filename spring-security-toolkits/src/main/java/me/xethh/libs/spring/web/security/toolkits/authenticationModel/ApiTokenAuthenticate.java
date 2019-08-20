package me.xethh.libs.spring.web.security.toolkits.authenticationModel;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiTokenAuthenticate extends UsernamePasswordAuthenticationToken {
    public static class ApiTokenAuthority implements GrantedAuthority {

        public static ApiTokenAuthority of(String authority){
            return new ApiTokenAuthority(authority);
        }
        ApiTokenAuthority(String authority){
            this.authority = authority;
        }
        String authority;

        @Override
        public String getAuthority() {
            return authority;
        }
    }

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param username username
     * @param token token
     * @param authorities the collection of GrantedAuthority for the principal
     */
    public ApiTokenAuthenticate(String username, String token, Collection<? extends GrantedAuthority> authorities) {
        super(username,token,authorities);
        this.username = username;
        this.token = token;
    }

    private String username,token;

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}

