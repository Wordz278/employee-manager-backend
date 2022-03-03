package com.kwambi.employeesystem;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.kwambi.employeesystem.constant.FileConstant.*;

@SpringBootApplication
public class EmployeesystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeesystemApplication.class, args);
		new File(USER_FOLDER).mkdirs();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
