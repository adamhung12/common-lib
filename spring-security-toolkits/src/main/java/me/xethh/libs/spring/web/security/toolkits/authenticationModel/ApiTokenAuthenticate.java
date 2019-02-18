package me.xethh.libs.spring.web.security.toolkits.authenticationModel;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiTokenAuthenticate extends UsernamePasswordAuthenticationToken {

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     * represented by this authentication object.
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

