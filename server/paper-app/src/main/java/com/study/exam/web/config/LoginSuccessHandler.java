package com.study.exam.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        handle(request, response, requestCache.getRequest(request, response), authentication);
        clearAuthenticationAttributes(request);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        var session = request.getSession(false);
        if(session!=null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            SavedRequest savedRequest,
            Authentication authentication
    ) throws IOException {
        var targetUrl = determineTargetUrl(request,savedRequest, authentication);
        if(response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final HttpServletRequest request,
                                        SavedRequest savedRequest,
                                        final Authentication authentication) {
        if(savedRequest!=null) {
            var redirectUrl = savedRequest.getRedirectUrl();
            if(redirectUrl!=null && !redirectUrl.startsWith("/login")) {
                return savedRequest.getRedirectUrl();
            }
        }

        return switch(request.getParameter("site")) {
            case "manager" -> "/manager";
            case "student" -> "/student";
            case "teacher" -> "/teacher";
            default -> "/";
        };

    }
}
