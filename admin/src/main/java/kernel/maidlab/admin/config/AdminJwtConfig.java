package kernel.maidlab.admin.config;

import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServletRequest;

import kernel.maidlab.admin.auth.entity.Admin;
import kernel.maidlab.admin.auth.jwt.AdminJwtFilter;
import kernel.maidlab.api.exception.BaseException;
import kernel.maidlab.common.enums.ResponseType;

@Configuration
public class AdminJwtConfig {

	public static boolean isNotAuthenticated(HttpServletRequest request) {
		return request.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY) == null;
	}

	public static String getCurrentAdminKey(HttpServletRequest request) {
		return (String)request.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY_VALUE);
	}

	public static Admin getCurrentAdmin(HttpServletRequest request) {
		Object admin = request.getAttribute(AdminJwtFilter.CURRENT_ADMIN_KEY);
		return (admin instanceof Admin) ? (Admin)admin : null;
	}

	public static void checkAdminAuth(HttpServletRequest request) {
		if (isNotAuthenticated(request)) {
			throw new BaseException(ResponseType.AUTHORIZATION_FAILED);
		}
		if (getCurrentAdmin(request) == null) {
			throw new BaseException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}
	}
}