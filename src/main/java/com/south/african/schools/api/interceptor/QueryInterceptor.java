package com.south.african.schools.api.interceptor;

import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.query.Query;
import com.south.african.schools.api.util.query.parameter.MaxResult;
import com.south.african.schools.api.util.query.parameter.NextToken;
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
 * The interceptor for validating the query parameters.
 */
@Component
@Slf4j
public class QueryInterceptor implements HandlerInterceptor {

    @SneakyThrows
    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler)
            throws Exception {

        log.info("Query Interceptor Pre Handle method is Calling");
        final Map<String, String[]> parameters = request.getParameterMap();

        Query.validateParameters(parameters);
        final Map<String, ImmutableSet<String>> filters = Query.extractFilters(parameters);
        final MaxResult maxResult = new MaxResult(parameters);
        final NextToken nextToken = new NextToken(parameters);
        final Query query = new Query(filters, maxResult, nextToken);

        final Request req = (Request) request.getAttribute(Request.KEY);
        request.setAttribute(Query.KEY, query);
        log.info("Query interceptor forwarding request id {}", req.getId());

        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler,
                           final ModelAndView modelAndView) throws Exception {

        log.info("Query Interceptor Post Handle method is Calling");
    }
}
