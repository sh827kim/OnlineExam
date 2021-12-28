package com.study.exam.web.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomLoginFilter(
            AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices
    ) {
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
        this.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        this.setAuthenticationFailureHandler(new LoginFailureHandler());
        this.setRememberMeServices(rememberMeServices);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var userLogin = UserLogin.builder()
                .username(request.getParameter("username"))
                .password(request.getParameter("password"))
                .site(request.getParameter("site"))
                .rememberme(request.getParameter("remember-me")!=null)
                .build();
        var authToken = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(),
                userLogin.getPassword(),
                null);

        return authenticationManager.authenticate(authToken);
    }
}
