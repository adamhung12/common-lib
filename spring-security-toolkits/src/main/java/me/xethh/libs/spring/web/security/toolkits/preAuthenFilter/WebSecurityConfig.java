package me.xethh.libs.spring.web.security.toolkits.preAuthenFilter;

import com.citictel.cpaas.security.RestAuthenticationEntryPoint;
import com.citictel.cpaas.security.handler.FailHandler;
import me.xethh.libs.spring.web.security.toolkits.defaultResponseType.jsonResponding.EnableJsonResponse;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.PreTokenFilter;
import me.xethh.libs.spring.web.security.toolkits.preAuthenFilter.handler.FailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

/**
 * Created by Xeth on 4/11/17.
 */
@EnableWebMvc
@EnableWebSecurity
@EnableJsonResponse
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    PreTokenFilter preTokenFilter;
    @Autowired
    FailHandler failHandler;

    @Autowired
    List<AuthenticationProvider> authenProvider;

    @Autowired
    RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/cpaas/msg/**").permitAll()
                .antMatchers("/cpaas/api/**").hasRole("api_user")
                .and()
                .addFilterAfter(preTokenFilter, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().httpBasic()
                .and()
                .formLogin()
                .loginPage("/cpaas/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(failHandler)
                .and()
                .logout()
        ;
    }
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        authenProvider.forEach(x->{
            auth.authenticationProvider(x);
        });
    }
}

