package com.south.african.schools.api.interceptor;

import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.QueryParameters;
import com.south.african.schools.api.util.request.Request;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Default application interceptor.
 */
@Component
@Slf4j
public class ApplicationInterceptor implements HandlerInterceptor {

    @SneakyThrows
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler)
            throws Exception {

        log.info("Pre Handle method is Calling");
        final Map<String, String[]> parameters = request.getParameterMap();
        QueryParameters.validateParameters(parameters);
        final QueryParameters.MaxResults maxResults = QueryParameters.extractMaxResults(parameters);
        final QueryParameters.NextToken nextToken = QueryParameters.extractNextToken(parameters);
        final Map<String, ImmutableSet<String>> filters = QueryParameters.extractFilters(parameters);
        final Request req = new Request(filters, maxResults, nextToken);
        request.setAttribute(Request.REQUEST_KEY, req);
        log.info("Processing request id {}", req.getRequestId());

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {

        log.info("Post Handle method is Calling");
    }
}
