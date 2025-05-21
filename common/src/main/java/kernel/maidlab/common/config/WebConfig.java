package kernel.maidlab.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public void addCorsMappings(CorsRegistry registry){
		registry.addMapping("/api/**")
			.allowedOrigins("http://localhost:5173", "https://kbe-5-maidlab-fe.vercel.app")
			.allowedMethods("*")
			.allowCredentials(true);
	}



}

