package kernel.maidlab.admin.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;
import kernel.maidlab.admin.auth.jwt.AdminJwtFilter;
import kernel.maidlab.api.auth.jwt.JwtFilter;

@Configuration
public class AdminFilterConfig {
	@Bean
	@ConditionalOnClass(AdminJwtFilter.class)
	public FilterRegistrationBean<Filter> AdminJwtFilterRegistration(AdminJwtFilter jwtFilter) {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(jwtFilter);
		registration.addUrlPatterns("/api/admin/*"); // 인증이 필요한 경로만 필터링
		registration.setName("AdminJwtFilter");
		registration.setOrder(1);

		return registration;
	}
}
