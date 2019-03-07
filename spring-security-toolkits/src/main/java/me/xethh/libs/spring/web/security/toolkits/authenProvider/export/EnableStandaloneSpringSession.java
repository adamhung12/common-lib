package me.xethh.libs.spring.web.security.toolkits.authenProvider.export;

import com.mysql.cj.jdbc.MysqlDataSource;
import me.xethh.libs.spring.web.security.toolkits.authenProvider.JdbcAuthenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EnableStandaloneSpringSession.Config.class)
public @interface EnableStandaloneSpringSession {
    public static class Config{

        @Bean
        public FindByIndexNameSessionRepository<? extends Session> findByIndexNameSessionRepository(
                @Value("${me.xethh.utils.authen.default.jdbc.provider.username}") String username,
                @Value("${me.xethh.utils.authen.default.jdbc.provider.password}") String password,
                @Value("${me.xethh.utils.authen.default.jdbc.provider.url}") String url,
                @Value("${me.xethh.utils.authen.default.jdbc.session.defaultMaxInactiveInterval}") int inactiveInterval
        ){
            MysqlDataSource mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(url);
            mysqlDS.setUser(username);
            mysqlDS.setPassword(password);

            JdbcOperationsSessionRepository repo = new JdbcOperationsSessionRepository(new JdbcTemplate(mysqlDS));
            repo.setDefaultMaxInactiveInterval(inactiveInterval);
            return repo;
        }
    }
}
