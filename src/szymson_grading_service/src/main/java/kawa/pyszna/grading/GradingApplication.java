package kawa.pyszna.grading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class GradingApplication {

	public static void main(String[] args) {
		Grader g = new Grader();
		GradeDTO gdto = g.grade(1.0, 2.0);
		SpringApplication.run(GradingApplication.class, args);
	}

}
