package avmb.desafio.AstenTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AstenTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstenTaskApplication.class, args);
	}

}
