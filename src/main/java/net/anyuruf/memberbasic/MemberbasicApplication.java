package net.anyuruf.memberbasic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.anyuruf.memberbasic")
public class MemberbasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberbasicApplication.class, args);
	}

}
