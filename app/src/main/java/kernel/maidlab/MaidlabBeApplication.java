package kernel.maidlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MaidlabBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaidlabBeApplication.class, args);
	}

}
