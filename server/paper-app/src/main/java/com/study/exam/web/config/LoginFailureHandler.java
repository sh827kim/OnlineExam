package com.study.exam.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        var session = request.getSession(false);
        if(session!=null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    protected void handle(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        var targetUrl = determineTargetUrl(request);
        if(response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(final HttpServletRequest request) {
        return switch (request.getParameter("site")) {
          case "manager" -> "/login?site=manager&error=true";
          case "student" -> "/login?site=student&error=true";
          case "teacher" -> "/login?site=teacher&error=true";
            default -> "/";
        };
    }
}
