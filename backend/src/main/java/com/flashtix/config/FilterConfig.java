package com.flashtix.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final AdminApiKeyFilter adminApiKeyFilter;

    public FilterConfig(AdminApiKeyFilter adminApiKeyFilter) {
        this.adminApiKeyFilter = adminApiKeyFilter;
    }

    @Bean
    public FilterRegistrationBean<AdminApiKeyFilter> adminApiKeyFilterRegistration() {
        FilterRegistrationBean<AdminApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(adminApiKeyFilter);
        registration.addUrlPatterns("/api/admin/*");
        registration.setOrder(1);
        return registration;
    }
}

