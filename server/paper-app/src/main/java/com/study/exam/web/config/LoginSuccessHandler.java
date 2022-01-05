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
        handle(request, response, requestCache.getRequest(request, response));
        clearAuthenticationAttributes(request);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, requestCache.getRequest(request, response));
        clearAuthenticationAttributes(request);
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
            SavedRequest savedRequest
    ) throws IOException {
        var targetUrl = determineTargetUrl(request,savedRequest);
        if(response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to {}", targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    /**
     * 어디로 redirect 시킬 지에 대해서 TargetUrl을 정하는 메서드
     * @param request - HttpServletRequest. 쿼리파라미터를 확인하기 위해 필요.
     * @param savedRequest - SavedRequest. ServletRequest 데이터를 캐싱해서 읽었을 때 문제가 없게 하기 위해 사용.
     * @return site에 따른 redirect URL
     */
    protected String determineTargetUrl(final HttpServletRequest request,
                                        SavedRequest savedRequest) {
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
