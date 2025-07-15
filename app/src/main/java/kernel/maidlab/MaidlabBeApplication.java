package kernel.maidlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class MaidlabBeApplication {

	public static void main(String[] args) {

		loadEnvFile();
		SpringApplication.run(MaidlabBeApplication.class, args);
	}
	private static void loadEnvFile() {
		try {
			Path envPath = Paths.get(".env");
			if (!Files.exists(envPath)) {
				envPath = Paths.get("../.env");
			}

			if (Files.exists(envPath)) {
				Files.lines(envPath)
						.filter(line -> line.contains("=") && !line.startsWith("#"))
						.forEach(line -> {
							String[] parts = line.split("=", 2);
							if (parts.length == 2) {
								String key = parts[0].trim();
								String value = parts[1].trim();
								System.setProperty(key, value);
								System.out.println("Loaded: " + key + "=" + value); // 디버깅용
							}
						});
				System.out.println(".env file loaded successfully");
			} else {
				System.out.println(".env file not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
