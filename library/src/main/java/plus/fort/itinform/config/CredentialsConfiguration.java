package plus.fort.itinform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import plus.fort.itinform.service.InMemoryUserDetailsService;
import plus.fort.itinform.service.UserDetailsService;

@Configuration
public class CredentialsConfiguration {

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsService();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

}
