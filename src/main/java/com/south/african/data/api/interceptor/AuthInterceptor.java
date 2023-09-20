package com.south.african.data.api.interceptor;

//import com.google.common.collect.ImmutableMap;
//import com.south.african.schools.api.util.encoding.Json;
//import com.south.african.schools.api.util.request.Request;
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
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        // TODO Authentication stuff to be done here.
        return true;
    }

    @Override
    public void postHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final ModelAndView modelAndView) throws Exception {
        // TODO Post authentication stuff to be done here.
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex) {
    }
}
