package ltd.foma.rainyhills.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = getLogger( MethodHandles.lookup().lookupClass() );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/","/rainyhills/*")
            .permitAll()
            .anyRequest()
            .authenticated()

            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()

            .and()
            .logout()
            .permitAll()
            ;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.inMemoryAuthentication()
                .passwordEncoder(getPasswordEncoder())
                .withUser("user")
                .password(BCrypt.hashpw("pass",BCrypt.gensalt()))
                .roles("USER");
    }
}
