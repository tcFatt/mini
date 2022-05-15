package nusiss.mini;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nusiss.mini.filter.AuthenticationFilter;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> registerFilters() {
        FilterRegistrationBean<AuthenticationFilter> regFilter = new FilterRegistrationBean<>();
        regFilter.setFilter(new AuthenticationFilter());
        regFilter.addUrlPatterns("/secure/*");
        return regFilter;
    }
    
}
