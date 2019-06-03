package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.configuration.CustomizedWebSecurityConfigProperties;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.handler.FailHandler;
import me.xethh.utils.authUtils.authentication.Encoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * Created by Xeth on 4/11/17.
 */
@EnableConfigurationProperties(CustomizedWebSecurityConfigProperties.class)
public class CustomizedWebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Configuration
    public static class Config{
        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder(){
            return Encoder.newBCryptEncoder();
        }
        @Bean
        public FailHandler failHandler(
                @Autowired ExceptionSetter exceptionSetter
        ){
            return new FailHandler(exceptionSetter);
        }
        @Bean
        public RestAuthenticationEntryPoint restAuthenticationEntryPoint(
                @Autowired ExceptionSetter exceptionSetter
        ){
            return new RestAuthenticationEntryPoint(exceptionSetter);
        }
    }
    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomizedWebSecurityConfigProperties properties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .httpBasic()
        ;
        for(CustomizedWebSecurityConfigProperties.Match match:properties.getMatches()){
            CustomizedWebSecurityConfigProperties.MatchType type = match.getType();
            String url = match.getUrl();
            String value = match.getValue();
            ExpressionUrlAuthorizationConfigurer.AuthorizedUrl matcher = http.authorizeRequests().antMatchers(url);
            switch (type){
                case HAS_ROLE:
                    matcher.hasRole(value);
                    break;
                case PERMIT_ALL:
                    matcher.permitAll();
                    break;
                case ANONYMOUS:
                    matcher.anonymous();
                    break;
                case AUTHENTICATED:
                    matcher.authenticated();
                    break;
                case IS_IP:
                    throw new RuntimeException("IP filter not support yet");
            }
        }
    }
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        for(CustomizedWebSecurityConfigProperties.Provider provider : properties.getProviders()){
            switch (provider.getProviderType()){
                case MEMORY:
                    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authProvider = auth.inMemoryAuthentication();
                    List<CustomizedWebSecurityConfigProperties.AuthPair> authens = provider.getAuthens();
                    for(CustomizedWebSecurityConfigProperties.AuthPair authen: authens){
                        authProvider.withUser(authen.getUsername()).password(bCryptPasswordEncoder.encode(authen.getPassword())).roles(authen.getRoles().toArray(new String[authen.getRoles().size()]));
                    }
                    break;
                case CLASS:
                    if(StringUtils.isNotBlank(provider.getClassName())){
                        switch (provider.getClassName()){
                            case "me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.AlwaysTrueAuthenticationProvider":
                                auth.authenticationProvider(new AlwaysTrueAuthenticationProvider());
                                break;
                            default:
                                throw new RuntimeException(String.format("Provider type[%s] not supported", provider.getProviderType()));
                        }
                    }

            }
        }
    }

}

