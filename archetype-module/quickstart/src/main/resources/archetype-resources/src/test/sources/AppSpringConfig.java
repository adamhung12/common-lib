package $package;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

/**
 * Base configuration for spring
 */
@Configuration
@ComponentScan("$package")
public class AppSpringConfig
{
}
