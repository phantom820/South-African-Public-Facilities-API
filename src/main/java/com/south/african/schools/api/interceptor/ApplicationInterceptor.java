package com.south.african.schools.api.interceptor;

import com.google.common.collect.ImmutableSet;
import com.south.african.schools.api.util.encoding.Json;
import com.south.african.schools.api.util.query.QueryParameterException;
import com.south.african.schools.api.util.query.QueryParameters;
import com.south.african.schools.api.util.request.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Default application interceptor.
 */
@Component
@Slf4j
public class ApplicationInterceptor implements HandlerInterceptor {


    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong | variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long timeLow = (currentTimeMillis & 0x0000_0000_FFFF_FFFFL) << 32;
        final long timeMid = ((currentTimeMillis >> 32) & 0xFFFF) << 16;
        final long version = 1 << 12;
        final long timeHi = ((currentTimeMillis >> 48) & 0x0FFF);
        return timeLow | timeMid | version | timeHi;
    }


    @SuppressWarnings("checkstyle:missingjavadocmethod")
    public static UUID generateType1UUID() {
        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits);
    }

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler)
            throws Exception {

        log.info("Pre Handle method is Calling");
        try {
            final Map<String, String[]> parameters = request.getParameterMap();
            QueryParameters.validateParameters(parameters);
            final QueryParameters.MaxResults maxResults = QueryParameters.extractMaxResults(parameters);
            final QueryParameters.NextToken nextToken = QueryParameters.extractNextToken(parameters);
            final Map<String, ImmutableSet<String>> filters = QueryParameters.extractFilters(parameters);
            final Request req =   new Request(generateType1UUID().toString(), filters, maxResults, nextToken);
            request.setAttribute(Request.REQUEST_KEY, req);
            log.info("Processing request id {}", req.getRequestId());

        } catch (QueryParameterException exception) {
            response.setStatus(exception.getHttpStatus().value());
            response.getWriter().write(Json.marshal(exception).toJSONString());
            response.setContentType("application/json");
            return false;
        }

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
