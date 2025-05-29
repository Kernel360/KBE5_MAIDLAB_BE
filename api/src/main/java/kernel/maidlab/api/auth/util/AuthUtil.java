package kernel.maidlab.api.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import kernel.maidlab.api.auth.entity.Consumer;
import kernel.maidlab.api.auth.entity.Manager;

import kernel.maidlab.api.auth.jwt.JwtDto;
import kernel.maidlab.api.auth.jwt.JwtProvider;
import kernel.maidlab.api.exception.custom.AuthException;
import kernel.maidlab.common.enums.ResponseType;
import kernel.maidlab.common.enums.UserType;

import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

	private final JwtProvider jwtProvider;

	public AuthUtil(JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	public <T> T getCurrentUser(HttpServletRequest request, Class<T> clazz) {
		String token = jwtProvider.extractToken(request);
		JwtDto.ValidationResult result = jwtProvider.validateAccessToken(token);

		if (!result.isValid()) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		String uuid = result.getUuid();
		UserType userType = result.getUserType();
		Object user = jwtProvider.findUser(uuid, userType);

		if (user == null) {
			throw new AuthException(ResponseType.THIS_USER_DOES_NOT_EXIST);
		}

		if (!clazz.isInstance(user)) {
			throw new AuthException(ResponseType.DO_NOT_HAVE_PERMISSION);
		}

		return clazz.cast(user);
	}

	public String getUuid(HttpServletRequest request) {
		String token = jwtProvider.extractToken(request);
		JwtDto.ValidationResult result = jwtProvider.validateAccessToken(token);

		if (!result.isValid()) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		return result.getUuid();
	}

	public UserType getUserType(HttpServletRequest request) {
		String token = jwtProvider.extractToken(request);
		JwtDto.ValidationResult result = jwtProvider.validateAccessToken(token);

		if (!result.isValid()) {
			throw new AuthException(ResponseType.AUTHORIZATION_FAILED);
		}

		return result.getUserType();
	}

	public Manager getManager(HttpServletRequest request) {
		return getCurrentUser(request, Manager.class);
	}

	public Consumer getConsumer(HttpServletRequest request) {
		return getCurrentUser(request, Consumer.class);
	}
}