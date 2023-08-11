package com.south.african.schools.api.interceptor;

import com.south.african.schools.api.util.request.Request;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;;

/**
 * Auth interceptor.
 */
@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @SneakyThrows
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler)
            throws Exception {

        final Request req = new Request();
        log.info("Auth interceptor processing request {}", req.getId());
        request.setAttribute(Request.KEY, req);

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {

        final Request req = (Request) request.getAttribute(Request.KEY);
        log.info("Auth interceptor post processing request {}", req.getId());
    }
}
