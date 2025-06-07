package kernel.maidlab.api.config;

import jakarta.servlet.Filter;

import kernel.maidlab.api.auth.jwt.JwtFilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	@ConditionalOnClass(JwtFilter.class)
	public FilterRegistrationBean<Filter> jwtFilterRegistration(JwtFilter jwtFilter) {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(jwtFilter);
		registration.addUrlPatterns("/api/auth/*", "/api/consumer/*", "/api/manager/*", "/api/board/*"); // 인증이 필요한 경로만 필터링
		registration.setName("jwtFilter");
		registration.setOrder(2);

		return registration;
	}
}

