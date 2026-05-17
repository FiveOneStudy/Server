package fiveonestudy.ddait.global.config;

import fiveonestudy.ddait.global.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XssConfig {

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(new XssFilter());
        filter.setOrder(1);
        filter.addUrlPatterns("/*");
        return filter;
    }
}