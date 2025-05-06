package com.example.secure_password_vault.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRouterConfig {
	
	@Bean
	public FilterRegistrationBean<RateLimitingFilter> rareLimitFilter() {
		FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new RateLimitingFilter());
		registrationBean.addUrlPatterns("/auth/login");
		return registrationBean;
	}
}
