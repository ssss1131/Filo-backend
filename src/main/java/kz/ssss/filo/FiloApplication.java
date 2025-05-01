package kz.ssss.filo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FiloApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiloApplication.class, args);
	}

}
