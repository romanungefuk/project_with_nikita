package com.equp.back.backend.config;

import com.equp.back.backend.security.jwt.JwtConfigurer;
import com.equp.back.backend.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth";
    private static final String SIGNUP_ENDPOINT = "/api/v1/user/signup";
    private static final String UPDATEBYEMAIL_ENDPOINT = "/api/v1/user/update-by-mail";
    private static final String PASSWORDCHANGE_ENDPOINT = "/password_change**";
    private static final String USER_ENDPOINT = "/api/v1/user**";
    private static final String MOOD_ENDPOINT = "/api/v1/mood**";
    private static final String CHECK_ENDPOINT = "/api/v1/auth/**";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(SIGNUP_ENDPOINT).permitAll()
                .antMatchers(UPDATEBYEMAIL_ENDPOINT).permitAll()
                .antMatchers(PASSWORDCHANGE_ENDPOINT).permitAll()
                .antMatchers(CHECK_ENDPOINT).permitAll()
                //.antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
                .antMatchers(USER_ENDPOINT).hasRole("USER")
                .antMatchers(MOOD_ENDPOINT).hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
