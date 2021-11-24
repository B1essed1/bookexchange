package com.example.bookexchange.data.config;

import com.example.bookexchange.data.security.jwt.JwtConfigurer;
import com.example.bookexchange.data.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Security configuration class for Spring Security application.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN_ENDPOINT = "/api/v.1/admin/**";
    private static final String REGISTER_ENDPOINT = "/api/v.1/register/**";
    private static final String LOGIN_ENDPOINT = "/api/v.1/login/**";
  //  private static final String USER_ENDPOINT = "/api/v.1/user/**";

    private final JwtTokenProvider jwtTokenProvider;

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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/v.1/user/create",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          //      .antMatchers(USER_ENDPOINT).hasRole("USER")
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers("/v2/api-docs", "/configuration/**", "/swagger-ui/**", "/webjars/**")
                .permitAll()
                .antMatchers(REGISTER_ENDPOINT).permitAll()
                .antMatchers(ADMIN_ENDPOINT).hasAnyRole("ADMIN","USER")
                .anyRequest().authenticated()

                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}