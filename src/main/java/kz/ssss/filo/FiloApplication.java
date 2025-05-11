package kz.ssss.filo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Cloud Storage API",
				version = "1.0",
				description = "API documentation for the file storage system",
				contact = @Contact(name = "ssss1131")
		)
)
@SpringBootApplication
public class FiloApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiloApplication.class, args);
	}

}
