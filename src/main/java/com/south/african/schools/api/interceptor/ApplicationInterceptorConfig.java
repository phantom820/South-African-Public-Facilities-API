package com.south.african.schools.api.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Interceptor for pre handler and post handler logic.
 */
@Component
public class ApplicationInterceptorConfig extends WebMvcConfigurerAdapter {

    /**
     * Default application interceptor.
     */
    @Autowired
    private ApplicationInterceptor applicationInterceptor;


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(applicationInterceptor);
    }
}
