package net.anyuruf.memberbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;

@SpringBootApplication
public class MemberbasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberbasicApplication.class, args);
	}

	@Configuration
	public class GraphQlConfig {
		@Bean
		public RuntimeWiringConfigurer runtimeWiringConfigurer() {
			return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date);
		}
	}
}
