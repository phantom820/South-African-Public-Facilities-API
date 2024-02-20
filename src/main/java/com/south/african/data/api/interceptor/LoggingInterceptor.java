package com.south.african.data.api.interceptor;

import com.south.african.data.api.util.encoding.Json;
import com.south.african.data.api.util.request.Request;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logging interceptor. This creates the request and the request id to the thread context, so it appears as a field in logs.
 * Additionally, metrics are posted after completion.
 */
@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @SneakyThrows
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler) throws Exception {

        final Request req = new Request();
        ThreadContext.put("requestId", req.getId());
        log.info("Starting processing request : " + req.getId());
        request.setAttribute(Request.KEY, req);

        return true;
    }

    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex) {

        final Request req = (Request) request.getAttribute(Request.KEY);
        req.setEndTimeMillis(System.currentTimeMillis());
        req.getMetrics().setLatencyMillis(req.getEndTimeMillis() - req.getStartTimeMillis());
        ThreadContext.put("metrics", Json.string(req.getMetrics()));
        log.info("Finished processing request : " + req.getId());
    }
}
