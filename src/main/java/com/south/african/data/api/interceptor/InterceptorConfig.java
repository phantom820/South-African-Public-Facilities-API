package com.south.african.data.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Interceptor for pre handler and post handler logic.
 */
@Component
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    /**
     * Logging interceptor.
     */
    @Autowired
    private LoggingInterceptor loggingIntercepto;

    /**
     * Auth interceptor.
     */
    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * Query interceptor.
     */
    @Autowired
    private QueryInterceptor queryInterceptor;


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(loggingIntercepto);
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(queryInterceptor);
    }
}
