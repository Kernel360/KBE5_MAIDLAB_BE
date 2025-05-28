package kernel.maidlab.api.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

	private String secretKey;
	private TokenExpiration expiration = new TokenExpiration();
	private String header;
	private String prefix;

	@Getter
	@Setter
	public static class TokenExpiration {
		private Long access;
		private Long refresh;
	}

}
