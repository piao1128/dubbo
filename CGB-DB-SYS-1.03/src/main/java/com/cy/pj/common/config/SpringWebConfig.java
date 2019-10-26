package com.cy.pj.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

//@Configuration
public class SpringWebConfig {
	//@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
		FilterRegistrationBean<DelegatingFilterProxy> sBean = new FilterRegistrationBean<DelegatingFilterProxy>();
		sBean.setFilter(new DelegatingFilterProxy("shiroFilterFactory"));
		sBean.addUrlPatterns("/*");
		return sBean;
	}
}
