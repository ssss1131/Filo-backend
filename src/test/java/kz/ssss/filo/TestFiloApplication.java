package kz.ssss.filo;

import org.springframework.boot.SpringApplication;

public class TestFiloApplication {

	public static void main(String[] args) {
		SpringApplication.from(FiloApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
