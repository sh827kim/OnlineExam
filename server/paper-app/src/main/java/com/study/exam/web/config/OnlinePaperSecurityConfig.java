package com.study.exam.web.config;

import com.study.exam.user.domain.Authority;
import com.study.exam.user.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class OnlinePaperSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSecurityService userSecurityService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }

    private RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(
                "paper-site-remember-me",
                userSecurityService
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final var loginFilter = new CustomLoginFilter(
                authenticationManagerBean(),
                rememberMeServices()
        );

        http
                .csrf().disable()
                .formLogin(login ->
                    login.loginPage("/login")
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                )
                .rememberMe(config ->
                        config.rememberMeServices(rememberMeServices())
                )
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception->
                    exception.accessDeniedPage("/access-denied")
                )
                .authorizeRequests(config ->
                        config
                                .antMatchers("/").permitAll()
                                .antMatchers("/login").permitAll()
                                .antMatchers("/error").permitAll()
                                .antMatchers("/signup").permitAll()
                                .antMatchers("/signup/*").permitAll()
                                .antMatchers("/student/**").hasAnyAuthority(Authority.ROLE_ADMIN, Authority.ROLE_STUDENT)
                                .antMatchers("/teacher/**").hasAnyAuthority(Authority.ROLE_ADMIN, Authority.ROLE_TEACHER)
                                .antMatchers("/manager/**").hasAuthority(Authority.ROLE_ADMIN)
                )
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
