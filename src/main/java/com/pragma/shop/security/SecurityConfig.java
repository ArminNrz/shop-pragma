package com.pragma.shop.security;

import com.pragma.shop.constant.Constant;
import com.pragma.shop.exception.RestAccessDeniedHandler;
import com.pragma.shop.security.filter.AuthenticationFilter;
import com.pragma.shop.security.filter.AuthorizationFilter;
import com.pragma.shop.service.lowlevel.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final SecurityService securityService;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AuthenticationFilter customAuthenticationFilter = new AuthenticationFilter(authenticationManagerBean(), securityService);
        customAuthenticationFilter.setFilterProcessesUrl(Constant.BASE_URL + "/login");

        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                        .antMatchers(Constant.BASE_URL + "/login/**").permitAll()
                        .antMatchers(Constant.BASE_URL + Constant.VERSION + "/users").permitAll()
                        .antMatchers(Constant.BASE_URL + Constant.VERSION + "/products").permitAll()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests().anyRequest().authenticated()
                .and()
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new AuthorizationFilter(securityService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(restAccessDeniedHandler);
    }
}
