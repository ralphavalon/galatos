package galatos.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import galatos.notification.request.NotificationRequestValidator;

@SpringBootApplication
public class GalatosApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GalatosApplication.class, args);
	}
	
	@Bean
	public NotificationRequestValidator requestValidator() {
		return new NotificationRequestValidator();
	}
	
}
