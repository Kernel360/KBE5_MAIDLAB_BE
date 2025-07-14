package kernel.maidlab.core.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kernel.maidlab.core.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)

			// API 경로별 접근 권한 설정
			.authorizeHttpRequests(auth -> auth
				// 공개 API (인증 불필요)
				.requestMatchers(
					"/api/auth/login",
					"/api/auth/sign-up",
					"/api/auth/social-login",
					"/api/auth/social-sign-up",
					"/api/auth/refresh",
					"/api/admin/auth/login",
					"/api/admin/auth/refresh",
					"/api/events",
					"/swagger-ui/**",
					"/v3/api-docs/**",
					"/swagger-resources/**",
					"/webjars/**",
					"/favicon.ico",
					"/error"
				).permitAll()

				// 관리자 전용 API
				.requestMatchers("/api/admin/**").hasRole("ADMIN")

				// 소비자 전용 API
				.requestMatchers("/api/consumers/**").hasRole("CONSUMER")

				// 매니저 전용 API
				.requestMatchers("/api/managers/**").hasRole("MANAGER")

				// 소비자 + 매니저 공통 API (인증된 사용자만)
				.requestMatchers(
					"/api/auth/change-password",
					"/api/auth/logout",
					"/api/auth/withdraw",
					"/api/board/**",
					"/api/matching/**",
					"/api/reservations/**",
					"/api/points/**",
					"/api/aws/**"
				).hasAnyRole("CONSUMER", "MANAGER")

				// 나머지 모든 요청은 인증 필요
				.anyRequest().authenticated()
			)

			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));

		// 인증 정보 허용 (쿠키, Authorization 헤더 등)
		configuration.setAllowCredentials(true);

		// 캐시 시간 설정
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}