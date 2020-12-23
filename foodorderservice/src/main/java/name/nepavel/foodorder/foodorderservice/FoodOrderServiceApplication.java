package name.nepavel.foodorder.foodorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FoodOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodOrderServiceApplication.class, args);
	}

}
