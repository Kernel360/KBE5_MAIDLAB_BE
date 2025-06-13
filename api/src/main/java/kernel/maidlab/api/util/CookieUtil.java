package kernel.maidlab.api.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@Component
public class CookieUtil {

	@Value("${app.cookie.domain:}")
	private String cookieDomain;

	@Value("${app.cookie.secure:false}")
	private boolean cookieSecure;

	@Value("${app.cookie.same-site:Lax}")
	private String cookieSameSite;

	public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
		ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(cookieSecure)
			.path("/")
			.maxAge(Duration.ofDays(7))
			.sameSite(cookieSameSite);

		if (cookieDomain != null && !cookieDomain.trim().isEmpty()) {
			cookieBuilder.domain(cookieDomain);
		} else {
		}

		ResponseCookie cookie = cookieBuilder.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public String getRefreshTokenFromCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("refreshToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public void clearRefreshTokenCookie(HttpServletResponse response) {
		ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("refreshToken", "")
			.httpOnly(true)
			.secure(cookieSecure)
			.path("/")
			.maxAge(Duration.ZERO)
			.sameSite(cookieSameSite);

		if (cookieDomain != null && !cookieDomain.trim().isEmpty()) {
			cookieBuilder.domain(cookieDomain);
		}

		ResponseCookie cookie = cookieBuilder.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}
}