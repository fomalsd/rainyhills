package ltd.foma.rainyhills.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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

    @Autowired
    public void ConfigureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("user")
                .password("pass")
                .roles("USER");
    }
}
